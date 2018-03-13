package org.mindroid.server.app.util;

import org.mindroid.server.app.MindroidServerConsoleFrame;

import java.io.IOException;

public class ShellService {

    public static void startADBServer() throws IOException {
        new ProcessBuilder("cmd.exe","cd %android_home/platform-tools/","adb start-server").start();
    }
}
