#!/bin/bash

JAVACC_HOME="/Users/seb/javacc-4.1/"
PROJECT_FOLDER="/Users/seb/Documents/eclipse/workspaces/uni/sproksch/de/f00sel/animal/formulaparser"

cd $PROJECT_FOLDER
${JAVACC_HOME}bin/jjtree FormulaParser.jjt

echo "============================================================================="

${JAVACC_HOME}bin/javacc FormulaParser.jj

echo "=======================================build.sh======================================"

javac *.java


