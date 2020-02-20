#!/bin/bash

JAVACC_HOME="/Users/roessling/Documents/Java/javacc-5.0"
PROJECT_FOLDER="/Users/roessling/Documents/workspace/Animal/algoanim/executors/formulaparser"

cd $PROJECT_FOLDER
${JAVACC_HOME}bin/jjtree FormulaParser.jjt

echo "============================================================================="

${JAVACC_HOME}bin/javacc FormulaParser.jj

echo "=======================================build.sh======================================"

javac *.java


