package org.mindroid.server.app;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Felicia Ruppel on 13.03.17.
 */
public class MindroidServerConsoleFrame extends JFrame {

    private static final MindroidServerConsoleFrame console = new MindroidServerConsoleFrame();
    private final JTextArea textArea;

    public static MindroidServerConsoleFrame getMindroidServerConsole() {
        return console;
    }

    private MindroidServerConsoleFrame(){
        super("Mindroid Server Console");
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setMinimumSize(new Dimension(800,500));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        final Image titleImage = MindroidServerSettings.getTitleImage();
        if (titleImage != null) {
            this.setIconImage(titleImage);
        }

        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        this.getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);

        JPanel southPanel = new JPanel(new BorderLayout());
        JButton clear = new JButton("Clear Console");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });

        southPanel.add(clear, BorderLayout.EAST);
        this.getContentPane().add(southPanel,BorderLayout.SOUTH);

    }

    public void appendLine(String message) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        textArea.append(timeStamp + "   " + message + "\n");
        try {
            textArea.setCaretPosition(textArea.getLineEndOffset(textArea.getLineCount()-1));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }


    }
}
