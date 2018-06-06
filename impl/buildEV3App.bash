#!/bin/bash

cd $(dirname $0)
cd "./ev3App"
bash gradlew clean test assemble
cd ..
