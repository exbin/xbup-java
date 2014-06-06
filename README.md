XBUP: eXtensible Binary Universal Protocol
==========================================

The XBUP Project aims to design and to provide an open-source support for unified general binary data representation format.

This should provide following advantages:

 * Advanced Data Structures - Unified structure should allow to combine various types of data together
 * Efficiency - Optional compression and encryption on multiple levels should allow effective representation of binary data
 * Flexibility - General framework should provide data transformations/processing and compatibility issues solving capability
 * Comprehensibility - Catalog of data types, metadata, relations and abstraction should allow better understanding of data

Homepage: http://xbup.org
Project page: http://sourceforge.net/projects/xbup

Structure
---------

As the project is currently in alpha stage, repository contains complete resources for distribution package with following folders:

 * doc - Documentation + related presenations
 * gradle - Gradle wrapper
 * src - Sources related to building distribution packages
 * modules - Libraries and other
 * plugins - Catalog plugins
 * resources - Related resource files, like sample files, images, etc.
 * tools - Distributable subprojects encapsulating modules to runnable applications

Compiling
---------

Java Development Kit (JDK) version 1.7 or later is required to build this project.

For project compiling Gradle 1.11 build system is used. You can either download and install gradle and run "gradle build" command in project folder or gradlew or gradlew.bat scripts to download separate copy of gradle to perform the project build.

Build system website: http://gradle.org

Development
-----------

The Gradle build system provides support for various IDEs. See gradle website for more information.

 * NetBeans 8.0 or later

There is gradle support plugin, which can be used to some degree, but some projects need other handling as described in their readme files.

Gradle support plugin website: http://plugins.netbeans.org/plugin/44510/gradle-support

License
-------

Project uses various libraries with specific licenses and some tools are licensed with multiple licenses with exceptions for specific modules to cover license requirements for used libraries.

Main license is: GNU/LGPL (see gpl-3.0.txt AND lgpl-3.0.txt)
License for documentation: GNU/FDL (see doc/fdl-1.3.txt)
