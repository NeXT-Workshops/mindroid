# mindroid
This repository holds resources of the Mindroid project

[![Run Status](https://api.shippable.com/projects/58eeab1b957294070031bf9b/badge?branch=master)](https://app.shippable.com/github/Echtzeitsysteme/mindroid) 
[![Project Stats](https://www.openhub.net/p/mindroid/widgets/project_thin_badge.gif)](https://www.openhub.net/p/mindroid)

# How to import the projects

1. **Setup**
 * Install IntelliJ IDEA Community Edition (https://www.jetbrains.com/idea/)
 * Install gradle https://gradle.org/install
 * Install Android SDK (https://developer.android.com/studio/index.html --> Get just the command line tools)
   * You will need all resources SDK version 25 (via *Android SDK Manager*)
2. Checkout the *mindroid2* repository
 * (currently) Switch to branch Refactored-V1
 * Create the file *impl/androidApp/local.properties* (e.g., using the provided sample file local.properties.sample)
   * Adjust sdk.dir to match the root of your Android SDK. You need to escape ':' and '\' (e.g., X\:\\AndroidSDK)
3. Open IntelliJ, import the projects impl/ev3App
 * Make sure to choose *Gradle*
 * Make sure that your JAVA_HOME points to the root of your JDK (NOT the JRE within)
 * After confirming with *Finish*, a dialog will pop up ("Gradle Data to Import") that asks you which submodules to import. Choose all of them.
4. Import the project impl/androidApp (via *File -> New... -> Project from existing source*)
 * All other steps are the same as for ev3App
 
# Troubleshooting Gradle
If you experience problems in a gradle script, e.g., while loading a project from existing sources, follow the subsequent steps:
1. Open the Gradle tool window (*View -> Tool Windows -> Gradle*)
2. Open the Gradle file that you have changed (e.g., *build.gradle*)
3. Press *Refresh* (circular blue arrow) in the Gradle window.
4. Wait for the synchronization process to finish and watch the Messages window for errors and warnings.
