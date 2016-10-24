
mil-sym-android
=========
mil-sym-android is a android port of the java-based mil-std rendering libraries that have been used in US Army Mission Command software for years.  In November 2013 Mission Command was given the approval to release and maintain these libraries as public open source.  

[Android Renderer Developer's Guide Wiki](https://github.com/missioncommand/mil-sym-android/wiki)  
[Google Group Discussion Forum](https://groups.google.com/forum/#!forum/mission-command-milstd-renderer)  

MIL-STD-2525
-----------
The [MIL-STD-2525] standard defines how to visualize military symbology.  This project provides support for the entire MIL-STD-2525B Change II plus USAS 13-14 and MIL-STD-2525C.  

Project Structure
--------------
mil-sym-android has a namespace structure that resembles the java layout although differs where we needed to implement java functionality that wasn't available in the Dalvik VM


Tech
--------------

Eclipse with Android plugins
or
Android Studio
or
Gradle based build system
or
Maven based build system

Building
--------

Build, install to maven local
````
./gradlew install
````

Build, run all checks, javadoc and source jars, and install to maven local

````
./gradlew install -Pprofile=ci
````

Build, install and publish to maven central or a nexus repository
````
./gradlew install -Pprofile=javadoc,sources
./gradlew publishArtifacts -Pprofile=javadoc,sources
````

Build the site with javadocs and reports aggregation
````
./gradlew install -Pprofile=ci
./gradlew site
````
You can then view the site at {projectRoot}/build/site/index.html.