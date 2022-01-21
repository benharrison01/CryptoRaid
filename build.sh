#!/bin/bash
cd src
srcdir=uob/cs/teamproject/sabrewulf
javac --class-path ../lib/\* -d ../build $srcdir/*.java $srcdir/*/*.java $srcdir/*/*/*.java
