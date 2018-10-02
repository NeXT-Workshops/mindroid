package org.mindroid.server.app.language;

import org.mindroid.server.app.MindroidServerConsoleFrame;
import org.mindroid.server.app.MindroidServerSettings;

import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

//TODO Fix missing bundle ressource
public class Language {

    public static final Locale GERMAN = Locale.GERMANY;
    public static final Locale ENGLISH = Locale.US;

    private static final String BASENAME = "StringsBundle";

    private static Locale currentLocale = Locale.ENGLISH;

    private Language() {
        setDefaultLocale();
    }

    public static void setLocale(Locale locale){
        currentLocale=locale;
    }

    private static void setDefaultLocale(){
        currentLocale = Locale.getDefault();
    }

    private static ResourceBundle getResources(Locale locale){
        try {
            return ResourceBundle.getBundle(BASENAME,currentLocale,Language.class.getClassLoader());
        }catch(MissingResourceException mre){
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine(mre.toString());
            MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Loaded Default Language (English)");
            return ResourceBundle.getBundle(BASENAME,Locale.getDefault(),Language.class.getClassLoader());
        }
    }

    public static String getString(String key){
        return getResources(currentLocale).getString(key);
    }
}
