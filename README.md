# simple-biblio

[![Build Status](https://travis-ci.com/lrusso96/simple-biblio.svg?branch=master)](https://travis-ci.com/lrusso96/simple-biblio)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

[![Maintainability](https://api.codeclimate.com/v1/badges/0948dd3574e676cc3627/maintainability)](https://codeclimate.com/github/lrusso96/simple-biblio/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/0948dd3574e676cc3627/test_coverage)](https://codeclimate.com/github/lrusso96/simple-biblio/test_coverage)

A simple Java library to search for books.

## Getting started

### Add to your project

#### Gradle
Add the library to dependencies

    dependencies {
         compile 'com.github.lrusso96:simple-biblio:0.2.0'
    }

#### Maven

    <dependency>
        <groupId>com.github.lrusso96</groupId>
        <artifactId>simple-biblio</artifactId>
        <version>0.2.0</version>
    </dependency>

## Examples

In order to set a logger you can do

    String my_package = "lrusso96.simplebiblio";
    Logger log = Logger.getLogger(my_package);

A simple search (with default options) can be run in this way

    SimpleBiblio biblio = new SimpleBiblioBuilder().build();
    List<Ebook> ret = biblio.searchAll("Carroll");

Otherwise, a more advanced (and custom) search is available

    // custom options for first provider
    Feedbooks feedbooks = new FeedbooksBuilder()
            .addLanguage(Language.ITALIAN)
            .build();

    // custom options for second provider
    LibraryGenesis libgen = new LibraryGenesisBuilder()
            .setSortingField(Field.TITLE)
            .setSortingMode(Sorting.ASCENDING)
            .build();

    // build a SimpleBiblio object and get the results
    biblio = new SimpleBiblioBuilder()
            .addProvider(feedbooks)
            .addProvider(libgen)
            .build();
    List<Ebook> ret = biblio.searchAll(query);

To get a direct download URI it's sufficient to invoke

    Ebook ebook = ...
    ebook.getDownload();

## Dependencies
 - [Square OkHttp](https://github.com/square/okhttp)
 - [JUnit](https://github.com/junit-team/junit4)
 - [jsoup](https://jsoup.org/)
 - [org/Json](https://github.com/stleary/JSON-java)
 - [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)
 - [JaCoCo](https://www.jacoco.org)
 
 ## Contributing
 
 1. clone the repository
 2. apply changes
 3. build and test before opening a PR with
 
```
./gradlew build
./gradlew test [jacocoTestReport]
```