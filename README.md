# :sparkles: Credentiam

[![Build Status](https://travis-ci.org/YoshinoriN/Credentiam.svg?branch=master)](https://travis-ci.org/YoshinoriN/Credentiam) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/7099cb31b4fb413c9bd2bcf1517d6c16)](https://www.codacy.com/app/YoshinoriN/Credentiam?utm_source=github.com&utm_medium=referral&utm_content=YoshinoriN/Credentiam&utm_campaign=badger) [![Known Vulnerabilities](https://snyk.io/test/github/YoshinoriN/Credentiam/badge.svg?targetFile=src/build.sbt)](https://snyk.io/test/github/YoshinoriN/Credentiam)



**This project is under construction.**

ActiveDirectory search application. Powerd by Scala & Play Framework.

# Features

* View LDAP domains and each domain information.
* View LDAP organizations and each organaization information.
* View LDAP users and each user information.
* View LDAP computers and each computer information.
* Search object (user, computer, organization)
* Easy to change search target attributes of LDAP of each objects (Organization, user, computer)

Maybe can use by OpenLDAP but some contents aren't display.

# Images

||||
|:---:|:---:|:---:|
|![](https://raw.githubusercontent.com/YoshinoriN/Credentiam/master/doc/images/image1.png)|![](https://raw.githubusercontent.com/YoshinoriN/Credentiam/master/doc/images/image2.png)|![](https://raw.githubusercontent.com/YoshinoriN/Credentiam/master/doc/images/image3.png)|
|![](https://raw.githubusercontent.com/YoshinoriN/Credentiam/master/doc/images/image4.png)|![](https://raw.githubusercontent.com/YoshinoriN/Credentiam/master/doc/images/image5.png)|![](https://raw.githubusercontent.com/YoshinoriN/Credentiam/master/doc/images/image6.png)|
|![](https://raw.githubusercontent.com/YoshinoriN/Credentiam/master/doc/images/image7.png)|-|-|

# Requirements

These requierments follow with [play framework](//www.playframework.com/documentation/2.6.x/Installing).

* sbt
* Java 1.8

# Installation

1. Download release from [release page](https://github.com/YoshinoriN/Credentiam/releases).
2. Extract zip.
3. Please change [settings](https://github.com/YoshinoriN/Credentiam/#settings).
4. Do below commands.

```
sh ./bin/credentiam -Dconfig.file=./conf/application.conf
```

# Settings

Please change settings below document. Configuration files are stored in `conf` directory. 

## LDAP Settings

LDAP config file is `ldap.conf`. Basically you have to change connection settings to your ldap server. Each value's explanation are write in `ldap.conf`.

## Play Settings

Please change `play.http.secret.key` in `application.conf` to any value you like.

# Supported Language

* English
* Japanese

# Supported OS

Currently production mode is supported only work on linux.
But, on Windows work when developer mode. So, I'm wanna production mode on Windows.

# For developer

## Packaging

### Universal

run `sbt universal:packageBin` in `src` directory.
