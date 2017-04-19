package org.mindroid.server.app;

import org.mindroid.common.messages.server.MindroidMessage;
import org.mindroid.common.messages.server.RobotId;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.InetSocketAddress;
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
    private final JMenuItem refreshIP;
    private HashMap<String,Color> assignedColors;
    private ArrayList<Color> availableColors;
    private final int SOURCE_COL = 1;  //Column that contains a message's source
    private final int LEVEL_COL = 2;
    private HashMap<RobotId, InetSocketAddress> ipMapping;


    public MindroidServerFrame() {
        super("Mindroid Server Application");
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(400,400));
        //Menubar
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem exitMenuItem = new JMenuItem();
        exitMenuItem.setAction(new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Close");
                System.exit(0);
            }
        });

        JMenuItem consoleMenuItem = new JMenuItem();
        consoleMenuItem.setAction(new AbstractAction("Show Console") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MindroidServerConsole console = MindroidServerConsole.getMindroidServerConsole();
                console.setVisible(true);
            }
        });

        refreshIP = new JMenuItem();
        refreshIP.setAction(new AbstractAction("Refresh IP Address") {
            @Override
            public void actionPerformed(ActionEvent e) {
                MindroidServerApplicationMain.invokeDisplayIPAdress();
            }
        });

        fileMenu.setMnemonic('f');
        exitMenuItem.setMnemonic('x');
        fileMenu.setMnemonic('h');

        fileMenu.add(consoleMenuItem);
        fileMenu.add(refreshIP);
        fileMenu.add(exitMenuItem);
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        this.setJMenuBar(menuBar);

        this.getContentPane().setLayout(new BorderLayout());

        //table at center
        String[] columnNames = {"Time","Source","LogLevel","Content"};
        DefaultTableModel model = new DefaultTableModel(columnNames,0);

        this.table = new JTable(model);
        JTextField tf = new JTextField();
        tf.setEditable(false);
        table.setDefaultEditor(Object.class, new DefaultCellEditor(tf));
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        table.getColumnModel().getColumn(0).setPreferredWidth(60);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(100);
        table.getColumnModel().getColumn(0).setMaxWidth(70);
        table.getColumnModel().getColumn(0).setMinWidth(70);
        table.getColumnModel().getColumn(1).setMaxWidth(120);
        table.getColumnModel().getColumn(2).setMaxWidth(100);

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
        this.getContentPane().add(new JScrollPane(this.table), BorderLayout.CENTER);

        //colors
        assignedColors = new HashMap<>();
        assignedColors.put("Local",Color.WHITE);
        availableColors = new ArrayList<>();

        //Bottom line
        JPanel southPanel = new JPanel(new BorderLayout());
        ownIPLabel = new JLabel();
        //ownIPLabel.setEditable(false);
        southPanel.add(ownIPLabel,BorderLayout.WEST);

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
        this.ipMapping = new HashMap<>();

    }


    public void addContentLine(MindroidMessage deseriaLogMessage) {
        try {
            DefaultTableModel model = (DefaultTableModel) this.table.getModel();
            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            model.addRow(new String[]{timeStamp, deseriaLogMessage.getSource().getValue(),
                    deseriaLogMessage.getMessageType().toString(), deseriaLogMessage.getContent()});
        } catch (ArrayIndexOutOfBoundsException e) {
            //Tries again n times, error was caused by 2 threads accessing the table at the same time
            int n = 10;
            int i=0;
            while (!retryAddContentLine(deseriaLogMessage) && i<n) {
                i++;

            }
            if (i==n) {
                //retrying failed
                throw e;
            }

             }
    }

    private boolean retryAddContentLine(MindroidMessage deseriaLogMessage) {
        try {
            DefaultTableModel model = (DefaultTableModel) this.table.getModel();
            String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
            model.addRow(new String[]{timeStamp, deseriaLogMessage.getSource().getValue(),
                    deseriaLogMessage.getMessageType().toString(), deseriaLogMessage.getContent()});
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

    }

    public void addContentLine(String source, String logLevel, String content ) {
        DefaultTableModel model = (DefaultTableModel) this.table.getModel();
        String timeStamp = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
        model.addRow(new String[] {timeStamp,source,logLevel,content});
    }

    public void displayIPAdress(String address, Color color) {
            ownIPLabel.setForeground(color);
            ownIPLabel.setText(" Server IP: " +address + "     Port: 33044");
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

    public void register(RobotId robotId, InetSocketAddress address) {
        ipMapping.put(robotId,address);
    }

    public InetSocketAddress findAddress(RobotId robotId) {
        return ipMapping.get(robotId);
    }
}
