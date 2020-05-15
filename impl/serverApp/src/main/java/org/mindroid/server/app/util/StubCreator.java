package org.mindroid.server.app.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.server.app.MindroidServerFrame;

import javax.lang.model.SourceVersion;
import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class StubCreator {
    private Logger LOGGER = LogManager.getLogger(MindroidServerFrame.class);

    private static StubCreator  ourInstance = new StubCreator();

    public static StubCreator getInstance() {
        return ourInstance;
    }

    private StubCreator(){

    }

    public void showDialog() {
        LOGGER.info("Showing Dialog to create new stub");
        JTextField className = new JTextField();
        JTextField progName = new JTextField();
        Integer[] sizes = {-1, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        JComboBox<Integer> sessionSize = new JComboBox<>(sizes);
        Object[] message = {"Klassenname", className,
                            "Programmname", progName,
                            "Sessiongröße", sessionSize};
        int n = JOptionPane.showOptionDialog(null, message, "title", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

        switch (n){
            case JOptionPane.CANCEL_OPTION:
                LOGGER.info("Canceled StubCreator Dialog");
                break;
            case JOptionPane.CLOSED_OPTION:
                LOGGER.info("Closed StubCreator Dialog");
                break;
            case JOptionPane.OK_OPTION:
                LOGGER.info("Chose to create Class in Dialog");
                LOGGER.info("Input from stub-dialog: " +
                        "Classname: [" + className.getText() + "] " +
                        "ProgName: [" + progName.getText() + "] " +
                        "SessionSize: [" + sizes[sessionSize.getSelectedIndex()] + "] ");
                String name = capitalize(className.getText());
                if (isValidName(name)){
                    try {
                        createStub(name, progName.getText(), sizes[sessionSize.getSelectedIndex()]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    LOGGER.error("[" + name + "] is not a valid identifier for a Java Class");
                }
        }
    }

    /**
     * Takes a String and capitalizes the first character
     * @param in the String whose first character shall be capitalized
     * @return the capitalized String
     */
    private String capitalize(String in){
        return in.substring(0,1).toUpperCase() + in.substring(1, in.length());
    }

    /**
     * Checks if the String is a valid name for a Java Class
     * @param className
     * @return
     */
    boolean isValidName (String className) {
        return SourceVersion.isIdentifier(className) && !SourceVersion.isKeyword(className);
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
                LOGGER.info("Created class: " + f.getCanonicalPath());
            }
        } else {
            LOGGER.error("Classname already in use");
        }
    }
}
