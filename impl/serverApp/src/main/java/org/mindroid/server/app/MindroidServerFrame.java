package org.mindroid.server.app;


import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;
import org.mindroid.server.app.util.UserManagement;
import org.mindroid.server.app.util.ManualADB;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


/**
 * @author Roland Kluge - Initial implementation
 */
public class MindroidServerFrame extends JFrame {

    private final JTable table;
    private final JLabel ownIPLabel;
    private final JLabel sessionStateLabel;
    private final JMenuItem refreshIP;
    private final JCheckBox activateScrollingCheckBox;
    private HashMap<String,Color> assignedColors;
    private ArrayList<Color> availableColors;
    private final int SOURCE_COL = 1;  //Column that contains a message's source
    private final int LEVEL_COL = 2;


    public MindroidServerFrame() {
        super("Mindroid Server Application");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(800,500));
        Image titleImage = MindroidServerSettings.getTitleImage();
        if (titleImage != null) {
            this.setIconImage(titleImage);
        }
        //Menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic('f');

        JMenuItem exitMenuItem = new JMenuItem();
        exitMenuItem.setAction(new AbstractAction("Quit") {
            @Override
            public void actionPerformed(final ActionEvent e) {
                System.exit(0);
            }
        });
        exitMenuItem.setMnemonic('q');
        exitMenuItem.setAccelerator(KeyStroke.getKeyStroke("control Q"));

