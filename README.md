# JaRIKo: a JAva virtual machine Rpg Interpreter written in KOtlin 
![jariko Logo](/images/jariko_small.png)  

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/smeup/jariko.svg?branch=master)](https://travis-ci.org/smeup/jariko)
[![CircleCI](https://circleci.com/gh/smeup/jariko.svg?style=svg)](https://circleci.com/gh/smeup/jariko)
[![codebeat badge](https://codebeat.co/badges/220f492a-ca5b-4c88-aa11-c873b568a84b)](https://codebeat.co/projects/github-com-smeup-jariko-master)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/smeup/jariko)


# ⚡️ Cross-platform RPG Code in a polyglot environment ⚡️

## Introduction

JaRIKo is an [interpreter](https://en.wikipedia.org/wiki/Interpreter_(computing)) for the [RPG programming language](https://en.wikipedia.org/wiki/IBM_RPG). It runs on the JVM since it's written in Kotlin.

It is part of a bigger project named **Sme.UP Open Architecture**, by the italian software company **Sme.UP**. 

Sme.UP Open Architecture aims to build a **software platform for business applications** (also mistakenly known as ERP systems) and it is the core of **Sme.UP Data Platform**.  


### How might this work in the real world 

JaRIKo can definitely be used as a library: you push RPG code in, you have it executed, you get results out. And it works, as you can see in this animated gif of a simple on-the-fly online rpg-running application.

![rpgweb](/images/rpgwebdark.gif)  

This application is running **completely on Linux**
* recieves the RPG code via HTTP
* uses Jariko as a jar
* overwrites the DSPLY opcode, redirecting the output to the http-response. 
Pretty slick!

![rpgweb](/images/RPG_WEB@2x.png)


But, **for an enterprise-grade application**, there'll be a runtime environment running RPG and JVM code altogether, and it will likely work as in the following picture

![smeup_data_platform](/images/architecture.png)  

Once a request comes into the system, it's recognized as a "program" (a unit of execution) and redirected towards the right program handler: RPG is handled by JaRIKo (interpreter), the others by different executors.
**So JaRIKo is one (important) part of the whole architecture**


### Why are we doing that?
*Why building something that runs RPG, such an old and niche programming language?* 
RPG is widely common in the business and financial industry. There are tons of super-stable, higly-tested, mission-critical lines of RPG code running a big amount of businesses since tens of years. Big companies still rely on this programming language. 

This is for at least a couple of reasons. 

First, RPG was designed for business, it is very simple, powerful for data manipulation, but extremely inadequate to solving technology issue (like threads, async, http calls, cryptography, and so forth). The developer doesn't need to be aware of the technical details. The system provides all this technology, making code run on top of a platform that solves those issues.

This helped to create a **generation of RPG programmers** that are closer to **business consultants** than to developers, and this is very good for business applications development.

The second is that RPG only runs on [IBMi](https://en.wikipedia.org/wiki/IBM_i), best known as AS/400, that was also designed for business, is very reliable, fast, well-supported, and stable. **RPG leverages AS/400 architecture**, they are the perfect couple. 

### Focus on: 
#### 1- Doping your code

One of the core features of Jariko is the **doping mechanism**: once the code is taken over by the interpreter, every single program can be replaced at runtime. This allows to write a very flexible and polyglot software, choosing the right tool for the right job and having all the java (and jvm) power and ecosystem available.

![doping](/images/doping.png)  

Through doping, it's also much simpler to make the architectural design needed to deal with things like SPOOLS, DATAQUEUES, JOBS, DATAAREA, etc, typical of the OS/400 operating system, where the RPG code used to run.


#### 2- DSL
If you know how programming languages work, you also know that once you have a syntax three in your hands, you can do almost whatever you want. So, since RPG is best used for business applications, why don't add new high-level features to make the life of the RPG programmer easier? This is a step towards a Domain Specific Language

     C                   PARM                    §§METO
     C                   PARM                    §§SVAR
     C                   EVAL      PRICE=§§SVAR
     C                   CALC_VAT(PRICE)    
     
     
In this example CALC_VAT, standing for "calculate vat" does not exist in RPG, but the interpreter can handle it as a language keyword, with type check, syntax highlighting, code suggenstions, intellisense, errors an so on.

### Some questions

#### 1.So it sounds like you are developing an interpreter to run an application on multiple other platforms.
 Right
#### 2.What language is the interpreter being developed in?
 Mostly Kotlin, but the overall resulting system will be µ-service-based and we're going to use jvm languages.
#### 3.How do you transfer the database to another platform? SQL scripts?
 We have ETLs. We've already been doing that since years. SQL must not have dialect-specific statements, or it has to be re-arranged, but we're thinking about that.
#### 4.Do you handle multiple member files, DDM files, multiple physical logical files
 No. We're also not implementing DDS, SUBFILE, CLP. And absolutely none of the os/400 system apis.
#### 5.Data areas and user spaces? 
 Yes. Also QTEMP, RLA (read, write, chain, setll, etc...), SQLRPGLE (maybe...), /COPY

## Development

All information for developers is to be found in the [Development guide](docs/development.md).

### Features

The following list shows all the implemented features:  
- [Operation Codes](docs/operation_codes.md)  
- [Built in Functions](docs/built_in_functions.md)  
- [Subprocedures](docs/subprocedures.md)   
- [/COPY](docs/copy.md)  
- [/API](docs/api.md)   
- [AST Serialization](docs/ast_serialization.md)

## Logging

The project supports configurable logging, described in a [separate document](docs/logging.md).

## RPG Unit tests: the MUTE mechanism

We support special annotations to be used to write unit tests in RPG.
For details see [this document](docs/mute.md).

### MUTEs and Visual Studio Code

You can run MUTE tests in Visual Studio Code: see the [documentation](docs/visual_studio_code.md)

## How the repository is organized

* _generated-src_ contains the code generated from the grammar
* _gradle_, _gradlew_, and _gradlew.bat_ contain the gradle wrapper
* _src_ contains the source code for the project and the tests
* _out_, and _build_ contain temporary files
* _misc_ contains utilities for downloading sources from AS400 (for example [this ruby script](misc/ftpas.rb))
* _misc/docker_: support for [Docker, see instructions](misc/docker/docker.md)
* _docs_ contains documentation

## Are there any presentations about your work?
Yes, of course: see the [presentation list](docs/presentations.md)

## How to use this code in your project

At the moment, we use [Maven Central](https://repo.maven.apache.org/maven2/) to publish the [project](https://repo.maven.apache.org/maven2/io/github/smeup/jariko).

### Maven	
Then add the following dependencies for the core library:
	
    <dependency>
        <groupId>io.github.smeup.jariko</groupId>
        <artifactId>rpgJavaInterpreter-core</artifactId>
        <version>develop-SNAPSHOT</version>
    </dependency>

And this if you want to include the examples too:
		
    <dependency>
        <groupId>io.github.smeup.jariko</groupId>
        <artifactId>examples</artifactId>
        <version>develop-SNAPSHOT</version>
    </dependency>

### Gradle
Here are the configurationd to add to your build.gradle:
```
dependencies {
    ...
    implementation 'io.github.smeup:jariko:develop-SNAPSHOT'
}
```
### Samples
Here is a [sample project that uses the interpreter as a library](https://github.com/f-lombardo/rpgclient), and here is [another one](https://github.com/smeup/rpgweb).

## Contributing

Every kind of contribution to this project is really welcome. See our [contributing guide](CONTRIBUTING.md) for more details.

## Credits

The grammar used in this project is based on the work from [Ryan Eberly](https://www.linkedin.com/in/ryan-eberly-428b438/). It is derived from his project [rpgleparser](https://github.com/rpgleparser/rpgleparser).

Some RPG Examples are from [Claudio Neroni](https://www.neroni.it) 

Another source for good examples is [go4as400](http://www.go4as400.com)

