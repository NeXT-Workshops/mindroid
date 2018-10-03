package org.mindroid.server.app;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mindroid.common.messages.server.MindroidLogMessage;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.server.app.language.Language;
import org.mindroid.server.app.log.LogFetcher;
import org.mindroid.server.app.log.LogHandler;
import org.mindroid.server.app.util.ADBService;
import se.vidstige.jadb.ConnectionToRemoteDeviceException;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;

public class ConnectedDevicesFrame extends JFrame implements ILogActionHandler{

    private static final ConnectedDevicesFrame console = new ConnectedDevicesFrame();

    public static ConnectedDevicesFrame getInstance() {
        return console;
    }
    
    private JPanel contentPane = new JPanel();

    private final int[] posX = {10,100,240,380,500,620,770};
    private final String[] columnNames = {
            Language.getString("label_user"),
            Language.getString("label_devices"),
            Language.getString("label_adbState"),
            Language.getString("label_fetchLog"),
            Language.getString("label_log"),
            Language.getString("label_removeUser"),
            Language.getString("label_connectAdb")};

    private UserManagement um = UserManagement.getInstance();

    private Logger logger;

    private ConnectedDevicesFrame() {
        logger = LogManager.getLogger(ConnectedDevicesFrame.class);

        setTitle(Language.getString("frame_conncetedDevices_title"));
        setSize(new Dimension(980,400));

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
        exitMenuItem.setAction(new AbstractAction(Language.getString("menu_item_quit")) {
            @Override
            public void actionPerformed(final ActionEvent e) {
                disposeFrame();
            }
        });
        exitMenuItem.setMnemonic('q');
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));

        refreshDevices = new JMenuItem();
        refreshDevices.setAction(new AbstractAction(Language.getString("menu_item_refreshDevices")) {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateDevices();
            }
        });
        refreshDevices.setMnemonic('r');
        refreshDevices.setAccelerator(KeyStroke.getKeyStroke("control R"));

        JMenuItem openLogDir = new JMenuItem();
        openLogDir.setAction(new AbstractAction(Language.getString("menu_item_openLogDir")) {
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

        JTextField field_connectADB = getTextField(columnNames[6]);
        contentPane.add(field_connectADB);
        field_connectADB.setBounds(posX[6],10,120,30);
    }

    private JTextField getTextField(String content){
        JTextField txtField = new JTextField(content);
        txtField.setEditable(false);
        txtField.setBorder(null);
        return txtField;
    }

    private JButton getFetchLogButton(final String robotID, final InetSocketAddress socketAddress){
        JButton btn_fetchButton = new JButton(Language.getString("label_fetchLog"));
        btn_fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO,"Fetching log of "+robotID);
                MindroidLogMessage logMsg = LogFetcher.fetchLog(socketAddress);
                LogHandler.saveToFile(logMsg,LogHandler.getFilename(robotID,socketAddress));
            }
        });
        return btn_fetchButton;
    }



    private JButton getLogButton(final String robotID, final InetSocketAddress socketAddress) {
        JButton btn_opLogButton = new JButton(Language.getString("label_openLog"));
        btn_opLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO,"Open Log of "+robotID);
                try {
                    Desktop desktop = Desktop.getDesktop();
                    if (LogHandler.isExistent(LogHandler.getFilename(robotID,socketAddress))) {
                        desktop.open(LogHandler.getLogFile(LogHandler.getFilename(robotID,socketAddress)));
                    }
                } catch (IOException e1) {
                    MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Open Log failed: "+e1.getMessage());
                }
            }
        });
        return btn_opLogButton;
    }

    private JButton getRemoveUserButton(final RobotId robotID) {
        JButton btn_removeUser = new JButton(Language.getString("label_removeUser"));
        btn_removeUser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO,"Remove User "+ robotID + " by Button");
                um.removeUserAndCloseConnection(robotID);
            }
        });
        return btn_removeUser;
    }

    private JButton getConnectADBButton(final String robotID,final InetSocketAddress socketAddress){
        JButton btn_connectADB = new JButton(Language.getString("label_connectAdb"));
        btn_connectADB.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                logger.log(Level.INFO,"Remove User "+robotID);
                try {
                    ADBService.connectADB(socketAddress);
                } catch (IOException | JadbException | ConnectionToRemoteDeviceException e1) {
                    logger.log(Level.ERROR,e1.getMessage());
                }finally {
                    updateDevices();
                    //TODO may just update the JLabel of ADBConnection state
                }
            }
        });
        return btn_connectADB;
    }

    public void updateDevices() {
        String[] devices = getDevices();
        clearContentPane();

        createUIHeadline(columnNames);
        int posY = 0;

        RobotId[] robots = um.getRobotIdsArray();
        for (int i = 0; i < robots.length; i++) {
            posY = 40+i*30;
            String ip = um.getAddress(robots[i]).getHostString();

            JTextField field_userName = getTextField(robots[i].getValue());
            contentPane.add(field_userName);
            field_userName.setBounds(posX[0],posY,80,30);

            JTextField field_deviceIp = getTextField(ip);
            contentPane.add(field_deviceIp);
            field_deviceIp.setBounds(posX[1],posY,120,30);

            String isConnected = getConnectionState(ip,devices);

            JTextField field_adbState = getTextField(isConnected);
            contentPane.add(field_adbState);
            field_adbState.setBounds(posX[2],posY,120,30);

            JButton btn_fetchLog = getFetchLogButton(robots[i].getValue(),um.getAddress(robots[i]));
            contentPane.add(btn_fetchLog);
            btn_fetchLog.setBounds(posX[3],posY,100,20);

            JButton btn_opLog = getLogButton(robots[i].getValue(),um.getAddress(robots[i]));
            contentPane.add(btn_opLog);
            btn_opLog.setBounds(posX[4],posY,100,20);

            JButton btn_removeUser = getRemoveUserButton(robots[i]);
            contentPane.add(btn_removeUser);
            btn_removeUser.setBounds(posX[5],posY,120,20);

            JButton btn_connectADB = getConnectADBButton(robots[i].getValue(),um.getAddress(robots[i]));
            contentPane.add(btn_connectADB);
            btn_connectADB.setBounds(posX[6],posY,150,20);
        }

        contentPane.repaint();
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


    public String[] getDevices() {
        String[] devices_arr;
        List<JadbDevice> devices = ADBService.getDevices();
        if (devices != null && !devices.isEmpty()) {
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
        logger.log(Level.INFO,"Handle Fetch LOG of "+rowIndex);
    }

    @Override
    public void handleShowLog(int rowIndex) {
        logger.log(Level.INFO,"Handle Show LOG of "+rowIndex);
    }

}

