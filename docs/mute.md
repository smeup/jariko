# Mute: MUltiplatform TEsts

This interpreter can process annotations in RPG code to be used to define assertions for testing purposes. These annotations are called Mute for historical reasons.

## Syntax

### Assertions that compare two values
The Mute annotations that compare two values looks like this:

```
MU* VAL1(B) VAL2(8) COMP(EQ)
```

In this case we are stating that we expect the value B to be equal to 8.

The operators supported are:
   
* **EQ** equal
* **NE** not equal
* **GT** greater than
* **GE** greater than or equal
* **LT** lower than
* **LE** lower than or equal

The Mute annotations are verified _after_ executing the statement they annotate. Typically the annotate the statement immediately following them. Note that multiple annotations can be applied to the same statement:

```
    MU* VAL1(A) VAL2(5) COMP(EQ)
    MU* VAL1(B) VAL2(8) COMP(EQ)
    MU* VAL1(RESULT) VAL2(13) COMP(EQ)
     C                   EVAL      RESULT = A + B
```     
### Timeout assertions 
For testing of performances, there are MUTE annotations that introduce a timeout in the execution:

```
    MU* TIMEOUT(123)
```

This instruction tells the MUTE running engine to fail if the current program has an execution time that exceeds 123 milliseconds.
Note that the assertion is verified at the end of the program, so it doesn't stop long running executions, but it just signals the timeout condition at the end of the interpretation.

You can add many ```TIMEOUT``` assertion in the same program, but, obviously, just the one with the shortest timeout is considered.

There isn't a mandatory position to insert the ```TIMEOUT``` annotation line, that, as other ```MUTE``` annotations, are just considered as comments by the standard interpreter engine.
 
## Command line utility

The Mute annotations can be verified using a utility contained in the class `com.smeup.rpgparser.mute.MuterunnerKt`.

For your convenience you can produce a JAR with all the dependencies by running:

```
./gradlew fatJar
```

And then run such class with this command:

```
java -cp rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar com.smeup.rpgparser.mute.MuterunnerKt
```

You may want to create an alias for convenience.

This program accepts the flags `-verbose` (default) and `-silent`.
All the other arguments are treated as paths to examine. If no paths are specified
the current directory is used.

Example of output produced:

```
MUTE Runner
(running in verbose mode)
paths to process: [../mutes_for_ci]
../mutes_for_ci/SIMPLE_MUTE.rpgle
Mute annotation at line 5 attached to statement 8
Mute annotation at line 6 attached to statement 8
Mute annotation at line 7 attached to statement 8

Total annotation: 3, executed: 3, failed: 0

Total files: 1, resolved: 3, executed: 3, failed:0

SUCCESS
```

## Mute as part of the CI

The utility to verify Mute annotation is run as part of the CI configuration. All Mute files contained in the directory _mutes_for_ci_ are automatically verified when running `./gradlew check`.

## Mute gradle task

You can run mute tests in the directory _mutes_for_ci_ using this gradle task:
```
./gradlew runMutes
```

If you want to run tests in a different directory, use the ```muteDir``` property. For example:
```
./gradlew runMutes -PmuteDir="../mute_tests"
```

To be more specific, you can follow these steps to run your own MUTE tests:

- Create a new folder to contain your Mutes: ```/my/dir/my_mutes```
- Copy in this folder the a config fil like ```mute_tests/mute_logging.config``` or  ```mutes_for_ci/mute_logging.config```
- Customize this file with your logging preferences
- Create your MUTE file in this folder.
- From the root directory of the smeup-rpg project, run ```./gradlew runMutes -PmuteDir="/my/dir/my_mutes"```

With this technique, you can have a (sort of) debug view of the execution of your program.

