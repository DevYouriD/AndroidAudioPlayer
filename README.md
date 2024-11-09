# Android Audio Player
A simple media player for audio files on your android device.

## About
I started this project because I was using a different application to play audio files but got 
prompted to log in on every startup. I also did not like the layout and found the way to create 
playlists very unintuitive.

Through this app I tried to make these things as easy as possible. There is one designated 
directory on the android device where the user can store their audio files, and the application 
will scan this directory, detect the files and display them according to how the user likes.

## TODO
- [ ] Audio Player
  - [x] Create media player page
  - [x] Create Song selection page
  - [x] Figure out how to access files on device
  - [x] Figure out how to access file metadata
  - [x] Figure out MediaPlayer logic
- [ ] Permissions
  - [x] Implement permission logic
  - [ ] Change permission logic to trigger when app is opened for the first time (not on button click)
- [ ] Design
  - [ ] Implement custom app icon
- [ ] Other
  - [x] Handle navigation between pages
  - [ ] Clean up code
    - [ ] Implement code quality plugins (e.g. checkstyle, pmd, spotbugs)
    - [ ] Scan project with sonarlint/ sonarqube
- [ ] Post-mvp idea's
  - [ ] Custom playlist functionality
  - [ ] Widget support
  - [ ] Control via bluetooth device support
  - [ ] Add image support (user uploads their own image, gets saved on device and relationship between
    image and file is saved in json file)

## Learning Goals
- Permission management
- Media Player usage

## Tools/ Technologies used
- Kotlin
- Gradle build tool
- Trello
- GitHub
- [PlantUML](https://plantuml.com/en-dark/)
- Junit

## Example Project Structure
```text
src
├── main
│   ├── java
│   │   └── com.example.musicplayer
│   │       ├── ui
│   │       │   ├── activity
│   │       │   ├── fragment
│   │       │   └── compose
│   │       ├── data
│   │       │   ├── model
│   │       │   ├── repository
│   │       │   └── local
│   │       ├── network
│   │       ├── di
│   │       ├── util
│   │       ├── service
│   │       └── ChatApp.kt
│   ├── res
│   │   ├── layout
│   │   ├── values
│   │   ├── drawable
│   │   └── mipmap
│   ├── assets
│   ├── AndroidManifest.xml
│   └── kotlin
└── test
    └── java
        └── com.example.musicplayer
            ├── ui
            ├── data
            └── ChatAppTests.kt
```

<br>

![Music Cat](https://i.pinimg.com/736x/fd/e9/1c/fde91cd80af36bc4affbd5271bb4ad1b.jpg)
