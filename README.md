# Simple Audio Player
This application is meant as a simple tool to play your audio files on your android device.

I started this project because I was using a different application to play audio files 
but got prompted to log in on every startup. I also didn't like the layout and found the way to
create playlists was very unintuitive.

In this app I tried to make these things as easy as possible. There is one designated directory on
the android device where the user can store their audio files, and the application will scan this 
directory, detect the files and display them according to how the user likes.

## ToDo
- Figure out if sound files have metadata and how to access
- Implement custom playlist functionality
- Implement custom app icon
- Design
  - Draw design by hand
  - Create design in figma
  - 
- ~~Something that is done~~

## Notes
File storage:
Music Directory:
Path: /storage/emulated/0/Music/ or Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
This is the standard directory for storing music files. Most music apps and media players use this directory to store and retrieve music files.

