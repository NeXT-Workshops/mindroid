package org.mindroid.server.app;

import org.mindroid.common.messages.server.Destination;
import org.mindroid.server.app.util.ADBService;
import org.mindroid.server.app.util.IPService;
import se.vidstige.jadb.JadbDevice;
import se.vidstige.jadb.JadbException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ConnectedDevicesFrame extends JFrame implements ILogActionHandler{

    private static final ConnectedDevicesFrame console = new ConnectedDevicesFrame();

    public static ConnectedDevicesFrame getConnectedDeviceFrame() {
        return console;
    }

    public static final String TITLE =  "Connected Devices";
    private JPanel contentPane = new JPanel();

    private final int[] posX = {10,100,240,380,500};
    private final String[] columnNames = {"User","Devices", "ADB State", "Fetch Log", "Log"};


    private ConnectedDevicesFrame() {
        setTitle(TITLE);
        setSize(new Dimension(620,400));

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
    }

    private JTextField getTextField(String content){
        JTextField txtField = new JTextField(content);
        txtField.setEditable(false);
        txtField.setBorder(null);
        return txtField;
    }

    private JButton getFetchLogButton(String IP){
        JButton btn_fetchButton = new JButton("fetch log");
        return btn_fetchButton;
    }

    private JButton getLogButton(File file){
        JButton btn_opLogButton = new JButton("Open Log");
        return btn_opLogButton;
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

                JButton btn_fetchLog = getFetchLogButton(""); //TODO
                contentPane.add(btn_fetchLog);
                btn_fetchLog.setBounds(posX[3],posY,100,20);

                JButton btn_opLog = getLogButton(null); //TODO
                contentPane.add(btn_opLog);
                btn_opLog.setBounds(posX[4],posY,100,20);
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
                StringBuffer sb = new StringBuffer();
                sb.append(devices.get(i).getSerial());
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
}

