#!/usr/bin/env bash
rm -rf bin
mkdir bin
${JAVA_HOME} -d bin -sourcepath src -cp libs/json-simple-1.1.1.jar src/by/bsu/up/chat/*.java