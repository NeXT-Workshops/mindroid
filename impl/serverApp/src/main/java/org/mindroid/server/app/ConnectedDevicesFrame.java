package org.mindroid.server.app;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.common.messages.server.MindroidLogMessage;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.server.app.log.LogFetcher;
import org.mindroid.server.app.log.LogHandler;
import org.mindroid.server.app.util.ADBService;
import org.mindroid.server.app.util.IPService;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

public class ConnectedDevicesFrame extends JFrame implements ILogActionHandler{

    private static final ConnectedDevicesFrame console = new ConnectedDevicesFrame();
    private ArrayList<IUserAction> userActionListeners = new ArrayList<IUserAction>();

    public static ConnectedDevicesFrame getInstance() {
        return console;
    }

    public static final String TITLE =  "Connected Devices";
    private JPanel contentPane = new JPanel();

    private final int[] posX = {10,100,240,380,500,620};
    private final String[] columnNames = {"User","Devices", "ADB State", "Fetch Log", "Log", "Remove User"};


    private ConnectedDevicesFrame() {
        setTitle(TITLE);
        setSize(new Dimension(800,400));

        setIconImage(MindroidServerSettings.getTitleImage());

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
                disposeFrame();
            }
        });
        exitMenuItem.setMnemonic('q');
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));

        refreshDevices = new JMenuItem();
        refreshDevices.setAction(new AbstractAction("Refresh Devices") {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDevices();
            }
        });
        refreshDevices.setMnemonic('r');
        refreshDevices.setAccelerator(KeyStroke.getKeyStroke("control R"));

        JMenuItem openLogDir = new JMenuItem();
        openLogDir.setAction(new AbstractAction("Open Log Directory") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop.getDesktop().open(LogHandler.getLogDir());
                } catch (IOException e1) {
                    MindroidServerConsoleFrame.getMindroidServerConsole().appendLine(e1.getCause()+"\r\n"+e1.getMessage());
                }
            }
        });
        openLogDir.setMnemonic('o');
        openLogDir.setAccelerator(KeyStroke.getKeyStroke("control O"));

        fileMenu.add(refreshDevices);
        fileMenu.add(openLogDir);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        this.setJMenuBar(menuBar);
    }

    private void disposeFrame() {
        this.dispose();
    }

    private void initPane(){
        //contentPane.setLayout(new GridLayout(2,4));
        contentPane.setLayout(null);
        setContentPane(new JScrollPane(contentPane));

        createUIHeadline(columnNames);
    }

    private void createUIHeadline(String[] columnNames) {
        JTextField field_headLineUser = getTextField(columnNames[0]);
        contentPane.add(field_headLineUser);
        field_headLineUser.setBounds(posX[0],10,80,30);

        JTextField field_headlineDevice = getTextField(columnNames[1]);
        contentPane.add(field_headlineDevice);
        field_headlineDevice.setBounds(posX[1],10,120,30);

        JTextField field_headlineAdbState = getTextField(columnNames[2]);
        contentPane.add(field_headlineAdbState);
        field_headlineAdbState.setBounds(posX[2],10,120,30);

        JTextField field_headlineFetchLog = getTextField(columnNames[3]);
        contentPane.add(field_headlineFetchLog);
        field_headlineFetchLog.setBounds(posX[3],10,120,30);

        JTextField field_headlineLog = getTextField(columnNames[4]);
        contentPane.add(field_headlineLog);
        field_headlineLog.setBounds(posX[4],10,120,30);

        JTextField field_removeUser = getTextField(columnNames[5]);
        contentPane.add(field_removeUser);
        field_removeUser.setBounds(posX[5],10,120,30);
    }

    private JTextField getTextField(String content){
        JTextField txtField = new JTextField(content);
        txtField.setEditable(false);
        txtField.setBorder(null);
        return txtField;
    }

    private JButton getFetchLogButton(final String userName, final InetSocketAddress socketAddress){
        JButton btn_fetchButton = new JButton("fetch log");
        btn_fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MindroidLogMessage logMsg = LogFetcher.fetchLog(socketAddress);
                LogHandler.saveToFile(logMsg,LogHandler.getFilename(userName,socketAddress));
            }
        });
        return btn_fetchButton;
    }



    private JButton getLogButton(final String userName, final InetSocketAddress socketAddress) {
        JButton btn_opLogButton = new JButton("Open Log");
        btn_opLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (LogHandler.isExistent(LogHandler.getFilename(userName,socketAddress))) {
                        desktop.open(LogHandler.getLogFile(LogHandler.getFilename(userName,socketAddress)));
                    }
                } catch (IOException e1) {
                    MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Open Log failed: "+e1.getMessage());
                }
            }
        });
        return btn_opLogButton;
    }

    private JButton getRemoveUserButton(final String userName) {
        JButton btn_removeUser = new JButton("Remove User");
        btn_removeUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            for (IUserAction userActionListener : userActionListeners) {
                userActionListener.kickUser(userName);
            }
            }
        });
        return btn_removeUser;
    }

    public void updateDevices() {
        try {
            String[] devices = getDevices();
            clearContentPane();

            createUIHeadline(columnNames);
            int posY = 0;

            Destination[] destinations = IPService.getIPMapping().keySet().toArray(new Destination[IPService.getIPMapping().keySet().size()]);
            for (int i = 0; i < destinations.length; i++) {
                posY = 40+i*30;
                String ip = getIP(destinations[i]);

                JTextField field_userName = getTextField(destinations[i].getValue());
                contentPane.add(field_userName);
                field_userName.setBounds(posX[0],posY,80,30);

                JTextField field_deviceIp = getTextField(ip);
                contentPane.add(field_deviceIp);
                field_deviceIp.setBounds(posX[1],posY,120,30);

                String isConnected = getConnectionState(ip,devices);

                JTextField field_adbState = getTextField(isConnected);
                contentPane.add(field_adbState);
                field_adbState.setBounds(posX[2],posY,120,30);

                JButton btn_fetchLog = getFetchLogButton(destinations[i].getValue(),IPService.findAddress(destinations[i]));
                contentPane.add(btn_fetchLog);
                btn_fetchLog.setBounds(posX[3],posY,100,20);

                JButton btn_opLog = getLogButton(destinations[i].getValue(),IPService.findAddress(destinations[i]));
                contentPane.add(btn_opLog);
                btn_opLog.setBounds(posX[4],posY,100,20);

                JButton btn_removeUser = getRemoveUserButton(destinations[i].getValue());
                contentPane.add(btn_removeUser);
                btn_removeUser.setBounds(posX[5],posY,150,20);
            }

            contentPane.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JadbException e) {
            e.printStackTrace();
        }
    }

    private String getIP(Destination destination) {
        return IPService.getIPMapping().get(destination).getAddress().toString().replace("/","");
    }

    private String getConnectionState(String ip, String[] devices){

        for (int i = 0; i < devices.length; i++) {
            if(devices[i].contains(ip)){
                return "connected";
            }
        }
        return "not connected";
    }

    private void clearContentPane(){
        contentPane.removeAll();
    }


    public String[] getDevices() throws IOException, JadbException {
        String[] devices_arr;
        List<JadbDevice> devices = ADBService.getDevices();
        if (!devices.isEmpty()) {
            devices_arr = new String[devices.size()];
            for (int i = 0; i < devices.size(); i++) {
                devices_arr[i] = devices.get(i).getSerial();
            }
            return devices_arr;
        } else {
            return new String[]{""};
        }
    }

    @Override
    public void handleFetchLog(int rowIndex) {
        System.out.println("Handle Fetch LOG of "+rowIndex);
    }

    @Override
    public void handleShowLog(int rowIndex) {
        System.out.println("Handle Show LOG of "+rowIndex);
    }

    public void addUserListener(IUserAction userActionListener) {
        this.userActionListeners.add(userActionListener);
    }
}

