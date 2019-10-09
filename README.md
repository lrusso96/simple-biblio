# simple-biblio

[![Build Status](https://travis-ci.com/lrusso96/simple-biblio.svg?branch=master)](https://travis-ci.com/lrusso96/simple-biblio)
[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)

[![Maintainability](https://api.codeclimate.com/v1/badges/0948dd3574e676cc3627/maintainability)](https://codeclimate.com/github/lrusso96/simple-biblio/maintainability)
[![Test Coverage](https://api.codeclimate.com/v1/badges/0948dd3574e676cc3627/test_coverage)](https://codeclimate.com/github/lrusso96/simple-biblio/test_coverage)

A simple Java library to search for books.

## Examples

    SimpleBiblio biblio = new SimpleBiblioBuilder().build();
    List<Ebook> ret = biblio.searchAll("Carroll");
    
 ## Dependencies
 - [Square OkHttp](https://github.com/square/okhttp)
 - [JUnit](https://github.com/junit-team/junit4)
 - [jsoup](https://jsoup.org/)
 - [org/Json](https://github.com/stleary/JSON-java)
 - [Apache Commons Lang](https://commons.apache.org/proper/commons-lang/)