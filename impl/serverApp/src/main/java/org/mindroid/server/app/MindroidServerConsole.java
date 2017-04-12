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
public class MindroidServerConsole extends JFrame{

    private static final MindroidServerConsole console = new MindroidServerConsole();
    private final JTextArea textArea;

    public static MindroidServerConsole getMindroidServerConsole() {
        return console;
    }

    private MindroidServerConsole(){
        super("Error Console");
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.setMinimumSize(new Dimension(600,400));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
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
