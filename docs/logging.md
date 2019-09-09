# Logging

The interpreter provides a logging facility to trace the RPG program execution.
To enable the log functionality it is required to specify the configuration file using the **-lc** or  **--log-configuration** on the runner command line, the muterunner command line or any other tool.

In order to monitor the interpreter behaviour at the runtime the logging is subdivided in channels each one designed to capture a specific aspect of the interpreter. 

The output consists of a set of data record, with a fixed header segment with common data and a variable data segment specific for each channel. 

The values of the record are separated by a character specified in the configuration  file.

The available channels are:

* **Data:** monitors the accesses to the variables.
* **Expression:** traces the expressions encountered during the program execution.
* **Statement:**  provides information about the statements executed.
* **Loop:** captures the loop execution including the number of iterations
* **Performance:** measures the execution time
* **Resolution:** provides information about the process to identify the routines or programs to invoke. 

## Configuration File
The logging configuration file is a plain text file containing the parameters to enable/disable the channels and set the output device. 

```
#
#  Logging configuration file
#

logger.data.separator =  \t
logger.date.pattern = HH:mm:ss.SSS

logger.file.path = /var/log/smeup-rpg
logger.file.name = log.log

data.level = all
data.output = console

loop.level = all
loop.output = console

expression.level = all
expression.output = console

statement.level = all
statement.output = file

performance.level = all
performance.output = console

resolution.level = off
resolution.output = console

```

The value specified in **logger.data.separator** is the character used to 
separate the values within a record. 

Each record has contains a timestamp in the **logger.date.pattern** allows
to specify the format of the data. 

The logging allows to redirect the output to the console or to file. 
In case of file the parameters **logger.file.path** and **logger.file.name** 
allow to set the path and the filename of the log file. 

The channel configuration consist of two values: **level** and **output**. 

* The **level** allows to enable or disable the channel, the possible values are
**all** and **off**.

* The **output** parameter specifies where the log will be redirected, in case is set
to **console**, the logging information are written on the screen otherwise if 
set ot **file** the data will be appended to the file specified by **logger.file.path** 
and **logger.file.name**. 

## Log file format

Each log record consists of a fixed header containing the common data and a 
variable data segment which depends on the channel.


```
<TIMESTAMP><S><FILENAME><S><LINE><S><CHANNEL><S><CHANNEL SPECIFIC>
+------------------ header -------------------+----- data ------+
```     

The header contains the following data:

* **TIMESTAMP** timestamp of the record
* **FILENAME** name of the source file
* **LINE** the line of code, if available
* **CHANNEL** a mnemonic name of the channel 

The **S** represent the separator character specified in the configuration.

## Data Channel DATA
The monitors the accesses to the variables during the program execution.  

```
14:25:29.471 TEST_06.rpgle    DATA NBR = 0            10
+-----------+-------------+--+---+---- data -----+- result -+
```     
The log record collects the initial value zero in this case and the new value
assigned by a statement. In the example above the value of variable NBR has an
initial value of 0 (zero) and assume the value of 10.


## Statement Channel STMT
The statement channel captures information about the statement executed.  

```
11:30:38.893 TEST_06.rpgle 45 STMT EVAL WORDINC = J - I	   5
+-----------+-------------+--+---+---- statement -----+- result -+
```     

In the example above the record contains the statement executed `EVAL WORDINC = J - I`
and the result of the expression, in this case **5**.

When statement evaluate a comparison operator the result represent the logical
value of the expression.

```
11:30:38.893 TEST_06.rpgle 45 STMT	SELECT WHEN	NBR = 0	(false)
11:30:38.893 TEST_06.rpgle 47 STMT	SELECT WHEN	NBR = 1	(true)
+-----------+-------------+--+---+---- statement -----+- result -+
```

The statement channel also tracks the start and the end of a
program or subroutine. 

```
11:30:38.893 TEST_06.rpgle 45 STMT	SELECT SUBROUTINE START	FIB

11:30:38.893 TEST_06.rpgle 55 STMT	SUBROUTINE END	FIB
+-----------+-------------+--+---+--------- statement ---------+ 
```

## Expression Channel EXPR
The expression channel collect all the expressions encountered during the program execution.

```
14:14:30.330 TEST_06.rpgle 28 EXPR A + B                3
+-----------+-------------+--+---+-- expression --+- result -+
```


## Loop Channel LOOP
The loop channel captures the execution of loops.
Two log records are created, the first when the loop starts and second
when the loop exits. The end loop record include the number of cycles 
actually executed.

```
14:14:30.330 TEST_06.rpgle 28 LOOP FOR J = 1  TO 4
14:14:30.571 TEST_06.rpgle 35 LOOP ENDFOR J              4
+-----------+-------------+--+---+----- loop ------+- result -+
```

The example below shows a DOW loop execution.

```
14:14:30.330 TEST_06.rpgle 13 LOOP DOW LOOP START COUNT < 100 
14:14:30.556 TEST_06.rpgle 22 STMT LEAVE
14:14:30.571 TEST_06.rpgle 33 LOOP DOW LOOP END                   45	
+-----------+-------------+--+---+---------- loop -----------+- result -+
```
Please note that statements like LEAVE may affect the number of cycles
actually executed.

## Performace Channel PERF
The performance channel measures the execution time of loops, programs and
subroutines. 
This kind of records are generated at the end of the execution, measuring the
time in milliseconds.

```
15:09:46.910 TEST_06.rpgle 79 PERF ENDFOR I                       8 ms
15:09:46.910 TEST_06.rpgle 80 PERF SUBROUTINE END PRINT           9 ms
15:09:46.910 TEST_06.rpgle    PERF END TEST_06.rpgle            160 ms
+-----------+-------------+--+---+---------- loop -----------+- result -+
```
