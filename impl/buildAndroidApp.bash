#!/bin/bash

cd $(dirname $0)
cd "./androidApp"
bash ./gradlew clean test assemble
cd ..