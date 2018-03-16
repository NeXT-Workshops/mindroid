package org.mindroid.server.app;

import se.vidstige.jadb.JadbConnection;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class MindroidServerADBInfoFrame extends JFrame {

    private static final MindroidServerADBInfoFrame console = new MindroidServerADBInfoFrame();

    public static MindroidServerADBInfoFrame getMindroidServerADBInfoFrame() {
        return console;
    }

    private final String TITLE =  "Connected ADB Devices";
    private JPanel contentPane = new JPanel();

    private MindroidServerADBInfoFrame() {
        setTitle(TITLE);
        setSize(new Dimension(300,400));

        initMenubar();
        initPane();
    }

    private void initMenubar() {
        //Menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');
        //JMenu helpMenu = new JMenu("Help");
        JMenuItem refreshDevices;

        JMenuItem exitMenuItem = new JMenuItem();
        exitMenuItem.setAction(new AbstractAction("Quit") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        exitMenuItem.setMnemonic('q');

        refreshDevices = new JMenuItem();
        refreshDevices.setAction(new AbstractAction("Refresh Devices") {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDevices();
            }
        });
        refreshDevices.setMnemonic('r');

        fileMenu.add(refreshDevices);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
    }

    private void initPane(){

        contentPane.setLayout(new GridLayout(20,1));
        setContentPane(new JScrollPane(contentPane));
    }


    public void updateDevices() {
        try {
            String[] devices = getDevices();
            clearContentPane();

            for (int i = 0; i < devices.length; i++) {
                JTextField txtField = new JTextField(devices[i]);
                contentPane.add(txtField);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JadbException e) {
            e.printStackTrace();
        }
    }

    private void clearContentPane(){
        contentPane.removeAll();
    }


    public String[] getDevices() throws IOException, JadbException {
        JadbConnection jadb = new JadbConnection();
        String[] devices_arr;

        List<JadbDevice> devices = jadb.getDevices();
        if (!devices.isEmpty()) {
            devices_arr = new String[devices.size()];
            for (int i = 0; i < devices.size(); i++) {
                StringBuffer sb = new StringBuffer();
                sb.append(devices.get(i).getSerial());
                devices_arr[i] = devices.get(i).getSerial();

            }
            return devices_arr;
        } else {
            return new String[]{""};
        }
    }

}