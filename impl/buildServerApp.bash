#!/bin/bash

cd $(dirname $0)
cd "./serverApp"
bash gradlew clean test assemble
cd ..