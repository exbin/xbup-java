eXtensible Binary Universal Protocol - Libraries
================================================

This repository contains Java implementation of the protocol parsers, catalog and service handling for eXtensible Binary Universal Protocol.

XBUP is binary data protocol and file format for communication, data storage and application interfaces. 

Homepage: https://xbup.exbin.org  

Structure
---------

As the project is currently in alpha stage, repository contains complete resources for distribution package with following folders:

  * modules - Libraries and other
  * doc - Documentation + related presentations
  * src - Sources related to building distribution packages
  * resources - Related resource files, like sample files, images, etc.
  * deps - Folder for downloading libraries for dependency resolution
  * gradle - Gradle wrapper

Compiling
---------

For project compiling Gradle 6.0 build system is used: https://gradle.org

You can either download and install gradle or use gradlew or gradlew.bat scripts to download separate copy of gradle to perform the project build.

Build commands: "gradle build" and "gradle distZip"

Currently it might be necessary to use local Maven - Manually download all dependencies from GitHub (clone repositories from github.com/exbin - see. deps directory for names) and run "gradle publish" on each of them.

License
-------

Project uses various libraries with specific licenses and some tools are licensed with multiple licenses with exceptions for specific modules to cover license requirements for used libraries.

License: Apache V2 (see LICENSE-2.0.txt)  
