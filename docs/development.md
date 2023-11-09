# Development


This document contains information for developers who want to contribute to the the project.

## How to compile the code

The project uses gradle and a gradle wrapper is included. That means that you can simply execute `./gradlew` (on Mac/Linux) or `gradlew.bat` (on Windows) to run it.  

There are a few parameters that can be used in order to customize gradle execution tasks, it follows the list and a brief explanation
when the names are not exhaustive:
 * org.gradle.jvmargs
 * FlightRecorderOptions - Used if you want to profile with JFR
 * kotlinVersion
 * serializationVersion - Kotlin serialization apis version
 * jvmVersion
 * reloadVersion - Allows to customize reload version used by jariko

Default values of these parameters are in `gradle.properties` file.

For example if you want to execute all tests with a specific reload version you can type:  
```
./gradlew -PreloadVersion=yourVersion test
```

The project contains an ANTLR grammar from which a parser in Java is generated. To generate it run:

```
./gradlew generateGrammarSource
```

The code can then be compiled with:

```
./gradlew build
```

### IDEA 2020
Importing project from github should be enough.

### IDEA 2019
After cloning the project, run
```
./gradlew idea
```
Then run the tests:
```
./gradlew test
```
You are now ready to import the gradle project into IDEA. We suggest to flag the *"Automatically import this project on changes in build script files"* option in gradle settings.

[Here is a short video on how to setup a Linux workstation to develop this project with IDEA 2019](https://youtu.be/eByxIBsLMp4)

### IDEA 2018
Then import in IDEA 2018 using these options:

![Idea import project options](images/setup/idea.png)

**It's very important not to check "Create separate module per source set"!!!**

[Here is a short video on how to setup a Linux workstation to develop this project with IDEA 2018](https://youtu.be/4Kd1b-VPTEs)

## Running tests

All tests (except for performance tests) can be executed by running:

```
./gradlew check
```

To run performance tests (i.e. tests tagged with the annotation `@Category(PerformanceTest::class)`) run:
```
./gradlew testPerformance
```

You can collect data about failed performance tests in a .csv file using:
```
./gradlew testPerformance -DexportCsvFile="/some/file.csv"
```

To run all tests:
```
./gradlew testAll
```


If you want to force the execution of all checks:

```
./gradlew check -rerun-tasks
```

(_Side note: if you get this error running tests_
 ```
 com.esotericsoftware.kryo.KryoException: Buffer underflow
 ```
 _try to clean the .gradle directory_)

## Dependency from develop-SNAPSHOT
This snapshot is published in sonatype, if you want to work with this version you can:
 - cloning jariko repo and to deploy in maven local by using ./gradlew deploy
 - or adding this maven repository url https://s01.oss.sonatype.org/content/repositories/snapshots/ to your pom or gradle script

## Tests regarding db native access

Jariko uses the [reload library](https://github.com/smeup/reload).  
Since reload needs a configuration that provides, per file or files group, the jdbc settings, 
you can pass these information through system property `jrkReloadConfig`.  
The value of this property is the path of a json file, and the payload is like this:  
 ```
 {
    "connectionConfigs": [
        {
            "fileName": "*",
            "url": "jdbc:as400://servername/MY_LIB",
            "user": "MY_USER",
            "password": "MY_PASSWORD",
            "driver": "com.ibm.as400.access.AS400JDBCDriver"
        }
    ]
}
 ```
This configuration says to reload that all files are:
 * located on as400 server servername: jdbc:as400://servername
 * in library: MY_USER
 * and the user used for the jdbc connection will be: MY_USER
 
 
## Profiling

You can create a jfr file (java flight recorder) at the end
of RPG program interpretation, jfr file path will be showed in console.  
This feature allows to evaluate bottlenecks, and improve jariko performance.  
Usage:
```
./gradlew profileRpgProgram -PrpgProgram=path_to_rpg_program
```

## Enabling experimental features

### Try new features by implementing a new instance of IFeaturesFactory

Jariko features are modeled by a factory that implements: `com.smeup.rpgparser.interpreter.IFeaturesFactory`

You can select a factory through system property: `-Djariko.featuresFactory=<factory.id>`.  
Where `<factory.id>` could be:
* default
* experimental
* Factory class implementation

Configuration for _default_ and _experimental_ factory is in: `META-INF/com.smeup.jariko/features.properties`

### Try new features with feature flags

You can try new features also through the feature flags.  
When you run jariko you will see in console something like this:

```
------------------------------------------------------------------------------------
Creating features factory: com.smeup.rpgparser.interpreter.StandardFeaturesFactory
------------------------------------------------------------------------------------
Feature flags status:
 - jariko.features.UnlimitedStringTypeFlag: off
------------------------------------------------------------------------------------
```

This it means that jariko is using the default `IFeaturesFactory` implementation (creating features factory...), 
but more relevant is the following part of the console message where it is displayed a list of available feature
flags and their status.  
What you see it means that currently jariko provides one feature flag named:
`jariko.features.UnlimitedStringTypeFlag` and its status is `off`.

**how to switch on a feature flag at runtime**  
Before to call jariko it is necessary set the system property like this:
```java
System.setProperty(featureFlagName, "on");
```
and for example if you want to try `jariko.features.UnlimitedStringTypeFlag` you can do:
```java
System.setProperty("jariko.features.UnlimitedStringTypeFlag", "on");
```

**how to switch on a feature flag via cli**  
For example if you want to execute all tests trying the feature flag `jariko.features.UnlimitedStringTypeFlag`:
```
./gradlew -Djariko.features.UnlimitedStringTypeFlag=on test
```


**available feature flags and description**

| feature flag                              | description                                                                                       |
|-------------------------------------------|---------------------------------------------------------------------------------------------------|
| `jariko.features.UnlimitedStringTypeFlag` | when `on` you ask jariko to force the use of `UnlimitedStringType` for all rpg alphanumeric types |
                                                                                                        |


## Creating a jar with all dependencies to run some examples

You can create a jar that includes all the dependencies:

```
./gradlew fatJar
```

This will produce the file

``` 
rpgJavaInterpreter-core/build/libs/rpgJavaInterpreter-core-all.jar
```

So you can run an RPGLE file from the command line this way (after moving to the directory that contains this jar):

``` 
java -jar rpgJavaInterpreter-core-all.jar path/to/my/RPGLE [parameters]
```

### Program Search Directories

In order to pass a list of directories containing your sources, you can use the -psd option (Program Search Directories) with a comma separated list of directories:

``` 
java -jar rpgJavaInterpreter-core-all.jar -psd /dir/one,/dir/two MYRPG [parameters]
```

[In this short video you can see how to run the examples](https://youtu.be/llw2vNeupA4)

See also this animation:

![Running interpreter from the command line](images/Command_line_interpreter.gif?raw=true "Running interpreter from the command line")

If you omit the program name, you will be able to call programs in a [simple shell](https://youtu.be/uNd6h5H2wTM).

The default JVM target for the project is 1.8, but if you want to do some experiments with different JVM targets, you can try to run the gradle build task setting the jvmVersion property.
For example:

```
./gradlew -PjvmVersion=1.6 fatJar
```

(Hint: the project is not fully compatible with JVM 1.6 :-)

## Autoformatting

It can be performed using the task `ktlintFormat`.

You can check formatting rules using:
``` 
gradlew ktlintCheck
``` 
We suggest to set the *"Kotlin style guide"* as the code style for this project: 
![Code style settings](images/code_style.png)
