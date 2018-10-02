package org.mindroid.server.app;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
        this.setMinimumSize(new Dimension(900,500));
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

        //Redirects the System.out and System.Err -Streams to the TextArea
        RedirectOutput.sendTo(textArea);
    }

    public void appendLine(String message) {
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());

        //System.out is redirected to textArea
        System.out.print(timeStamp + "   " + message + "\n");
    }


    /*
     *  Thanks to https://www.experts-exchange.com/questions/27184771/Java-output-to-a-textarea.html
     */
    public static class RedirectOutput {

        JTextComponent textComponent;

        public static void sendTo(JTextComponent textComponent) {
            new RedirectOutput(textComponent).redirectSystemStreams();
        }

        private RedirectOutput(JTextComponent textComponent) {
            this.textComponent = textComponent;
        }

        private void updateTextComponent(final String text) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Document doc = textComponent.getDocument();
                    try {
                        doc.insertString(doc.getLength(), text, null);
                    } catch (BadLocationException e) {
                        throw new RuntimeException(e);
                    }
                    textComponent.setCaretPosition(doc.getLength() - 1);
                }
            });
        }

        private void redirectSystemStreams() {
            OutputStream out = new OutputStream() {
                @Override
                public void write(final int b) throws IOException {
                    updateTextComponent(String.valueOf((char) b));
                }

                @Override
                public void write(byte[] b, int off, int len) throws IOException {
                    updateTextComponent(new String(b, off, len));
                }

                @Override
                public void write(byte[] b) throws IOException {
                    write(b, 0, b.length);
                }
            };

            System.setOut(new PrintStream(out, true));
            //System.setErr(new PrintStream(out, true)); Removed redirection for error debugging purpose at start
        }

    }

}
