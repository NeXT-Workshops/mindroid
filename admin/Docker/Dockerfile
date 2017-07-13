# Build me with
# docker build -t mindroid .
# Deploy me with
# docker tag mindroid echtzeitsysteme/mindroid
# docker login
# docker push echtzeitsysteme/mindroid
#
# Test procedure:
# docker run -i -t echtzeitsysteme/mindroid /bin/bash
# git clone https://github.com/Echtzeitsysteme/mindroid.git && cd mindroid && git checkout rkluge-dev && cd impl/androidApp && bash ./gradlew clean test assemble
#
FROM thyrlian/android-sdk:1.5
MAINTAINER Roland Kluge
# Just create an empty file 
RUN mkdir -p /root/.android
RUN touch /root/.android/repositories.cfg
RUN sdkmanager "platforms;android-25" "build-tools;25.0.2"
RUN sdkmanager --update