
package ptts.gui.paystation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ptts.entities.Administrator;
import ptts.entities.Cashier;
import ptts.entities.Passenger;
import ptts.entities.TransactionRecord;
import ptts.entities.managers.PassengerManager;
import ptts.entities.managers.TransactionRecordManager;
import ptts.gui.LogoutConfirm;
import static ptts.main.PublicTransportTicketingSystem.payStationController;

/**
 *
 * @author Siri@MnS
 */
public class PayStationTerminal extends javax.swing.JFrame {

    /**
     * Creates new form PayStationTerminal
     */
    //public static JFrame payStationTerminal = new PayStationTerminal();
    public static JFrame payStationTerminal;
    
    private Cashier currentCashier = null;
    
    private ArrayList<TransactionRecord> currentTransactionRecordList;
    
    //Transaction Records Table variables
    String[] transactionRecordsTableColumns = {"Date","Record ID","Cashier ID", "Passenger ID", "Desciption", "Credit", "Debit"};
    DefaultTableModel tableModel_transaction_records = new DefaultTableModel(transactionRecordsTableColumns, 0);
    TableRowSorter<TableModel> rowSorter_transaction_records = new TableRowSorter<>(tableModel_transaction_records);
    
    
    public PayStationTerminal() {
        initComponents();
        setLocationRelativeTo(null); //centering the JFrame in the computer display
        hide_panels();
        
        try {
            //this try bloack is to handle the currenCashier=NULL exception when calling the Terminal Selector from Admin Terminal
            currentCashier = payStationController.getCurrentCashier();
            populate_homescreen_panel();
            homescreen_panel.setVisible(true);
        } catch (NullPointerException e) {
            payStationTerminal = null;
            e.printStackTrace();
        }
        loadPassengerList();
    }
    
    //constructor to be used when an admin access Pay Station Terminal
    public PayStationTerminal(Administrator admin){
        //initComponents();
        Cashier tempCashier = new Cashier(admin.getUserID(), 3 , null, false, admin.getName(), admin.getGender(), null, null, null);
        this.currentCashier = tempCashier;
        initComponents();
        setLocationRelativeTo(null);
        hide_panels();
        button_homescreen_profile.setEnabled(false);
        button_homescreen_logout.setEnabled(false);
        label_homescreen_currentUsername.setText(currentCashier.getName());
        label_fixed_cashierID.setText("Admin login:  ");
        label_cashierID.setText(currentCashier.getUserID());
        homescreen_panel.setVisible(true);
        //this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void hide_panels(){
        homescreen_panel.setVisible(false);
        profile_edit_panel.setVisible(false);
        profile_panel.setVisible(false);
        new_ticket_panel.setVisible(false);
        account_topup_panel.setVisible(false);
        transaction_records_panel.setVisible(false);
        new_transaction_record_panel.setVisible(false);
    }
    
    
    
    private void populate_homescreen_panel(){
        label_homescreen_currentUsername.setText(currentCashier.getName());
        label_cashierID.setText(currentCashier.getUserID());
    }
    
    private void loadPassengerList(){
        ArrayList<Passenger> tempPassengerList = PassengerManager.getPassengerList();

        if (tempPassengerList != null && !tempPassengerList.isEmpty()) {
            String[] passengertList = new String[tempPassengerList.size()];
            for (int i = 0; i < tempPassengerList.size(); i++) {
                passengertList[i] = tempPassengerList.get(i).getUserID();
            }
            
            combo_account_topup_passengerID.setModel(new DefaultComboBoxModel(passengertList));
            combo_new_transrecord_passengerID.setModel(new DefaultComboBoxModel(passengertList));
            text_account_topup_amount.setEnabled(true);
            
        }
        else{
            String[] passengertList = {"Empty List"};
            combo_account_topup_passengerID.setModel(new DefaultComboBoxModel(passengertList));
            combo_new_transrecord_passengerID.setModel(new DefaultComboBoxModel(passengertList));

        }
    }
    
    private float getPassengerCreditBalance() {
        //throw new UnsupportedOperationException("Not supported yet.");
        float tempVal;
        String passengerID = null;
        try {
            passengerID = combo_account_topup_passengerID.getSelectedItem().toString();
        } catch (Exception e1) {
            tempVal = -1.0f;
            System.out.println("Selected Item cannot be converted to string!");
            e1.printStackTrace();
        }
        
        try {
            tempVal  = PassengerManager.getPassengerByID(passengerID).getCreditBalance();
            
        } catch (Exception e2) {
            tempVal = -2.0f;
            System.out.println("Cannot find a passenger with ID of" + passengerID);
            e2.printStackTrace();
        }
        
        return tempVal;
    }
    
    private boolean loadTransactionRecordList(){
        try {
            currentTransactionRecordList = TransactionRecordManager.getTransRecordList();
            label_transaction_records_count.setText(String.valueOf(currentTransactionRecordList.size()));
            return true;
            
        } catch (Exception e) {
            System.out.println("Pay station: Null TransRec List!");
            return false;
        }
    }
    
