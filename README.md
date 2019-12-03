# JaRIKo: a JAva virtual machine Rpg Interpreter written in KOtlin 

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/smeup/jariko.svg?branch=master)](https://travis-ci.org/smeup/jariko)
[![CircleCI](https://circleci.com/gh/smeup/jariko.svg?style=svg)](https://circleci.com/gh/smeup/jariko)
[![codebeat badge](https://codebeat.co/badges/220f492a-ca5b-4c88-aa11-c873b568a84b)](https://codebeat.co/projects/github-com-smeup-jariko-master)
[![jitpack](https://jitpack.io/v/smeup/jariko.svg)](https://jitpack.io/#smeup/jariko)
![GitHub commit activity](https://img.shields.io/github/commit-activity/m/smeup/jariko)

This project contains an interpreter for the [RPG programming language](https://en.wikipedia.org/wiki/IBM_RPG). This interpreter runs on the JVM since it's written in Kotlin.

## Development

All information for developers is contained in the [Development guide](docs/development.md).

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

## How to use this code in your project

At the moment, we use [Jitpack](https://jitpack.io/) to publish the [project](https://jitpack.io/#smeup/smeup-rpg).

If you use Maven, add these lines to your pom.xml in order to add the repository

    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
	
Then add the following dependencies for the core library:
	
    <dependency>
        <groupId>com.github.smeup.jariko</groupId>
        <artifactId>rpgJavaInterpreter-core</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>

And this if you want to include the examples too:
		
    <dependency>
        <groupId>com.github.smeup.jariko</groupId>
        <artifactId>examples</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>

Here is a [sample project that uses the interpreter as a library](https://github.com/f-lombardo/rpgclient), and here is [another one](https://github.com/smeup/rpgweb).

_Side note for maven users who use mirrors: remember to change your .m2/settings.xml with settings like this:_

    <mirrors>
        <mirror>
            <id>myNexus</id>
            <mirrorOf>!jitpack.io,*</mirrorOf>

## Contributing

Every kind of contribution to this project is really welcome. See our [contributing guide](CONTRIBUTING.md) for more details.

## Credits

The grammar used in this project is based on the work from [Ryan Eberly](https://www.linkedin.com/in/ryan-eberly-428b438/). It is derived from his project [rpgleparser](https://github.com/rpgleparser/rpgleparser).

Some RPG Examples are from [Claudio Neroni](https://www.neroni.it) 

Another source for good examples is [go4as400](http://www.go4as400.com)