        JMenuItem consoleMenuItem = new JMenuItem();
        consoleMenuItem.setAction(new AbstractAction("Show Console") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MindroidServerConsoleFrame console = MindroidServerConsoleFrame.getMindroidServerConsole();
                console.setVisible(!console.isVisible());
            }
        });
        consoleMenuItem.setMnemonic('c');
        consoleMenuItem.setAccelerator(KeyStroke.getKeyStroke("shift C"));

        JMenuItem connectADBMenuItem = new JMenuItem();
        connectADBMenuItem.setAction(new AbstractAction("Connect ADB") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ManualADB.getInstance().showDialog();
            }
        });
        connectADBMenuItem.setMnemonic('a');
        connectADBMenuItem.setAccelerator(KeyStroke.getKeyStroke("shift A"));



        refreshIP = new JMenuItem();
        refreshIP.setAction(new AbstractAction("Refresh IP Address") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MindroidServerApplicationMain.invokeDisplayIPAdress();
            }
        });
        refreshIP.setMnemonic('r');
        refreshIP.setAccelerator(KeyStroke.getKeyStroke("control R"));

        JMenuItem adbDevicesMenuItem = new JMenuItem();
        adbDevicesMenuItem.setAction(new AbstractAction("Show "+ConnectedDevicesFrame.TITLE) {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectedDevicesFrame adbDevicesFrame = ConnectedDevicesFrame.getInstance();
                adbDevicesFrame.setVisible(!adbDevicesFrame.isVisible());
            }
        });
        adbDevicesMenuItem.setMnemonic('d');
        adbDevicesMenuItem.setAccelerator(KeyStroke.getKeyStroke("control D"));

        JMenuItem restartServerMenuItem = new JMenuItem("Restart Server");
        restartServerMenuItem.setAction(new AbstractAction("Restart Server") {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    restartServer();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
        //restartServerMenuItem.setMnemonic('r');
        restartServerMenuItem.setAccelerator(KeyStroke.getKeyStroke("control N"));

        fileMenu.add(consoleMenuItem);
        fileMenu.add(adbDevicesMenuItem);
        fileMenu.add(connectADBMenuItem);
        fileMenu.add(refreshIP);
        fileMenu.add(restartServerMenuItem);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);

        //menuBar.add(helpMenu);


        this.setJMenuBar(menuBar);

        this.getContentPane().setLayout(new BorderLayout());

        //table at center
        String[] columnNames = {"Time", "RuntimeID", "Source", "Target", "Type", "Content"};
        DefaultTableModel model = new DefaultTableModel(columnNames,0);

        this.table = new JTable(model);
        JTextField tf = new JTextField();
        tf.setEditable(false);
        table.setDefaultEditor(Object.class, new DefaultCellEditor(tf));
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(60);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        table.getColumnModel().getColumn(4).setPreferredWidth(100);
        table.getColumnModel().getColumn(5).setPreferredWidth(200);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.getColumnModel().getColumn(0).setMinWidth(70);
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(2).setMaxWidth(120);
        table.getColumnModel().getColumn(3).setMaxWidth(120);
        table.getColumnModel().getColumn(4).setMaxWidth(100);

        table.getTableHeader().setReorderingAllowed(false);

        //colors each row depending on source
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus, int row, int col) {

                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                String source = (String)table.getModel().getValueAt(row, SOURCE_COL);
                if (!assignedColors.containsKey(source)) {
                    findColor(source);
                }
                    setBackground(assignedColors.get(source));
                    setForeground(table.getForeground());
                String loglevel = (String)table.getModel().getValueAt(row, LEVEL_COL);
                if (loglevel.equals("ERROR")) {
                    setBackground(Color.RED);
                    setForeground(table.getForeground());
                }

                return this;
            }
        });
        table.setColumnSelectionAllowed(false);
        table.setRowSelectionAllowed(false);

        final JScrollPane scrollPane = new JScrollPane(this.table);

        activateScrollingCheckBox = new JCheckBox("autoscrolling");
        activateScrollingCheckBox.setSelected(true);
        table.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(activateScrollingCheckBox.isSelected()) {
                    table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
                }
            }
        });
        this.getContentPane().add(activateScrollingCheckBox,BorderLayout.NORTH);
        scrollPane.setAutoscrolls(activateScrollingCheckBox.isSelected());
        this.getContentPane().add(scrollPane,BorderLayout.CENTER);

        //colors
        assignedColors = new HashMap<>();
        assignedColors.put("Local",Color.WHITE);
        availableColors = new ArrayList<>();

        //Bottom line
        JPanel southPanel = new JPanel(new BorderLayout());
        ownIPLabel = new JLabel();
        sessionStateLabel = new JLabel();
        //ownIPLabel.setEditable(false);
        southPanel.add(ownIPLabel,BorderLayout.WEST);
        southPanel.add(sessionStateLabel, BorderLayout.CENTER);

        JButton clear = new JButton("Clear Messages");
        clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) table.getModel();
                model.setRowCount(0);
            }
        });
        southPanel.add(clear, BorderLayout.EAST);
        this.getContentPane().add(southPanel,BorderLayout.SOUTH);

        this.pack();
        this.setVisible(true);
    }

    private void restartServer() throws IOException {
        //Kick all users
        //TODO Kick all users when restarting server, to notify them that the server cloesd
        // or send close message to all users, same effect.

        //start new server using startServer.bat cd ..\..\admin\Scripts
        Runtime.getRuntime().exec("cmd /c start \"\" ServerStarten.bat",null,new File("..\\..\\admin\\Scripts"));

        //Shutdown current server
        System.exit(0);
    }

    public void addLocalContentLine(String type, String content){
        addContentLine("Local", "-", type, content, "-");
    }

    public void addContentLineFromMessage(MindroidMessage mMsg) {
        String src = mMsg.getSource().getValue();
        String dest = mMsg.getDestination().getValue();
        String type = String.valueOf(mMsg.getMessageType());
        String content = mMsg.getContent();
        String sourceRuntimeID = String.valueOf(mMsg.getSessionRobotCount());

        if(!addContentLine(src, dest, type, content, sourceRuntimeID)){
            //Tries again n times, error was caused by 2 threads accessing the table at the same time
            int n = 10;
            int i = 0;
            while (!addContentLine(src, dest, type, content, sourceRuntimeID) && i < n) {
                i++;
            }
            if (i == n) {
                //retrying failed
                MindroidServerConsoleFrame.getMindroidServerConsole().appendLine("Adding Content to Log failed");
            }
        }
    }

    public boolean addContentLine(String source, String target, String type, String content, String runtimeID) {
        try {
            DefaultTableModel model = (DefaultTableModel) this.table.getModel();
            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            model.addRow(new String[]{timeStamp, runtimeID, source, target, type, content});
            return true;
        }catch(ArrayIndexOutOfBoundsException e){
            return false;
        }
    }


    public void displayIPAdress(String address, Color color) {
        ownIPLabel.setForeground(color);
        ownIPLabel.setText(" Server IP: " +address + "     Port: 33044");
    }

    public void displaySessionState(String text){
        sessionStateLabel.setForeground(Color.BLACK);
        sessionStateLabel.setText("\t" + text);
    }

    public void disableRefresh(boolean disable) {
        refreshIP.setEnabled(!disable);
    }

    public void findColor(String source) {
        if (availableColors.size()==0) {

            availableColors.add(Color.ORANGE);
            availableColors.add(Color.GREEN);
            availableColors.add(Color.YELLOW);
            availableColors.add(Color.CYAN);
            availableColors.add(Color.PINK);
            availableColors.add(Color.MAGENTA);
            availableColors.add(Color.LIGHT_GRAY);
            availableColors.add(Color.GRAY);
        }
        assignedColors.put(source,availableColors.get(0));
        availableColors.remove(0);
    }

    public void removeRegistration(String robotName) {
        UserManagement.getInstance().removeRegistration(robotName);
        /*
        Destination dest = new Destination(robotName);

        UserManagement.getIPMapping().remove(dest);
        UserManagement.getSocketMapping().remove(dest);

        ConnectedDevicesFrame.getInstance().updateDevices();
        */
    }
}
