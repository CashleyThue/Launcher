# Launcher
launches apps :3\
uses Java 25\
it opens up a menu and you can type in the query, it uses fuzzy search to get the app to launch\

## Installation
### Run from .java file (With the menu thing)
```bash
java <path to Swing.java>
```

### Run from .java file (Without the menu thing)
```bash
java <path to Main.java> <query>
```

### Compile to jar
from Launcher:
```bash
javac -cp "lib/*" -d out src/*.java
jar -cfm Launcher.jar manifest.txt -C out .
```
Use ```java -jar Launcher.jar <query>``` to run

## Config files
config files are stored at ```~/.config/cashlaunch/config.json```
put your custom directories there and it will scan those directories for .desktop files as well
there is no command to do this for you yet
