javac -sourcepath src -d bin -encoding utf-8 src/role/data/*.java src/role/main/*.java
pause
jar -cvfm RPG.jar META-INF/MANIFEST.MF src res -C bin .
pause