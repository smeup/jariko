# smeup-rpg

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/smeup/smeup-rpg.svg?branch=master)](https://travis-ci.org/smeup/smeup-rpg)
[![CircleCI](https://circleci.com/gh/smeup/smeup-rpg.svg?style=svg)](https://circleci.com/gh/smeup/smeup-rpg)
[![codebeat badge](https://codebeat.co/badges/92633ae2-5640-47b3-a0e7-b324f68288ac)](https://codebeat.co/projects/github-com-smeup-smeup-rpg-master)
[![](https://jitpack.io/v/smeup/smeup-rpg.svg)](https://jitpack.io/#smeup/smeup-rpg)

This project contains an interpreter for the [RPG programming language](https://en.wikipedia.org/wiki/IBM_RPG). This interpreter runs on the JVM. The interpreter is written in Kotlin.

## Development

All information for developers is contained in the [Development guide](docs/development.md).

## Logging

The project supports configurable logging, described in a [separate document](docs/logging.md).

## RPG Unit tests: the MUTE mechanism

We support special annotations to be used to write unit tests in RPG.
For details see [this document](docs/mute.md).

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
        <groupId>com.github.smeup.smeup-rpg</groupId>
        <artifactId>rpgJavaInterpreter-core</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>

And this if you want to include the examples too:
		
    <dependency>
        <groupId>com.github.smeup.smeup-rpg</groupId>
        <artifactId>examples</artifactId>
        <version>-SNAPSHOT</version>
    </dependency>

Side note for maven users who use mirrors: remember to change your .m2/settings.xml with settings like this:

    <mirrors>
        <mirror>
            <id>myNexus</id>
            <mirrorOf>!jitpack.io,*</mirrorOf>


## Credits

The grammar used in this project is based on the work from [Ryan Eberly](https://www.linkedin.com/in/ryan-eberly-428b438/). It is derived from his project [rpgleparser](https://github.com/rpgleparser/rpgleparser).

Some RPG Examples are from [Claudio Neroni](https://www.neroni.it) 

Another source for good examples is [go4as400](http://www.go4as400.com)

