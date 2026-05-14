# Launcher
launches apps :3
uses Java 25
it opens up a menu and you can type in the query, it uses fuzzy search to get the app to launch
note that I added "Discord" hardcoded so if you try to launch it without having it installed through snap it will die :P

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
javac src/*.java
mv src/*.class .
jar -cfm Launcher.jar manifest.txt *.class
```
Use ```java -jar Launcher.jar <query>``` to run