    private void loadTransactionRecordTableData(){

        table_transaction_records.setModel(tableModel_transaction_records);
        table_transaction_records.setRowSorter(rowSorter_transaction_records);
        
        if (loadTransactionRecordList()) {
            for (int i = 0; i < currentTransactionRecordList.size(); i++) {
                
                TransactionRecord tr = currentTransactionRecordList.get(i);
                
                String transID = tr.getTransID();
                String cashierID = tr.getCashierID();
                String passengerID = tr.getPassengerID();
                float amount = tr.getAmount();
                String description = tr.getDescription();
                int type = tr.getType();
                
                Calendar cal = tr.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String date = dateFormat.format(cal.getTime());
                
                String credit, debit;
                
                if (type == 0) {
                    credit = String.valueOf(amount);
                    debit = null;
                } else {
                    credit = null;
                    debit = String.valueOf(amount);
                }
                
                Object[] data = {date, transID, cashierID, passengerID, description, credit, debit};
                tableModel_transaction_records.addRow(data);
                
            }
        }
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        homescreen_panel = new javax.swing.JPanel();
        label_fixed_cashierID = new javax.swing.JLabel();
        label_cashierID = new javax.swing.JLabel();
        button_homescreen_logout = new javax.swing.JButton();
        button_homescreen_profile = new javax.swing.JButton();
        button_homescreen_guestTicket = new javax.swing.JButton();
        button_homescreen_accountTopUp = new javax.swing.JButton();
        button_homescreen_newTransRecord = new javax.swing.JButton();
        label_homescreen_currentUsername = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        button_homescreen_transactionRecords = new javax.swing.JButton();
        profile_panel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        panel_tokenQRCode1 = new javax.swing.JPanel();
        jButton8 = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        profile_edit_panel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        panel_tokenQRCode2 = new javax.swing.JPanel();
        jButton11 = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        new_ticket_panel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jButton14 = new javax.swing.JButton();
        button_buy_ticket_cancel = new javax.swing.JButton();
        combo_buy_ticket_toStation = new javax.swing.JComboBox();
        jLabel35 = new javax.swing.JLabel();
        combo_buy_ticket_class = new javax.swing.JComboBox();
        jLabel40 = new javax.swing.JLabel();
        combo_buy_ticket_type = new javax.swing.JComboBox();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        checkbox_buy_ticket_isBus = new javax.swing.JCheckBox();
        checkbox_buy_ticket_isTrain = new javax.swing.JCheckBox();
        checkbox_buy_ticket_isTaxi = new javax.swing.JCheckBox();
        jLabel44 = new javax.swing.JLabel();
        combo_buy_ticket_fromStation = new javax.swing.JComboBox();
        jLabel45 = new javax.swing.JLabel();
        checkbox_buy_ticket_isReturn = new javax.swing.JCheckBox();
        label_buy_ticket_ticketFee = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        button_buy_ticket_proceed = new javax.swing.JButton();
        dateChooserCombo_buy_ticket_startDate = new datechooser.beans.DateChooserCombo();
        dateChooserCombo_buy_ticket_endDate = new datechooser.beans.DateChooserCombo();
        button_buy_ticket_clear = new javax.swing.JButton();
        account_topup_panel = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        button_account_topup_back = new javax.swing.JButton();
        text_account_topup_amount = new javax.swing.JTextField();
        jLabel90 = new javax.swing.JLabel();
        button_account_topup_proceed = new javax.swing.JButton();
        jLabel87 = new javax.swing.JLabel();
        label_account_topup_balance = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        combo_account_topup_passengerID = new javax.swing.JComboBox();
        transaction_records_panel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_transaction_records = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        button_transaction_records_back = new javax.swing.JButton();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        jLabel106 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        jLabel138 = new javax.swing.JLabel();
        jLabel139 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        label_transaction_records_count = new javax.swing.JLabel();
        new_transaction_record_panel = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        button_new_transrecord_cancel = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();
        combo_new_transrecord_type = new javax.swing.JComboBox();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        text_new_transrecord_amount = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        button_new_transrecord_proceed = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea_new_transrecord_description = new javax.swing.JTextArea();
        combo_new_transrecord_passengerID = new javax.swing.JComboBox();
        checkbox_new_removePassengerID = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PTTS v1.0  Pay Station Terminal");
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homescreen_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        label_fixed_cashierID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_fixed_cashierID.setText("Cashier ID:");

        label_cashierID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_cashierID.setText("#cashierID");

        button_homescreen_logout.setText("Logout");
        button_homescreen_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_logoutActionPerformed(evt);
            }
        });

        button_homescreen_profile.setText("Profile");
        button_homescreen_profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_profileActionPerformed(evt);
            }
        });

        button_homescreen_guestTicket.setText("<html><center>Guest<br>Ticket</center>");
        button_homescreen_guestTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_guestTicketActionPerformed(evt);
            }
        });

        button_homescreen_accountTopUp.setText("<html><center>Account<br>Top-Up</center>");
        button_homescreen_accountTopUp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_accountTopUpActionPerformed(evt);
            }
        });

        button_homescreen_newTransRecord.setText("<html><center>New<br>Transaction<br>Record</center>");
        button_homescreen_newTransRecord.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_newTransRecordActionPerformed(evt);
            }
        });

        label_homescreen_currentUsername.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_homescreen_currentUsername.setText("Username");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel34.setText("Hi, ");

        button_homescreen_transactionRecords.setText("<html><center>Transaction<br>Records</center>");
        button_homescreen_transactionRecords.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_transactionRecordsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout homescreen_panelLayout = new javax.swing.GroupLayout(homescreen_panel);
        homescreen_panel.setLayout(homescreen_panelLayout);
        homescreen_panelLayout.setHorizontalGroup(
            homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homescreen_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(label_fixed_cashierID)
                                .addGap(18, 18, 18)
                                .addComponent(label_cashierID))
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_homescreen_currentUsername)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(button_homescreen_logout, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(button_homescreen_profile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addComponent(button_homescreen_guestTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(button_homescreen_accountTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(button_homescreen_newTransRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(button_homescreen_transactionRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 75, Short.MAX_VALUE)))
                .addContainerGap())
        );
        homescreen_panelLayout.setVerticalGroup(
            homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homescreen_panelLayout.createSequentialGroup()
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(label_homescreen_currentUsername))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_fixed_cashierID)
                            .addComponent(label_cashierID))
                        .addGap(48, 48, 48)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_homescreen_transactionRecords, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(button_homescreen_guestTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(button_homescreen_accountTopUp, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(button_homescreen_newTransRecord, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(button_homescreen_logout)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(button_homescreen_profile)))
                .addContainerGap(197, Short.MAX_VALUE))
        );

        getContentPane().add(homescreen_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        profile_panel.setEnabled(false);
        profile_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setText("Profile");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Name");

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel20.setText("userName");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Gender");

        jLabel22.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel22.setText("userGender");

        panel_tokenQRCode1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_tokenQRCode1.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout panel_tokenQRCode1Layout = new javax.swing.GroupLayout(panel_tokenQRCode1);
        panel_tokenQRCode1.setLayout(panel_tokenQRCode1Layout);
        panel_tokenQRCode1Layout.setHorizontalGroup(
            panel_tokenQRCode1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        panel_tokenQRCode1Layout.setVerticalGroup(
            panel_tokenQRCode1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );

        jButton8.setText("Edit");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Contact Number");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("userContactNumber");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("userEmail");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setText("Email");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setText("Address");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("userAddress");

        jButton10.setText("Back");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout profile_panelLayout = new javax.swing.GroupLayout(profile_panel);
        profile_panel.setLayout(profile_panelLayout);
        profile_panelLayout.setHorizontalGroup(
            profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profile_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(profile_panelLayout.createSequentialGroup()
                        .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel19)
                            .addComponent(jLabel21)
                            .addComponent(jLabel27)
                            .addComponent(jLabel30)
                            .addComponent(jLabel31))
                        .addGap(18, 18, 18)
                        .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel32))
                        .addGap(0, 250, Short.MAX_VALUE))
                    .addGroup(profile_panelLayout.createSequentialGroup()
                        .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(profile_panelLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8))
                            .addGroup(profile_panelLayout.createSequentialGroup()
                                .addComponent(panel_tokenQRCode1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton10)
                .addContainerGap())
        );
        profile_panelLayout.setVerticalGroup(
            profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jButton8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_tokenQRCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(jLabel22))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(jLabel29))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(jLabel31))
                .addGap(24, 24, 24)
                .addComponent(jButton10)
                .addContainerGap())
        );

        getContentPane().add(profile_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        profile_edit_panel.setEnabled(false);
        profile_edit_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel17.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel17.setText("Profile");

        jLabel23.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel23.setText("Name");

        jLabel25.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel25.setText("Gender");

        panel_tokenQRCode2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_tokenQRCode2.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout panel_tokenQRCode2Layout = new javax.swing.GroupLayout(panel_tokenQRCode2);
        panel_tokenQRCode2.setLayout(panel_tokenQRCode2Layout);
        panel_tokenQRCode2Layout.setHorizontalGroup(
            panel_tokenQRCode2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        panel_tokenQRCode2Layout.setVerticalGroup(
            panel_tokenQRCode2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jButton11.setText("Done");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setText("Contact Number");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setText("Email");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setText("Address");

        jButton12.setText("Change Picture");

        jButton13.setText("Cancel");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jTextField1.setText("userName");

        jTextField3.setText("userContactNumber");

        jTextField4.setText("userEmail");

        jTextField5.setText("userAddress");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        javax.swing.GroupLayout profile_edit_panelLayout = new javax.swing.GroupLayout(profile_edit_panel);
        profile_edit_panel.setLayout(profile_edit_panelLayout);
        profile_edit_panelLayout.setHorizontalGroup(
            profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profile_edit_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton11))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_edit_panelLayout.createSequentialGroup()
                        .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25)
                            .addComponent(jLabel33)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37))
                        .addGap(18, 18, 18)
                        .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField1)
                            .addComponent(jTextField3)
                            .addComponent(jTextField4)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                        .addComponent(jButton13))
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addComponent(panel_tokenQRCode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton12)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        profile_edit_panelLayout.setVerticalGroup(
            profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_edit_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jButton11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_tokenQRCode2, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addGap(0, 74, Short.MAX_VALUE)
                        .addComponent(jButton12)))
                .addGap(29, 29, 29)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton13)
                    .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(profile_edit_panelLayout.createSequentialGroup()
                            .addComponent(jLabel23)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel25)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel33)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel36)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel37))
                        .addGroup(profile_edit_panelLayout.createSequentialGroup()
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        getContentPane().add(profile_edit_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        new_ticket_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setText("Buy Ticket");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("To");

        jButton14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jButton14.setText("<html><center>Find<br>Fee</center>");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        button_buy_ticket_cancel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_buy_ticket_cancel.setText("Cancel");
        button_buy_ticket_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_buy_ticket_cancelActionPerformed(evt);
            }
        });

        combo_buy_ticket_toStation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gampaha", "Ragama", "Fort", "Panadura" }));

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setText("Class");

        combo_buy_ticket_class.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Standard", "Standard_AC", "First_Class", "Second_Class" }));

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel40.setText("Ticket Type");

        combo_buy_ticket_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Day_OffPeak", "Day_Peak", "Weekend", "Hourly", "WeekDays" }));

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setText("Start Date");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel42.setText("End Date");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setText("Prefered Method");

        checkbox_buy_ticket_isBus.setText("Bus");

        checkbox_buy_ticket_isTrain.setText("Train");

        checkbox_buy_ticket_isTaxi.setText("Taxi");

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel44.setText("From");

        combo_buy_ticket_fromStation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Gampaha", "Ragama", "Fort", "Panadura" }));

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setText("Return");

        checkbox_buy_ticket_isReturn.setText("Return");

        label_buy_ticket_ticketFee.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_buy_ticket_ticketFee.setText("00.00");

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel46.setText("Fee   LKR");

        button_buy_ticket_proceed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_buy_ticket_proceed.setText("Proceed");
        button_buy_ticket_proceed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_buy_ticket_proceedActionPerformed(evt);
            }
        });

        dateChooserCombo_buy_ticket_startDate.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
            public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
                dateChooserCombo_buy_ticket_startDateOnSelectionChange(evt);
            }
        });

        button_buy_ticket_clear.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_buy_ticket_clear.setText("Clear");
        button_buy_ticket_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_buy_ticket_clearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout new_ticket_panelLayout = new javax.swing.GroupLayout(new_ticket_panel);
        new_ticket_panel.setLayout(new_ticket_panelLayout);
        new_ticket_panelLayout.setHorizontalGroup(
            new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(new_ticket_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(new_ticket_panelLayout.createSequentialGroup()
                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41)
                            .addComponent(jLabel42)
                            .addComponent(jLabel26)
                            .addComponent(jLabel35)
                            .addComponent(jLabel44))
                        .addGap(18, 18, 18)
                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dateChooserCombo_buy_ticket_startDate, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(combo_buy_ticket_class, javax.swing.GroupLayout.Alignment.LEADING, 0, 1, Short.MAX_VALUE)
                            .addComponent(combo_buy_ticket_fromStation, javax.swing.GroupLayout.Alignment.LEADING, 0, 80, Short.MAX_VALUE)
                            .addComponent(combo_buy_ticket_toStation, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateChooserCombo_buy_ticket_endDate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(65, 65, 65)
                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                .addComponent(jLabel40)
                                .addGap(18, 18, 18)
                                .addComponent(combo_buy_ticket_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(button_buy_ticket_proceed)
                                    .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel45)
                                        .addComponent(checkbox_buy_ticket_isReturn)
                                        .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                            .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                                    .addComponent(checkbox_buy_ticket_isBus)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(checkbox_buy_ticket_isTrain))
                                                .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(checkbox_buy_ticket_isTaxi))))
                                .addGap(18, 18, 18)
                                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(button_buy_ticket_cancel)
                                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(new_ticket_panelLayout.createSequentialGroup()
                        .addComponent(jLabel46)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_buy_ticket_ticketFee))
                    .addComponent(jLabel18))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, new_ticket_panelLayout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addComponent(button_buy_ticket_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        new_ticket_panelLayout.setVerticalGroup(
            new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, new_ticket_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel18)
                .addGap(18, 18, 18)
                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(combo_buy_ticket_class, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(combo_buy_ticket_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(new_ticket_panelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel41)
                            .addComponent(dateChooserCombo_buy_ticket_startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                .addComponent(jLabel42)
                                .addGap(109, 109, 109)
                                .addComponent(button_buy_ticket_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(25, 25, 25)
                                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(button_buy_ticket_proceed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button_buy_ticket_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(33, 33, 33))
                            .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                .addComponent(dateChooserCombo_buy_ticket_endDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel45)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(checkbox_buy_ticket_isReturn))
                                    .addGroup(new_ticket_panelLayout.createSequentialGroup()
                                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(combo_buy_ticket_fromStation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel44))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(combo_buy_ticket_toStation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel26))))
                                .addGap(64, 64, 64)
                                .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(label_buy_ticket_ticketFee)
                                    .addComponent(jLabel46))
                                .addGap(51, 51, 51))))
                    .addGroup(new_ticket_panelLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(new_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkbox_buy_ticket_isBus)
                            .addComponent(checkbox_buy_ticket_isTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkbox_buy_ticket_isTaxi))
                        .addGap(28, 28, 28)
                        .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        getContentPane().add(new_ticket_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        account_topup_panel.setEnabled(false);
        account_topup_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel84.setText("Account Top-up");

        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel85.setText("Amount");

        button_account_topup_back.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        button_account_topup_back.setText("Back");
        button_account_topup_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_account_topup_backActionPerformed(evt);
            }
        });

        text_account_topup_amount.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        text_account_topup_amount.setEnabled(false);

        jLabel90.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel90.setText("LKR");

        button_account_topup_proceed.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        button_account_topup_proceed.setText("Proceed");
        button_account_topup_proceed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_account_topup_proceedActionPerformed(evt);
            }
        });

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel87.setText("Credit Balance");

        label_account_topup_balance.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        label_account_topup_balance.setText("#balance");

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel89.setText("LKR");

        jLabel91.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel91.setText("Passenger ID");

        combo_account_topup_passengerID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Passenger1", "Passenger2", "Passenger3" }));
        combo_account_topup_passengerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_account_topup_passengerIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout account_topup_panelLayout = new javax.swing.GroupLayout(account_topup_panel);
        account_topup_panel.setLayout(account_topup_panelLayout);
        account_topup_panelLayout.setHorizontalGroup(
            account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(account_topup_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(account_topup_panelLayout.createSequentialGroup()
                        .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_topup_panelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(button_account_topup_proceed, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(button_account_topup_back))
                            .addGroup(account_topup_panelLayout.createSequentialGroup()
                                .addComponent(jLabel84)
                                .addGap(0, 337, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(account_topup_panelLayout.createSequentialGroup()
                        .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(account_topup_panelLayout.createSequentialGroup()
                                .addComponent(jLabel91)
                                .addGap(18, 18, 18)
                                .addComponent(combo_account_topup_passengerID, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(account_topup_panelLayout.createSequentialGroup()
                                .addComponent(jLabel85)
                                .addGap(17, 17, 17)
                                .addComponent(jLabel90)
                                .addGap(18, 18, 18)
                                .addComponent(text_account_topup_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(account_topup_panelLayout.createSequentialGroup()
                                .addComponent(jLabel89)
                                .addGap(18, 18, 18)
                                .addComponent(label_account_topup_balance))
                            .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(47, 47, 47))))
        );
        account_topup_panelLayout.setVerticalGroup(
            account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_topup_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel84)
                .addGap(42, 42, 42)
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel91)
                        .addComponent(combo_account_topup_passengerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(account_topup_panelLayout.createSequentialGroup()
                        .addComponent(jLabel87)
                        .addGap(18, 18, 18)
                        .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_account_topup_balance)
                            .addComponent(jLabel89))))
                .addGap(39, 39, 39)
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(text_account_topup_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel90))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 131, Short.MAX_VALUE)
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_account_topup_back)
                    .addComponent(button_account_topup_proceed))
                .addContainerGap())
        );

        getContentPane().add(account_topup_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        transaction_records_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_transaction_records.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Trans ID", "Cashier ID", "Passenger ID", "Description", "Credit", "Debit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_transaction_records.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane4.setViewportView(table_transaction_records);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("#count");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText("Transaction Records");

        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel92.setText("From");

        jLabel93.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel93.setText("To");

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel97.setText("Search Results");

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel100.setText("Filter Results");

        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel101.setText("Date");

        jLabel102.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel102.setText("Amount LKR");

        jLabel103.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel103.setText("Min");

        jTextField21.setText("min");

        jTextField22.setText("max");

        jLabel104.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel104.setText("Max");

        jLabel105.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel105.setText("Type");

        button_transaction_records_back.setText("Back");
        button_transaction_records_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_transaction_records_backActionPerformed(evt);
            }
        });

        jCheckBox5.setText("Credit");

        jCheckBox6.setText("Debit");

        jLabel106.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel106.setText("PassengerID");

        jTextField23.setText("min");

        jTextField24.setText("max");

        jLabel138.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel138.setText("Cashier ID");

        jLabel139.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel139.setText("More");

        jLabel107.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel107.setText("Total records count");

        label_transaction_records_count.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        label_transaction_records_count.setText("#count");

        javax.swing.GroupLayout transaction_records_panelLayout = new javax.swing.GroupLayout(transaction_records_panel);
        transaction_records_panel.setLayout(transaction_records_panelLayout);
        transaction_records_panelLayout.setHorizontalGroup(
            transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(transaction_records_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                    .addGroup(transaction_records_panelLayout.createSequentialGroup()
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel101)
                            .addGroup(transaction_records_panelLayout.createSequentialGroup()
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel92)
                                    .addComponent(jLabel93))
                                .addGap(18, 18, 18)
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel102)
                            .addGroup(transaction_records_panelLayout.createSequentialGroup()
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel103)
                                    .addComponent(jLabel104))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField21)
                                    .addComponent(jTextField22, javax.swing.GroupLayout.DEFAULT_SIZE, 67, Short.MAX_VALUE))))
                        .addGap(18, 18, 18)
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel105)
                            .addComponent(jCheckBox5)
                            .addComponent(jCheckBox6))
                        .addGap(18, 18, 18)
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel139)
                            .addGroup(transaction_records_panelLayout.createSequentialGroup()
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel106)
                                    .addComponent(jLabel138))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField23, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField24, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(transaction_records_panelLayout.createSequentialGroup()
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel100)
                            .addGroup(transaction_records_panelLayout.createSequentialGroup()
                                .addComponent(jLabel97)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel9))
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transaction_records_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_transaction_records_back, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transaction_records_panelLayout.createSequentialGroup()
                                .addComponent(jLabel107)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_transaction_records_count)))))
                .addContainerGap())
        );
        transaction_records_panelLayout.setVerticalGroup(
            transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transaction_records_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel107)
                    .addComponent(label_transaction_records_count))
                .addGap(10, 10, 10)
                .addComponent(jLabel100)
                .addGap(8, 8, 8)
                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(transaction_records_panelLayout.createSequentialGroup()
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel102)
                            .addComponent(jLabel101)
                            .addComponent(jLabel105))
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, transaction_records_panelLayout.createSequentialGroup()
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(transaction_records_panelLayout.createSequentialGroup()
                                        .addGap(11, 11, 11)
                                        .addComponent(jLabel92)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, transaction_records_panelLayout.createSequentialGroup()
                                        .addGap(6, 6, 6)
                                        .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel93)
                                    .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20))
                            .addGroup(transaction_records_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox5)
                                    .addGroup(transaction_records_panelLayout.createSequentialGroup()
                                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel103)
                                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(jLabel104)
                                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jCheckBox6))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(transaction_records_panelLayout.createSequentialGroup()
                        .addComponent(jLabel139)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel106)
                            .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel138)
                            .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(transaction_records_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel97)
                    .addComponent(jLabel9))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_transaction_records_back)
                .addGap(5, 5, 5))
        );

        getContentPane().add(transaction_records_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        new_transaction_record_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel38.setText("New Transaction Record");

        button_new_transrecord_cancel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_new_transrecord_cancel.setText("Cancel");
        button_new_transrecord_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_new_transrecord_cancelActionPerformed(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setText("Type");

        combo_new_transrecord_type.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Credit", "Debit" }));

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setText("Passenger ID");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setText("Amount  LKR");

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setText("Description");

        button_new_transrecord_proceed.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_new_transrecord_proceed.setText("Proceed");
        button_new_transrecord_proceed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_new_transrecord_proceedActionPerformed(evt);
            }
        });

        textArea_new_transrecord_description.setColumns(20);
        textArea_new_transrecord_description.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        textArea_new_transrecord_description.setRows(5);
        jScrollPane1.setViewportView(textArea_new_transrecord_description);

        combo_new_transrecord_passengerID.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Credit", "Debit" }));

        checkbox_new_removePassengerID.setText("Remove Passenge ID");
        checkbox_new_removePassengerID.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                checkbox_new_removePassengerIDItemStateChanged(evt);
            }
        });
        checkbox_new_removePassengerID.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                checkbox_new_removePassengerIDStateChanged(evt);
            }
        });
        checkbox_new_removePassengerID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkbox_new_removePassengerIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout new_transaction_record_panelLayout = new javax.swing.GroupLayout(new_transaction_record_panel);
        new_transaction_record_panel.setLayout(new_transaction_record_panelLayout);
        new_transaction_record_panelLayout.setHorizontalGroup(
            new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                        .addComponent(jLabel38)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, new_transaction_record_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_new_transrecord_proceed)
                        .addGap(18, 18, 18)
                        .addComponent(button_new_transrecord_cancel)
                        .addGap(24, 24, 24))))
            .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel49)
                    .addComponent(jLabel50)
                    .addComponent(jLabel47)
                    .addComponent(jLabel52))
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(combo_new_transrecord_passengerID, 0, 91, Short.MAX_VALUE)
                            .addComponent(combo_new_transrecord_type, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(checkbox_new_removePassengerID)
                                .addComponent(text_new_transrecord_amount, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(0, 173, Short.MAX_VALUE))
        );
        new_transaction_record_panelLayout.setVerticalGroup(
            new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, new_transaction_record_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel38)
                .addGap(18, 18, 18)
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel47)
                    .addComponent(combo_new_transrecord_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel49)
                    .addComponent(combo_new_transrecord_passengerID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(checkbox_new_removePassengerID)
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jLabel50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, new_transaction_record_panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(text_new_transrecord_amount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addComponent(jLabel52))
                    .addGroup(new_transaction_record_panelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(29, 29, 29)
                .addGroup(new_transaction_record_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_new_transrecord_cancel)
                    .addComponent(button_new_transrecord_proceed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(35, 35, 35))
        );

        getContentPane().add(new_transaction_record_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_homescreen_profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_profileActionPerformed
        // TODO add your handling code here:
        hide_panels();
        profile_panel.setVisible(true);
    }//GEN-LAST:event_button_homescreen_profileActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        profile_panel.setVisible(false);
        profile_edit_panel.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        profile_panel.setVisible(false);
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        // TODO add your handling code here:
        profile_edit_panel.setVisible(false);
        profile_panel.setVisible(true);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void button_buy_ticket_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_buy_ticket_cancelActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_buy_ticket_cancelActionPerformed

    private void button_account_topup_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_account_topup_backActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_account_topup_backActionPerformed

    private void button_new_transrecord_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_new_transrecord_cancelActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_new_transrecord_cancelActionPerformed

    private void button_buy_ticket_proceedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_buy_ticket_proceedActionPerformed
        // TODO add your handling code here:
        String ticketClass = combo_buy_ticket_class.getSelectedItem().toString();
        String ticketType = combo_buy_ticket_type.getSelectedItem().toString();
        Calendar startDate = dateChooserCombo_buy_ticket_startDate.getCurrent();
        Calendar endDate = dateChooserCombo_buy_ticket_endDate.getCurrent();
        String fromStation = combo_buy_ticket_fromStation.getSelectedItem().toString();
        String toStation = combo_buy_ticket_toStation.getSelectedItem().toString();
        boolean isReturn = checkbox_buy_ticket_isReturn.isSelected();
        int[] transModes =new int[3];
        if(checkbox_buy_ticket_isBus.isSelected()){
            transModes[0] = 1;
        }
        if(checkbox_buy_ticket_isTrain.isSelected()){
            transModes[1] = 1;
        }
        if(checkbox_buy_ticket_isTaxi.isSelected()){
            transModes[2] = 1;
        }
        
        if(payStationController.buyTicket(ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes)){
            JOptionPane.showMessageDialog(null, "Please collect the printed ticket from cashier", "Message", JOptionPane.INFORMATION_MESSAGE);
            label_buy_ticket_ticketFee.setVisible(false);
            button_buy_ticket_proceed.setEnabled(false);
        }else{
            JOptionPane.showMessageDialog(null, "Transaction error!\nContact Admin.","Message", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_button_buy_ticket_proceedActionPerformed

    private void dateChooserCombo_buy_ticket_startDateOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo_buy_ticket_startDateOnSelectionChange
        // TODO add your handling code here:
        dateChooserCombo_buy_ticket_endDate.setSelectedDate(dateChooserCombo_buy_ticket_startDate.getCurrent());
    }//GEN-LAST:event_dateChooserCombo_buy_ticket_startDateOnSelectionChange

    private void button_buy_ticket_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_buy_ticket_clearActionPerformed
        // TODO add your handling code here:
        combo_buy_ticket_class.setEnabled(true);
        combo_buy_ticket_type.setEnabled(true);
        combo_buy_ticket_fromStation.setEnabled(true);
        combo_buy_ticket_toStation.setEnabled(true);
        
        dateChooserCombo_buy_ticket_startDate.setEnabled(true);
        dateChooserCombo_buy_ticket_endDate.setEnabled(true);
        dateChooserCombo_buy_ticket_startDate.setSelectedDate(Calendar.getInstance());
        dateChooserCombo_buy_ticket_endDate.setSelectedDate(Calendar.getInstance());
                
        checkbox_buy_ticket_isReturn.setEnabled(true);
        checkbox_buy_ticket_isBus.setEnabled(true);
        checkbox_buy_ticket_isTaxi.setEnabled(true);
        checkbox_buy_ticket_isTrain.setEnabled(true);
        
        label_buy_ticket_ticketFee.setVisible(false);
        
        checkbox_buy_ticket_isReturn.setSelected(false);
        checkbox_buy_ticket_isBus.setSelected(false);
        checkbox_buy_ticket_isTaxi.setSelected(false);
        checkbox_buy_ticket_isTrain.setSelected(false);
    }//GEN-LAST:event_button_buy_ticket_clearActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        // TODO add your handling code here:
        combo_buy_ticket_class.setEnabled(false);
        combo_buy_ticket_type.setEnabled(false);
        dateChooserCombo_buy_ticket_startDate.setEnabled(false);
        dateChooserCombo_buy_ticket_endDate.setEnabled(false);
        combo_buy_ticket_fromStation.setEnabled(false);
        combo_buy_ticket_toStation.setEnabled(false);
        checkbox_buy_ticket_isReturn.setEnabled(false);
        checkbox_buy_ticket_isBus.setEnabled(false);
        checkbox_buy_ticket_isTaxi.setEnabled(false);
        checkbox_buy_ticket_isTrain.setEnabled(false);
        
        String ticketClass = combo_buy_ticket_class.getSelectedItem().toString();
        String ticketType = combo_buy_ticket_type.getSelectedItem().toString();
        Calendar startDate = dateChooserCombo_buy_ticket_startDate.getCurrent();
        Calendar endDate = dateChooserCombo_buy_ticket_endDate.getCurrent();
        String fromStation = combo_buy_ticket_fromStation.getSelectedItem().toString();
        String toStation = combo_buy_ticket_toStation.getSelectedItem().toString();
        boolean isReturn = checkbox_buy_ticket_isReturn.isSelected();
        int[] transModes =new int[3];
        if(checkbox_buy_ticket_isBus.isSelected()){
            transModes[0] = 1;
        }
        if(checkbox_buy_ticket_isTrain.isSelected()){
            transModes[1] = 1;
        }
        if(checkbox_buy_ticket_isTaxi.isSelected()){
            transModes[2] = 1;
        }
        
        
        float fee = payStationController.findFee(ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes);
        if(fee == 0.0f){
            JOptionPane.showMessageDialog(null,"Zero fee! Check the But Ticket Form\nor\nContact System Admin!");
        }
        else{
            label_buy_ticket_ticketFee.setText(String.valueOf(fee));
            label_buy_ticket_ticketFee.setVisible(true);
            button_buy_ticket_proceed.setEnabled(true);
        }
    }//GEN-LAST:event_jButton14ActionPerformed

    private void button_new_transrecord_proceedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_new_transrecord_proceedActionPerformed
        // TODO add your handling code here:
        int type = combo_new_transrecord_type.getSelectedIndex();
        String passengerID;
        if(checkbox_new_removePassengerID.isSelected()){
            passengerID = "nil";
        }
        else{
            passengerID = combo_new_transrecord_passengerID.getSelectedItem().toString();
        }
        
        float amount = Float.valueOf(text_new_transrecord_amount.getText());
        
        String description = textArea_new_transrecord_description.getText();
        
        if(payStationController.newTransactionRecord(passengerID, description, amount, type)){
            JOptionPane.showMessageDialog(null, "Transaction record saved successfully", "Message", JOptionPane.INFORMATION_MESSAGE);
            checkbox_new_removePassengerID.setSelected(false);
            text_new_transrecord_amount.setText(null);
            textArea_new_transrecord_description.setText(null);
            loadTransactionRecordList();
        }
        else{
            JOptionPane.showMessageDialog(null, "Transaction record NOT saved successfully", "Message", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_button_new_transrecord_proceedActionPerformed

    private void button_homescreen_guestTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_guestTicketActionPerformed
        // TODO add your handling code here:
        hide_panels();
        new_ticket_panel.setVisible(true);
    }//GEN-LAST:event_button_homescreen_guestTicketActionPerformed

    private void button_homescreen_accountTopUpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_accountTopUpActionPerformed
        // TODO add your handling code here:
        hide_panels();
        loadPassengerList();
        label_account_topup_balance.setText(null);
        account_topup_panel.setVisible(true);
    }//GEN-LAST:event_button_homescreen_accountTopUpActionPerformed

    private void button_homescreen_newTransRecordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_newTransRecordActionPerformed
        // TODO add your handling code here:
        hide_panels();
        new_transaction_record_panel.setVisible(true);
    }//GEN-LAST:event_button_homescreen_newTransRecordActionPerformed

    private void button_homescreen_transactionRecordsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_transactionRecordsActionPerformed
        // TODO add your handling code here:
        hide_panels();
        loadTransactionRecordTableData();
        transaction_records_panel.setVisible(true);
    }//GEN-LAST:event_button_homescreen_transactionRecordsActionPerformed

    private void button_homescreen_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_logoutActionPerformed
        // TODO add your handling code here:
        new LogoutConfirm(3).setVisible(true);
    }//GEN-LAST:event_button_homescreen_logoutActionPerformed

    private void button_account_topup_proceedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_account_topup_proceedActionPerformed
        // TODO add your handling code here:
        String passengerID = null;
        try {
            passengerID = combo_account_topup_passengerID.getSelectedItem().toString();
        } catch (Exception e) {
            System.out.println("Procced button: The selected item cannot be converted to string");
        }
        float amount = Float.valueOf(text_account_topup_amount.getText());
        if (payStationController.accountTopUp(passengerID, amount)) {
            JOptionPane.showMessageDialog(null,passengerID + " account was credited LKR"+ amount + "successfully!","Message", JOptionPane.INFORMATION_MESSAGE);
            text_account_topup_amount.setText("");
            label_account_topup_balance.setText(String.valueOf(getPassengerCreditBalance()));
        }
        else{
            text_account_topup_amount.setText("");
            label_account_topup_balance.setText(String.valueOf(getPassengerCreditBalance()));
            JOptionPane.showMessageDialog(null,passengerID + " account was not credited!","Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_button_account_topup_proceedActionPerformed

    private void button_transaction_records_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_transaction_records_backActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_transaction_records_backActionPerformed

    private void combo_account_topup_passengerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_account_topup_passengerIDActionPerformed
        // TODO add your handling code here:
        label_account_topup_balance.setText(String.valueOf(getPassengerCreditBalance()));
    }//GEN-LAST:event_combo_account_topup_passengerIDActionPerformed

    private void checkbox_new_removePassengerIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkbox_new_removePassengerIDActionPerformed
        // TODO add your handling code here:
        combo_new_transrecord_passengerID.setEnabled(false);
    }//GEN-LAST:event_checkbox_new_removePassengerIDActionPerformed

    private void checkbox_new_removePassengerIDItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_checkbox_new_removePassengerIDItemStateChanged
        // TODO add your handling code here:
        
    }//GEN-LAST:event_checkbox_new_removePassengerIDItemStateChanged

    private void checkbox_new_removePassengerIDStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_checkbox_new_removePassengerIDStateChanged
        // TODO add your handling code here:
        boolean seleted = checkbox_new_removePassengerID.isSelected();
        combo_new_transrecord_passengerID.setEnabled(!seleted);
    }//GEN-LAST:event_checkbox_new_removePassengerIDStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PayStationTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PayStationTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PayStationTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PayStationTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new PayStationTerminal().setVisible(false);
                //payStationTerminal.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel account_topup_panel;
    private javax.swing.JButton button_account_topup_back;
    private javax.swing.JButton button_account_topup_proceed;
    private javax.swing.JButton button_buy_ticket_cancel;
    private javax.swing.JButton button_buy_ticket_clear;
    private javax.swing.JButton button_buy_ticket_proceed;
    private javax.swing.JButton button_homescreen_accountTopUp;
    private javax.swing.JButton button_homescreen_guestTicket;
    private javax.swing.JButton button_homescreen_logout;
    private javax.swing.JButton button_homescreen_newTransRecord;
    private javax.swing.JButton button_homescreen_profile;
    private javax.swing.JButton button_homescreen_transactionRecords;
    private javax.swing.JButton button_new_transrecord_cancel;
    private javax.swing.JButton button_new_transrecord_proceed;
    private javax.swing.JButton button_transaction_records_back;
    private javax.swing.JCheckBox checkbox_buy_ticket_isBus;
    private javax.swing.JCheckBox checkbox_buy_ticket_isReturn;
    private javax.swing.JCheckBox checkbox_buy_ticket_isTaxi;
    private javax.swing.JCheckBox checkbox_buy_ticket_isTrain;
    private javax.swing.JCheckBox checkbox_new_removePassengerID;
    private javax.swing.JComboBox combo_account_topup_passengerID;
    private javax.swing.JComboBox combo_buy_ticket_class;
    private javax.swing.JComboBox combo_buy_ticket_fromStation;
    private javax.swing.JComboBox combo_buy_ticket_toStation;
    private javax.swing.JComboBox combo_buy_ticket_type;
    private javax.swing.JComboBox combo_new_transrecord_passengerID;
    private javax.swing.JComboBox combo_new_transrecord_type;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private datechooser.beans.DateChooserCombo dateChooserCombo_buy_ticket_endDate;
    private datechooser.beans.DateChooserCombo dateChooserCombo_buy_ticket_startDate;
    private javax.swing.JPanel homescreen_panel;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton8;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel138;
    private javax.swing.JLabel jLabel139;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JLabel label_account_topup_balance;
    private javax.swing.JLabel label_buy_ticket_ticketFee;
    private javax.swing.JLabel label_cashierID;
    private javax.swing.JLabel label_fixed_cashierID;
    private javax.swing.JLabel label_homescreen_currentUsername;
    private javax.swing.JLabel label_transaction_records_count;
    private javax.swing.JPanel new_ticket_panel;
    private javax.swing.JPanel new_transaction_record_panel;
    private javax.swing.JPanel panel_tokenQRCode1;
    private javax.swing.JPanel panel_tokenQRCode2;
    private javax.swing.JPanel profile_edit_panel;
    private javax.swing.JPanel profile_panel;
    private javax.swing.JTable table_transaction_records;
    private javax.swing.JTextArea textArea_new_transrecord_description;
    private javax.swing.JTextField text_account_topup_amount;
    private javax.swing.JTextField text_new_transrecord_amount;
    private javax.swing.JPanel transaction_records_panel;
    // End of variables declaration//GEN-END:variables

    
}
