#!/bin/bash

cd $(dirname $0)
cd "./ev3App"
bash ../androidApp/gradlew clean test assemble
cd ..
