#!/bin/bash
cd build
java --module-path ../lib --add-modules javafx.base,javafx.controls,javafx.graphics,javafx.media -cp \. uob.cs.teamproject.sabrewulf.Game
