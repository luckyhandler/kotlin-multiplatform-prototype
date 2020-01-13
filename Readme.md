# Kotlin Multiplatform Prototype

This project evaluates the use of Kotlin Multiplatform for Android and iOS. The main reason to use such an approach is to be able to share code such as entities, business logic but also the network connection and database storage.

## Architecture

The project uses [gradle][1] as build system. A gradle project consists of at least 2 modules: the project itself and at least one other module.

This project has the following structure:

```
project
  |
  |- android
  |
  |- shared
  |
  |- ios
``` 

Besides the project, there are 3 modules, which encapsulate special logic:

### shared
The `SharedCode` module is the library module, which contains code that can be propagated to Android and iOS. 
Within the module itself there are three folders:

```
...
  |- shared
      |
      |- androidMain
      |
      |- commonMain
      |
      |- iosMain
...
```

Kotlin Multiplatform works as follows: 

The *commonMain* package defines classes which can be used in the `android` or `ios` gradle modules. If there is a functionality which contains platform specific code the two packages *androidMain* & *iosMain* are used. In this case the *commonMain* package declares an `expect` placeholder and the platform-specific packages implement the `actual` logic.
This project uses this feature.  

### android
The `android` module contains the Android app. 
It simply uses the `shared` code module by implementing it via gradle:
```groovy
implementation project(':SharedCode')
```

### ios
The `ios` module contains the ios app and can only be opened in Xcode. The app consumes the data provided 
by the repository in a ViewModel and provides the data - once there - to the ui. The UI is built with Swift UI
and uses a declarative pattern. The `shared` module is implemented by the use of `cocoapods`.

## Features

The `shared` module shares 
- a network client ([Ktor][2]), 
- a SQLite database ([SQDelight][3]), 
- a Repository class which stores the network data in the database and only routes to the network if explicitly desired or if no data is found in the database.

To be able to work with asynchronous code it makes use of [Kotlin Coroutines][4].

## Setup

To get started you have to do the following things first:
- Build and setup `cocoapods` for ios
- Prepare the ios project to be able to use sqlite as SQDelight uses a sqlight database.

### cocoapods

The project uses the `cocoapods` gradle plugin to be able to easily integrate the `shared` code module into ios.
To generate the respective pod run 
```groovy
gradlew :SharedCode:podspec
```
before, then - from within the `ios` module - call `pod install` before building.

### Setup SQDelight

To be able to use the shared database in ios you have to enable sqlight support in Xcode.
Therefore,
 - Open Xcode
 - open the workspace `KotlinIOS`
 - Select the target `KotlinIOS`
 - Select the tab `Build Phases`
 - Under `Link Binary with Libraries` add `libsqlite3.tbd`

[1]: https://gradle.org/
[2]: https://ktor.io/
[3]: https://github.com/cashapp/sqldelight
[4]: https://kotlinlang.org/docs/reference/coroutines-overview.html