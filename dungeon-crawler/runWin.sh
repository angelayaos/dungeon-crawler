#!/bin/bash

# Compile & Run on Windows machines

cd ./src
javac -cp ".;../lib/*" -d "./bin" unsw/dungeon/*.java
echo "Compiled code"
echo "Running code"
java -cp "./bin;.;../lib/*" --module-path "../lib/;../bin;unsw/dungeon" --add-modules=javafx.controls,javafx.fxml  unsw.dungeon.DungeonApplication
cd ../src
