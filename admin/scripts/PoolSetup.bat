echo MINDROID POOL SETUP
echo .
echo Setting Paths

set ANDROID_HOME = C:\Users\Public\Mindroid\Android\
set GRADLE_USER_HOME = C:\Users\Public\Mindroid\Gradle\gradle-4.1\bin
set PATH=%PATH%;C:\Users\Public\Mindroid\Android\platform-tools;C:\Users\Public\Mindroid\GitPortable\bin

echo stashing git

cd mindroid 
git stash
git pull

echo copyAssemble.bat to JE dir

cp C:\Users\Public\Mindroid\mindroid\admin\Scripts\assemble.bat C:\Users\Public\Mindroid\JavaEditorPortable\assemble.bat