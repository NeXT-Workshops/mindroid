package org.mindroid.server.app;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerSettings {

    /**
     * Returns the default title image.
     *
     * <code>null</code> if a problem occurred
     */
    public static Image getTitleImage() {
        try {
            final InputStream imageStream = MindroidServerSettings.class.getClassLoader().getResourceAsStream("MindrobotWithTango.png");
            return ImageIO.read(imageStream);
        } catch (final IOException e) {
            System.err.println("Error loading title image: " + e.getMessage());
            return null;
        }
    }
}
