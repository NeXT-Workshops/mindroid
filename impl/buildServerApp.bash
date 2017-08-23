#!/bin/bash

cd $(dirname $0)
cd "./serverApp"
bash ../androidApp/gradlew clean test assemble
cd ..