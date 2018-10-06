package org.mindroid.server.app.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.server.app.MindroidServerFrame;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StubCreator {
    private Logger l = LogManager.getLogger(MindroidServerFrame.class);

    private static StubCreator  ourInstance = new StubCreator();

    public static StubCreator getInstance() {
        return ourInstance;
    }

    private StubCreator(){

    }

    public void showDialog() {
        JTextField className = new JTextField();
        JTextField progName = new JTextField();
        Integer[] sizes = {-1, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JComboBox<Integer> sessionSize = new JComboBox<>(sizes);
        Object[] message = {"Klassenname", className,
                            "Programmname", progName,
                            "Sessiongröße", sessionSize};

        JOptionPane pane = new JOptionPane( message,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);
        pane.createDialog(null, "Klasse generieren").setVisible(true);
        try {

            createStub(capitalize(className.getText()), progName.getText(), sizes[sessionSize.getSelectedIndex()]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String capitalize(String in){
        return in.substring(0,1).toUpperCase() + in.substring(1, in.length());
    }

    private final String basePath = "..\\androidApp\\app\\src\\main\\java\\org\\mindroid\\android\\app\\programs\\workshop\\stubs\\";

    private String stub = "package org.mindroid.android.app.programs.workshop.stubs;\n" +
            "\n" +
            "import org.mindroid.api.ImperativeWorkshopAPI;\n" +
            "import org.mindroid.api.ev3.EV3StatusLightColor;\n" +
            "import org.mindroid.api.ev3.EV3StatusLightInterval;\n" +
            "import org.mindroid.common.messages.server.MindroidMessage;\n" +
            "import org.mindroid.impl.brick.Button;\n" +
            "import org.mindroid.impl.brick.Textsize;\n" +
            "\n" +
            "public class $CLASSNAME$ extends ImperativeWorkshopAPI {\n" +
            "\n" +
            "\tpublic $CLASSNAME$(){\n" +
            "\t\tsuper(\"$PROGNAME$\", $SESSIONSIZE$);\n" +
            "\t}\n" +
            "\n" +
            "\t@Override\n" +
            "\t\tpublic void run() {\n" +
            "\t}\n" +
            "}";

    private void createStub(String classname, String progname, int sessionSize) throws IOException {
        File f = new File(basePath + classname + ".java");
        FileWriter classfile = null;
        if (!f.exists()) {
            try {
                classfile = new FileWriter(f);
                classfile.write(stub.replace("$CLASSNAME$", classname).replace("$PROGNAME$", progname).replace("$SESSIONSIZE$", String.valueOf(sessionSize)));
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                assert classfile != null;
                classfile.flush();
                classfile.close();
            }
        } else {
            l.error("Classname already in use");
        }
    }
}
