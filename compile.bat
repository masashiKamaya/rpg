javac -d bin -encoding utf-8 -sourcepath src src/role/main/*.java src/role/data/*.java
pause
java -cp .;bin; role.main.App