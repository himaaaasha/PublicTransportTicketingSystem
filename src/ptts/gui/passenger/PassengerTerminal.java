
package ptts.gui.passenger;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ptts.entities.CheckIn;
import ptts.entities.Passenger;
import ptts.entities.Ticket;
import ptts.entities.Token;
import ptts.entities.Journey;
import ptts.entities.TransactionRecord;
import ptts.entities.managers.TransactionRecordManager;
import ptts.gui.LogoutConfirm;
import static ptts.main.PublicTransportTicketingSystem.passengerController;

/**
 *
 * @author Siri@MnS
 */
public class PassengerTerminal extends javax.swing.JFrame {

    /**
     * Creates new form PassengerTerminal
     */
    
    //public static JFrame passengerTerminal = new PassengerTerminal();
    public static JFrame passengerTerminal;
    private Passenger currentPassenger = null;
    
    ArrayList<TransactionRecord> passengerTransactionRecordList;
    ArrayList<Journey> journeyList;
    ArrayList<Ticket> activeTicketList;
    
    //Account History Table variables
    String[] transactionRecordsTableColumns = {"Date","Record ID","Cashier ID", "Passenger ID", "Desciption", "Expense (LKR)", "Receipt (LKR)"};
    DefaultTableModel tableModel_transaction_records = new DefaultTableModel(transactionRecordsTableColumns, 0);
    TableRowSorter<TableModel> rowSorter_transaction_records = new TableRowSorter<>(tableModel_transaction_records);
    
    
    //Active Ticket Table variables
    String[] activeTicketTableColumns = {"Ticket ID", "Date", "From", "To", "Return", "Expires On"};
    DefaultTableModel tableModel_active_tickets = new DefaultTableModel(activeTicketTableColumns, 0);
    TableRowSorter<TableModel> rowSorter_active_tickets = new TableRowSorter<>(tableModel_active_tickets);
    
    //Journey Table variables
    String[] journeyHistoryTableColumns = {"Date", "From", "To", "Ticket ID", "Distance", "Start Time", "End Time", "Duration"};
    DefaultTableModel tableModel_journey_history = new DefaultTableModel(journeyHistoryTableColumns, 0);
    TableRowSorter<TableModel> rowSorter_journey_history = new TableRowSorter<>(tableModel_journey_history);
    
    public PassengerTerminal() {
        initComponents();
        setLocationRelativeTo(null); //centering the JFrame in the computer display
        currentPassenger = passengerController.getCurrentPassenger();
        hide_panels();
        
        // TO DO: populate the relavent panel accourding to the attributes of current user
        //        before making the panel visible
        populate_home_screen_panel();
        populate_profile_and_edit_panels();
        populate_buy_ticket_panel();
        
        homescreen_panel.setVisible(true);

    }
    
    private void loadPassengerDetails(){
        //reload current passenger from Passenger Controller
        //assign his differecnt ticket lists, load transRec list, calculate account balance etc.
        currentPassenger = passengerController.getCurrentPassenger();
        //simultaneously update passenger details in PassengerManager
    }
    
    private void hide_panels(){
        homescreen_panel.setVisible(false);
        profile_panel.setVisible(false);
        profile_edit_panel.setVisible(false);
        buy_ticket_panel.setVisible(false);
        journey_history_panel.setVisible(false);
        account_topup_panel.setVisible(false);
        account_history_panel.setVisible(false);
        ticket_details_panel.setVisible(false);
    }
    
    private void populate_home_screen_panel() {
        loadPassengerDetails();
        label_homescreen_currentUsername.setText(currentPassenger.getName());
        label_homescreen_currentUserID.setText(currentPassenger.getUserID());
        label_homescreen_creditBalance.setText(String.valueOf(currentPassenger.getCreditBalance()));
        try {
            Token token = currentPassenger.getToken();
            
            label_homescreen_tokenID.setText(token.getTokenID());
            if(token.isActive()){
            label_homescreen_tokenStatus.setText("Active");
            label_homescreen_tokenStatus.setForeground(Color.GREEN);
            label_homescreen_scheduledTripCount.setText(String.valueOf(token.getActiveTicketList().size()));
            }
            else{
                label_homescreen_tokenStatus.setText("Active");
                label_homescreen_tokenStatus.setForeground(Color.RED);
            }
            
        } catch (NullPointerException e) {
            label_homescreen_tokenID.setText("NO Token");
            label_homescreen_tokenID.setForeground(Color.RED);
            label_homescreen_tokenStatus.setText("--");
            label_homescreen_tokenStatus.setForeground(Color.RED);
            label_homescreen_scheduledTripCount.setText("--");
            label_homescreen_scheduledTripCount.setForeground(Color.RED);
        }
        loadActiveTicketTableData();
        
        
        
    }
    
    private void populate_profile_and_edit_panels() {
        loadPassengerDetails();
        label_profile_userName.setText(currentPassenger.getName());
        if (currentPassenger.getGender() == 1) {
            label_profile_userGender.setText("Male");
        }
        else{
            label_profile_userGender.setText("Male");
        }
        label_profile_userContactNumber.setText(currentPassenger.getContactNumber());
        label_profile_userEmail.setText(currentPassenger.getEmail());
        label_profile_userEmail.setText(currentPassenger.getAddress());
        
        text_profile_edit_userName.setText(currentPassenger.getName());
        if (currentPassenger.getGender() == 1) {
            comboBox_profile_edit_gender.setSelectedIndex(0);
        }
        else{
            comboBox_profile_edit_gender.setSelectedIndex(1);
        }
        text_profile_edit_userContactNumber.setText(currentPassenger.getContactNumber());
        text_profile_edit_userEmail.setText(currentPassenger.getEmail());
        textArea_profile_edit_userAddress.setText(currentPassenger.getAddress());
        
        
    }
    
    private void populate_buy_ticket_panel(){
        loadPassengerDetails();
        label_buy_ticket_ticketFee.setVisible(false);
        label_buy_ticket_accountBalance.setText(String.valueOf(currentPassenger.getCreditBalance()));
        button_buy_ticket_buy.setEnabled(false);
        
        dateChooserCombo_buy_ticket_startDate.setSelectedDate(Calendar.getInstance());
        dateChooserCombo_buy_ticket_startDate.setMinDate(Calendar.getInstance());
        dateChooserCombo_buy_ticket_endDate.setSelectedDate(Calendar.getInstance());
        dateChooserCombo_buy_ticket_endDate.setMinDate(dateChooserCombo_buy_ticket_startDate.getCurrent());
        
    }
    
    private boolean loadTransactionRecordList(){
        
        //ArrayList<TransactionRecord> tempTranRecList;
        
        try {
            passengerTransactionRecordList = TransactionRecordManager.getTransRecordListByPassengerID(currentPassenger.getUserID());
            label_account_history_balance.setText(String.valueOf(currentPassenger.getCreditBalance()));
            return true;
            
        } catch (Exception e) {
            System.out.println("Passenger Terminal: Account History: Null TransRec List!");
            return false;
        }
    }
    
    private void loadTransactionRecordTableData(){

        table_account_history.setModel(tableModel_transaction_records);
        table_account_history.setRowSorter(rowSorter_transaction_records);
        
        if (loadTransactionRecordList()) {
            for (int i = 0; i < passengerTransactionRecordList.size(); i++) {
                
                TransactionRecord tr = passengerTransactionRecordList.get(i);
                
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
                
                Object[] data = {date, transID, cashierID, passengerID, description, debit, credit};
                tableModel_transaction_records.addRow(data);
                
            }
        }
        
    }
    
  
    
    private boolean loadActiveList(){
        
        //ArrayList<TransactionRecord> tempTranRecList;
        
        try {
            activeTicketList = currentPassenger.getActiveTicketList();
            //label_homescreen_scheduledTripCount.setText(String.valueOf(activeTicketList.size()));
            return true;
            
        } catch (Exception e) {
            System.out.println("Passenger Terminal: Homescreen: Null Active Ticket List!");
            return false;
        }
    }
    
    private void loadActiveTicketTableData(){

        table_active_tickets.setModel(tableModel_active_tickets);
        table_active_tickets.setRowSorter(rowSorter_active_tickets);
        
        if (loadActiveList()) {
            for (int i = 0; i < activeTicketList.size(); i++) {
                
                Ticket ticket = activeTicketList.get(i);
                
                String fromStation = ticket.getFromStop();
                String toStation = ticket.getDestinationStop();
                String ticketID = ticket.getTicketID();
                
                String returnStatus;
                
                if (ticket.isReturn()) {
                    returnStatus = "YES";
                } else {
                    returnStatus = "NO";
                }
                
                
                Calendar cal_1 = ticket.getStartDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String dateIssued = dateFormat.format(cal_1.getTime());
                
                Calendar cal_2 = ticket.getValidUntilDate();
                String dateExpires = dateFormat.format(cal_2.getTime());
                
                
                
                Object[] data = {ticketID, dateIssued, fromStation, toStation, returnStatus, dateExpires};
                tableModel_active_tickets.addRow(data);
                
            }
        }
        
    }
    
    private String selectedTicketID_active_tickets_table() {
      
        String selectedTicketID= null;
        
        //retrieving the selected row index
        int row = table_active_tickets.getSelectedRow();

        //if a single row is selected from the table, get selectedBookNo
        if (table_active_tickets.getRowSelectionAllowed()) {
            selectedTicketID =  table_active_tickets.getValueAt(row,0).toString();
        }
        
        return selectedTicketID;
    }
    
    private int getSeletedTicketIndex_active_tickets_table(String selectedTicketID) {
        int userIndex = -1;
 
        for(int i=0; i<activeTicketList.size(); i++){
            if(activeTicketList.get(i).getTicketID().equals(selectedTicketID)){
                userIndex = i;
            }
        }

        return userIndex;
    }
    
    /*
    private boolean loadJourneyList(){
        
        //ArrayList<TransactionRecord> tempTranRecList;
        
        try {
            passengerTransactionRecordList = TransactionRecordManager.getTransRecordListByPassengerID(currentPassenger.getUserID());
            label_account_history_balance.setText(String.valueOf(currentPassenger.getCreditBalance()));
            return true;
            
        } catch (Exception e) {
            System.out.println("Passenger Terminal: Account History: Null TransRec List!");
            return false;
        }
    }
    
    private void loadJourneyHistoryTableData(){

        table_account_history.setModel(tableModel_transaction_records);
        table_account_history.setRowSorter(rowSorter_transaction_records);
        
        if (loadTransactionRecordList()) {
            for (int i = 0; i < passengerTransactionRecordList.size(); i++) {
                
                TransactionRecord tr = passengerTransactionRecordList.get(i);
                
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
                
                Object[] data = {date, transID, cashierID, passengerID, description, debit, credit};
                tableModel_transaction_records.addRow(data);
                
            }
        }
        
    }
    
    
    */
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainMenu_panel = new javax.swing.JPanel();
        button_profile = new javax.swing.JButton();
        button_new_journey = new javax.swing.JButton();
        button_journey_history = new javax.swing.JButton();
        button_topup = new javax.swing.JButton();
        button_share_credits = new javax.swing.JButton();
        button_account_history = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        button_homescreen_home = new javax.swing.JButton();
        contentPlaceholder_panel = new javax.swing.JPanel();
        homescreen_panel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        table_active_tickets = new javax.swing.JTable();
        label_homescreen_scheduledTripCount = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        label_homescreen_currentUserID = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        label_homescreen_tokenID = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        label_homescreen_tokenStatus = new javax.swing.JLabel();
        panel_tokenQRCode3 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        label_homescreen_creditBalance = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        panel_userPic1 = new javax.swing.JPanel();
        button_homescreen_logout = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        label_homescreen_currentUsername = new javax.swing.JLabel();
        button_homescreen_viewTicket = new javax.swing.JButton();
        profile_panel = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        label_profile_userName = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        label_profile_userGender = new javax.swing.JLabel();
        panel_tokenQRCode1 = new javax.swing.JPanel();
        button_profile_edit = new javax.swing.JButton();
        jLabel27 = new javax.swing.JLabel();
        label_profile_userContactNumber = new javax.swing.JLabel();
        label_profile_userEmail = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        button_profile_back = new javax.swing.JButton();
        profile_edit_panel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        panel_tokenQRCode2 = new javax.swing.JPanel();
        button_profile_edit_done = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        button_profile_edit_cancel = new javax.swing.JButton();
        text_profile_edit_userName = new javax.swing.JTextField();
        text_profile_edit_userContactNumber = new javax.swing.JTextField();
        text_profile_edit_userEmail = new javax.swing.JTextField();
        textArea_profile_edit_userAddress = new javax.swing.JTextField();
        comboBox_profile_edit_gender = new javax.swing.JComboBox();
        buy_ticket_panel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        button_buy_ticket_findFee = new javax.swing.JButton();
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
        button_buy_ticket_buy = new javax.swing.JButton();
        jLabel55 = new javax.swing.JLabel();
        label_buy_ticket_accountBalance = new javax.swing.JLabel();
        dateChooserCombo_buy_ticket_startDate = new datechooser.beans.DateChooserCombo();
        dateChooserCombo_buy_ticket_endDate = new datechooser.beans.DateChooserCombo();
        button_buy_ticket_clear = new javax.swing.JButton();
        journey_history_panel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_journey_history = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        jTextField18 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        jButton19 = new javax.swing.JButton();
        account_topup_panel = new javax.swing.JPanel();
        jLabel84 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        label_accountTopUp_back = new javax.swing.JButton();
        text_accountTopUp_amout = new javax.swing.JTextField();
        button_accountTopUp_onlineBanking = new javax.swing.JButton();
        jLabel90 = new javax.swing.JLabel();
        button_accountTopUp_debitCredutCard = new javax.swing.JButton();
        button_accountTopUp_mobilePay = new javax.swing.JButton();
        jLabel87 = new javax.swing.JLabel();
        label_accountTopUp_creditBalance = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        account_history_panel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_account_history = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jLabel94 = new javax.swing.JLabel();
        label_account_history_balance = new javax.swing.JLabel();
        jLabel96 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        jButton18 = new javax.swing.JButton();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        ticket_details_panel = new javax.swing.JPanel();
        jLabel172 = new javax.swing.JLabel();
        jLabel173 = new javax.swing.JLabel();
        label_ticket_details_ticketID = new javax.swing.JLabel();
        jLabel175 = new javax.swing.JLabel();
        label_ticket_details_cashierID = new javax.swing.JLabel();
        button_ticket_details_print = new javax.swing.JButton();
        label_ticket_details_transRecID = new javax.swing.JLabel();
        jLabel178 = new javax.swing.JLabel();
        button_ticket_details_back = new javax.swing.JButton();
        jLabel179 = new javax.swing.JLabel();
        label_ticket_details_class = new javax.swing.JLabel();
        jLabel181 = new javax.swing.JLabel();
        label_ticket_details_fromStation = new javax.swing.JLabel();
        jLabel183 = new javax.swing.JLabel();
        jLabel184 = new javax.swing.JLabel();
        label_ticket_details_endDate = new javax.swing.JLabel();
        label_ticket_details_toStation = new javax.swing.JLabel();
        jLabel187 = new javax.swing.JLabel();
        label_ticket_details_startDate = new javax.swing.JLabel();
        jLabel189 = new javax.swing.JLabel();
        label_ticket_details_ticketType = new javax.swing.JLabel();
        label_ticket_details_isReturn = new javax.swing.JLabel();
        jLabel192 = new javax.swing.JLabel();
        label_ticket_details_train = new javax.swing.JLabel();
        label_ticket_details_bus = new javax.swing.JLabel();
        label_ticket_details_taxi = new javax.swing.JLabel();
        jLabel196 = new javax.swing.JLabel();
        jLabel197 = new javax.swing.JLabel();
        jLabel198 = new javax.swing.JLabel();
        jLabel199 = new javax.swing.JLabel();
        label_ticket_details_fee = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        jLabel201 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        textArea_ticket_details_checkInList = new javax.swing.JList();
        jLabel202 = new javax.swing.JLabel();
        label_ticket_details_distanceTravelled = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PTTS v1.0  Passenger Terminal");
        setPreferredSize(new java.awt.Dimension(650, 420));
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        mainMenu_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        button_profile.setText("Profile");
        button_profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_profileActionPerformed(evt);
            }
        });

        button_new_journey.setText("New Ticket");
        button_new_journey.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_new_journeyActionPerformed(evt);
            }
        });

        button_journey_history.setText("History");
        button_journey_history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_journey_historyActionPerformed(evt);
            }
        });

        button_topup.setText("Top-up");
        button_topup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_topupActionPerformed(evt);
            }
        });

        button_share_credits.setText("Share Credits");
        button_share_credits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_share_creditsActionPerformed(evt);
            }
        });

        button_account_history.setText("History");
        button_account_history.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_account_historyActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("My Account");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Journey");

        button_homescreen_home.setText("Home");
        button_homescreen_home.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_homeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainMenu_panelLayout = new javax.swing.GroupLayout(mainMenu_panel);
        mainMenu_panel.setLayout(mainMenu_panelLayout);
        mainMenu_panelLayout.setHorizontalGroup(
            mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenu_panelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(button_profile)
                    .addComponent(button_new_journey)
                    .addComponent(button_journey_history, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_topup)
                    .addComponent(button_share_credits)
                    .addComponent(button_account_history)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(button_homescreen_home))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainMenu_panelLayout.setVerticalGroup(
            mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenu_panelLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(button_profile)
                .addGap(13, 13, 13)
                .addComponent(jLabel2)
                .addGap(14, 14, 14)
                .addComponent(button_new_journey)
                .addGap(18, 18, 18)
                .addComponent(button_journey_history)
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_topup)
                .addGap(18, 18, 18)
                .addComponent(button_share_credits)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button_account_history)
                .addGap(18, 18, 18)
                .addComponent(button_homescreen_home)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        getContentPane().add(mainMenu_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 370));

        contentPlaceholder_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        contentPlaceholder_panel.setPreferredSize(new java.awt.Dimension(500, 370));
        contentPlaceholder_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        homescreen_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_active_tickets.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Date", "From", "To", "Return", "Ticket ID", "Expires"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_active_tickets.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_active_ticketsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(table_active_tickets);

        label_homescreen_scheduledTripCount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_homescreen_scheduledTripCount.setText("tripCount");

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel34.setText("Hi, ");

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setText("Passenger ID:");

        label_homescreen_currentUserID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_homescreen_currentUserID.setText("passengerID");

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setText("Token ID:");

        label_homescreen_tokenID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_homescreen_tokenID.setText("tokenID");

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel49.setText("Token Status:");

        label_homescreen_tokenStatus.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_homescreen_tokenStatus.setText("tokenStatus");

        panel_tokenQRCode3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panel_tokenQRCode3Layout = new javax.swing.GroupLayout(panel_tokenQRCode3);
        panel_tokenQRCode3.setLayout(panel_tokenQRCode3Layout);
        panel_tokenQRCode3Layout.setHorizontalGroup(
            panel_tokenQRCode3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panel_tokenQRCode3Layout.setVerticalGroup(
            panel_tokenQRCode3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel51.setText("Credit Balance");

        label_homescreen_creditBalance.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_homescreen_creditBalance.setText("00.00");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel53.setText("LKR");

        panel_userPic1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panel_userPic1Layout = new javax.swing.GroupLayout(panel_userPic1);
        panel_userPic1.setLayout(panel_userPic1Layout);
        panel_userPic1Layout.setHorizontalGroup(
            panel_userPic1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panel_userPic1Layout.setVerticalGroup(
            panel_userPic1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        button_homescreen_logout.setText("Logout");
        button_homescreen_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_logoutActionPerformed(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel54.setText("Scheduled Trips:");

        label_homescreen_currentUsername.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_homescreen_currentUsername.setText("Username");

        button_homescreen_viewTicket.setText("View Ticket");
        button_homescreen_viewTicket.setEnabled(false);
        button_homescreen_viewTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_homescreen_viewTicketActionPerformed(evt);
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
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel34)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_homescreen_currentUsername))
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel38)
                                .addGap(18, 18, 18)
                                .addComponent(label_homescreen_currentUserID))
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(panel_tokenQRCode3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel49)
                                    .addComponent(jLabel47))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label_homescreen_tokenID)
                                    .addComponent(label_homescreen_tokenStatus))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homescreen_panelLayout.createSequentialGroup()
                                .addComponent(panel_userPic1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(button_homescreen_logout)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homescreen_panelLayout.createSequentialGroup()
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel53)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(label_homescreen_creditBalance))
                                    .addComponent(jLabel51))
                                .addGap(56, 56, 56))))
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel54)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_homescreen_scheduledTripCount))
                            .addComponent(button_homescreen_viewTicket))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        homescreen_panelLayout.setVerticalGroup(
            homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, homescreen_panelLayout.createSequentialGroup()
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel34)
                            .addComponent(label_homescreen_currentUsername))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel38)
                            .addComponent(label_homescreen_currentUserID)))
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_homescreen_logout)
                            .addComponent(panel_userPic1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel51)
                                .addGap(18, 18, 18)
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel53)
                                    .addComponent(label_homescreen_creditBalance)))
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel47)
                                    .addComponent(label_homescreen_tokenID))
                                .addGap(18, 18, 18)
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel49)
                                    .addComponent(label_homescreen_tokenStatus)))))
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(panel_tokenQRCode3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel54)
                    .addComponent(label_homescreen_scheduledTripCount))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_homescreen_viewTicket)
                .addGap(7, 7, 7))
        );

        contentPlaceholder_panel.add(homescreen_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        profile_panel.setEnabled(false);
        profile_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel16.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel16.setText("Profile");

        jLabel19.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel19.setText("Name");

        label_profile_userName.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_profile_userName.setText("userName");

        jLabel21.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel21.setText("Gender");

        label_profile_userGender.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_profile_userGender.setText("userGender");

        panel_tokenQRCode1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_tokenQRCode1.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout panel_tokenQRCode1Layout = new javax.swing.GroupLayout(panel_tokenQRCode1);
        panel_tokenQRCode1.setLayout(panel_tokenQRCode1Layout);
        panel_tokenQRCode1Layout.setHorizontalGroup(
            panel_tokenQRCode1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );
        panel_tokenQRCode1Layout.setVerticalGroup(
            panel_tokenQRCode1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );

        button_profile_edit.setText("Edit");
        button_profile_edit.setMaximumSize(new java.awt.Dimension(67, 23));
        button_profile_edit.setPreferredSize(new java.awt.Dimension(67, 23));
        button_profile_edit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_profile_editActionPerformed(evt);
            }
        });

        jLabel27.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel27.setText("Contact Number");

        label_profile_userContactNumber.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_profile_userContactNumber.setText("userContactNumber");

        label_profile_userEmail.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_profile_userEmail.setText("userEmail");

        jLabel30.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel30.setText("Email");

        jLabel31.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel31.setText("Address");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("userAddress");

        button_profile_back.setText("Back");
        button_profile_back.setMaximumSize(new java.awt.Dimension(67, 23));
        button_profile_back.setPreferredSize(new java.awt.Dimension(67, 23));
        button_profile_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_profile_backActionPerformed(evt);
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
                        .addComponent(jLabel16)
                        .addGap(356, 356, 356)
                        .addComponent(button_profile_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(profile_panelLayout.createSequentialGroup()
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
                                    .addComponent(label_profile_userName)
                                    .addComponent(label_profile_userGender)
                                    .addComponent(label_profile_userContactNumber)
                                    .addComponent(label_profile_userEmail)
                                    .addComponent(jLabel32)))
                            .addComponent(panel_tokenQRCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_profile_back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        profile_panelLayout.setVerticalGroup(
            profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(button_profile_edit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_tokenQRCode1, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(label_profile_userName))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel21)
                    .addComponent(label_profile_userGender))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(label_profile_userContactNumber))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(label_profile_userEmail))
                .addGap(18, 18, 18)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(jLabel31))
                .addGap(24, 24, 24)
                .addComponent(button_profile_back, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        contentPlaceholder_panel.add(profile_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

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

        button_profile_edit_done.setText("Done");

        jLabel33.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel33.setText("Contact Number");

        jLabel36.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel36.setText("Email");

        jLabel37.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel37.setText("Address");

        jButton12.setText("Change Picture");

        button_profile_edit_cancel.setText("Cancel");
        button_profile_edit_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_profile_edit_cancelActionPerformed(evt);
            }
        });

        text_profile_edit_userName.setText("userName");

        text_profile_edit_userContactNumber.setText("userContactNumber");

        text_profile_edit_userEmail.setText("userEmail");

        textArea_profile_edit_userAddress.setText("userAddress");

        comboBox_profile_edit_gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        javax.swing.GroupLayout profile_edit_panelLayout = new javax.swing.GroupLayout(profile_edit_panel);
        profile_edit_panel.setLayout(profile_edit_panelLayout);
        profile_edit_panelLayout.setHorizontalGroup(
            profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(profile_edit_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(button_profile_edit_cancel))
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(profile_edit_panelLayout.createSequentialGroup()
                                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jLabel25)
                                    .addComponent(jLabel33)
                                    .addComponent(jLabel36)
                                    .addComponent(jLabel37))
                                .addGap(18, 18, 18)
                                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(text_profile_edit_userName)
                                    .addComponent(text_profile_edit_userContactNumber)
                                    .addComponent(text_profile_edit_userEmail)
                                    .addComponent(textArea_profile_edit_userAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboBox_profile_edit_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(profile_edit_panelLayout.createSequentialGroup()
                                .addComponent(panel_tokenQRCode2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton12)))
                        .addGap(0, 189, Short.MAX_VALUE))
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button_profile_edit_done, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        profile_edit_panelLayout.setVerticalGroup(
            profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_edit_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(button_profile_edit_done))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_tokenQRCode2, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addGap(0, 74, Short.MAX_VALUE)
                        .addComponent(jButton12)))
                .addGap(29, 29, 29)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button_profile_edit_cancel)
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
                            .addComponent(text_profile_edit_userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(comboBox_profile_edit_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(text_profile_edit_userContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(text_profile_edit_userEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(textArea_profile_edit_userAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

        contentPlaceholder_panel.add(profile_edit_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        buy_ticket_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setText("Buy Ticket");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("To");

        button_buy_ticket_findFee.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_buy_ticket_findFee.setText("<html><center>Find<br>Fee</center>");
        button_buy_ticket_findFee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_buy_ticket_findFeeActionPerformed(evt);
            }
        });

        button_buy_ticket_cancel.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_buy_ticket_cancel.setText("Back");
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

        button_buy_ticket_buy.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        button_buy_ticket_buy.setText("Buy");
        button_buy_ticket_buy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_buy_ticket_buyActionPerformed(evt);
            }
        });

        jLabel55.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel55.setText("Credit Balance   LKR");

        label_buy_ticket_accountBalance.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_buy_ticket_accountBalance.setText("#balance");

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

        javax.swing.GroupLayout buy_ticket_panelLayout = new javax.swing.GroupLayout(buy_ticket_panel);
        buy_ticket_panel.setLayout(buy_ticket_panelLayout);
        buy_ticket_panelLayout.setHorizontalGroup(
            buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                        .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel44)
                            .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel42)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel35))
                                .addGap(18, 18, 18)
                                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(combo_buy_ticket_class, 0, 1, Short.MAX_VALUE)
                                    .addComponent(combo_buy_ticket_fromStation, 0, 80, Short.MAX_VALUE)
                                    .addComponent(combo_buy_ticket_toStation, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateChooserCombo_buy_ticket_startDate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                    .addComponent(dateChooserCombo_buy_ticket_endDate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addGap(65, 65, 65)
                                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel40)
                                        .addGap(18, 18, 18)
                                        .addComponent(combo_buy_ticket_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                        .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(button_buy_ticket_buy)
                                            .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel45)
                                                .addComponent(checkbox_buy_ticket_isReturn)
                                                .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                                    .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                                            .addComponent(checkbox_buy_ticket_isBus)
                                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(checkbox_buy_ticket_isTrain))
                                                        .addComponent(jLabel43, javax.swing.GroupLayout.Alignment.LEADING))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addComponent(checkbox_buy_ticket_isTaxi))))
                                        .addGap(18, 18, 18)
                                        .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                                .addComponent(button_buy_ticket_findFee, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(button_buy_ticket_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                            .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                .addComponent(jLabel46)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_buy_ticket_ticketFee)))
                        .addContainerGap())
                    .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel55)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_buy_ticket_accountBalance)
                        .addGap(24, 24, 24))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buy_ticket_panelLayout.createSequentialGroup()
                .addGap(411, 411, 411)
                .addComponent(button_buy_ticket_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        buy_ticket_panelLayout.setVerticalGroup(
            buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buy_ticket_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel55)
                    .addComponent(label_buy_ticket_accountBalance))
                .addGap(18, 18, 18)
                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(combo_buy_ticket_class, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel40)
                    .addComponent(combo_buy_ticket_type, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel41)
                    .addComponent(dateChooserCombo_buy_ticket_startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dateChooserCombo_buy_ticket_endDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42))
                        .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buy_ticket_panelLayout.createSequentialGroup()
                                .addGap(104, 104, 104)
                                .addComponent(button_buy_ticket_clear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(25, 25, 25)
                                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(button_buy_ticket_buy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(button_buy_ticket_cancel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(33, 33, 33))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, buy_ticket_panelLayout.createSequentialGroup()
                                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, buy_ticket_panelLayout.createSequentialGroup()
                                        .addGap(25, 25, 25)
                                        .addComponent(jLabel44)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel26)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                                .addComponent(jLabel45)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(checkbox_buy_ticket_isReturn))
                                            .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                                                .addComponent(combo_buy_ticket_fromStation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(combo_buy_ticket_toStation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(64, 64, 64)
                                .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(label_buy_ticket_ticketFee)
                                    .addComponent(jLabel46))
                                .addGap(51, 51, 51))))
                    .addGroup(buy_ticket_panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(buy_ticket_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkbox_buy_ticket_isBus)
                            .addComponent(checkbox_buy_ticket_isTrain, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkbox_buy_ticket_isTaxi))
                        .addGap(28, 28, 28)
                        .addComponent(button_buy_ticket_findFee, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        contentPlaceholder_panel.add(buy_ticket_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        journey_history_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_journey_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "From", "To", "Ticket ID", "Distance (KM)", "Start Time", "End Time", "Duration"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, true, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_journey_history.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jScrollPane3.setViewportView(table_journey_history);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel8.setText("#count");

        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setText("Journey History");

        jLabel65.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel65.setText("Total Tickets");

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel66.setText("#count");

        jLabel67.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel67.setText("From");

        jLabel68.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel68.setText("To");

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel69.setText("Distance Travelled");

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel70.setText("#distance");

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel71.setText("KM");

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel72.setText("Search Results");

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel73.setText("Active Tickets");

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel74.setText("#count");

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel75.setText("Search");

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel76.setText("Location");

        jTextField13.setText("from");

        jTextField14.setText("to");

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel77.setText("Fare");

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel78.setText("Min");

        jTextField15.setText("min");

        jTextField16.setText("max");

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel79.setText("Max");

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel80.setText("Distance");

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel81.setText("Max");

        jTextField17.setText("to");

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel82.setText("Min");

        jTextField18.setText("from");

        jButton17.setText("Back");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton19.setText("View Ticket");
        jButton19.setEnabled(false);
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout journey_history_panelLayout = new javax.swing.GroupLayout(journey_history_panel);
        journey_history_panel.setLayout(journey_history_panelLayout);
        journey_history_panelLayout.setHorizontalGroup(
            journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(journey_history_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(journey_history_panelLayout.createSequentialGroup()
                                .addComponent(jLabel72)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel8)
                                .addGap(318, 318, 318))
                            .addComponent(jScrollPane3))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                                .addComponent(jLabel70)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel71))
                            .addComponent(jLabel69))
                        .addGap(25, 25, 25))
                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(journey_history_panelLayout.createSequentialGroup()
                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel76)
                                            .addGroup(journey_history_panelLayout.createSequentialGroup()
                                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel67)
                                                    .addComponent(jLabel68))
                                                .addGap(18, 18, 18)
                                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField13, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                                                    .addComponent(jTextField14))))
                                        .addGap(48, 48, 48)
                                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel77)
                                            .addGroup(journey_history_panelLayout.createSequentialGroup()
                                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel78)
                                                    .addComponent(jLabel79))
                                                .addGap(18, 18, 18)
                                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField15)
                                                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(14, 14, 14))
                                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel65)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel66)
                                        .addGap(39, 39, 39)
                                        .addComponent(jLabel73)))
                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel74))
                                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addComponent(jLabel80))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jLabel82)
                                        .addGap(21, 21, 21))))
                            .addComponent(jLabel10))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel81)
                        .addGap(18, 18, 18)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField18)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(92, 92, 92))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                        .addComponent(jButton19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton17)
                        .addContainerGap())))
        );
        journey_history_panelLayout.setVerticalGroup(
            journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                        .addComponent(jLabel69)
                        .addGap(8, 8, 8)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel71)
                            .addComponent(jLabel70))
                        .addGap(11, 11, 11)
                        .addComponent(jLabel80)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel82)
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel81)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(journey_history_panelLayout.createSequentialGroup()
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel65)
                            .addComponent(jLabel66)
                            .addComponent(jLabel73)
                            .addComponent(jLabel74))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel75)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel77)
                            .addComponent(jLabel76))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(journey_history_panelLayout.createSequentialGroup()
                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel78)
                                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel79)
                                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, journey_history_panelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel67)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel68))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel72)
                            .addComponent(jLabel8))))
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(journey_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton17)
                    .addComponent(jButton19)))
        );

        contentPlaceholder_panel.add(journey_history_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        account_topup_panel.setEnabled(false);
        account_topup_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel84.setText("Account Top-up");

        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel85.setText("Amount");

        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel86.setText("Pay with");

        label_accountTopUp_back.setText("Back");
        label_accountTopUp_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                label_accountTopUp_backActionPerformed(evt);
            }
        });

        text_accountTopUp_amout.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N

        button_accountTopUp_onlineBanking.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        button_accountTopUp_onlineBanking.setText("<html><center>Online<br>Banking<center>");
        button_accountTopUp_onlineBanking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_accountTopUp_onlineBankingActionPerformed(evt);
            }
        });

        jLabel90.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel90.setText("LKR");

        button_accountTopUp_debitCredutCard.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        button_accountTopUp_debitCredutCard.setText("<html><center>Debit/Credit<br>Card<center>");
        button_accountTopUp_debitCredutCard.setMargin(new java.awt.Insets(2, 1, 2, 1));
        button_accountTopUp_debitCredutCard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_accountTopUp_debitCredutCardActionPerformed(evt);
            }
        });

        button_accountTopUp_mobilePay.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        button_accountTopUp_mobilePay.setText("<html><center>Mobile<br>Pay<center>");
        button_accountTopUp_mobilePay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_accountTopUp_mobilePayActionPerformed(evt);
            }
        });

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel87.setText("Credit Balance");

        label_accountTopUp_creditBalance.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        label_accountTopUp_creditBalance.setText("#balance");

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 15)); // NOI18N
        jLabel89.setText("LKR");

        javax.swing.GroupLayout account_topup_panelLayout = new javax.swing.GroupLayout(account_topup_panel);
        account_topup_panel.setLayout(account_topup_panelLayout);
        account_topup_panelLayout.setHorizontalGroup(
            account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_topup_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(account_topup_panelLayout.createSequentialGroup()
                        .addComponent(jLabel89)
                        .addGap(18, 18, 18)
                        .addComponent(label_accountTopUp_creditBalance))
                    .addComponent(jLabel87))
                .addGap(45, 45, 45))
            .addGroup(account_topup_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_topup_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(label_accountTopUp_back))
                    .addGroup(account_topup_panelLayout.createSequentialGroup()
                        .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(account_topup_panelLayout.createSequentialGroup()
                                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_topup_panelLayout.createSequentialGroup()
                                        .addComponent(button_accountTopUp_onlineBanking, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(button_accountTopUp_debitCredutCard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(account_topup_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel85)
                                        .addGap(18, 18, 18)
                                        .addComponent(text_accountTopUp_amout, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel90)
                                        .addGap(25, 25, 25)))
                                .addComponent(button_accountTopUp_mobilePay, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel84)
                            .addComponent(jLabel86))
                        .addGap(0, 170, Short.MAX_VALUE)))
                .addContainerGap())
        );
        account_topup_panelLayout.setVerticalGroup(
            account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_topup_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel84)
                .addGap(5, 5, 5)
                .addComponent(jLabel87)
                .addGap(18, 18, 18)
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85)
                    .addComponent(text_accountTopUp_amout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel90)
                    .addComponent(label_accountTopUp_creditBalance)
                    .addComponent(jLabel89))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(jLabel86)
                .addGap(19, 19, 19)
                .addGroup(account_topup_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_accountTopUp_debitCredutCard, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_accountTopUp_mobilePay, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(button_accountTopUp_onlineBanking, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(54, 54, 54)
                .addComponent(label_accountTopUp_back)
                .addContainerGap())
        );

        contentPlaceholder_panel.add(account_topup_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        account_history_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_account_history.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Date", "Description", "Remarks", "Expense (LKR)", "Receipt (LKR)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_account_history.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jScrollPane4.setViewportView(table_account_history);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("#count");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel11.setText("Account History");

        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel92.setText("From");

        jLabel93.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel93.setText("To");

        jLabel94.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel94.setText("Credit Balance");

        label_account_history_balance.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_account_history_balance.setText("#balance");

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel96.setText("LKR");

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel97.setText("Search Results");

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel100.setText("Search");

        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel101.setText("Date");

        jTextField19.setText("from");

        jTextField20.setText("to");

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

        jButton18.setText("Back");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jCheckBox5.setText("Expense");

        jCheckBox6.setText("Receipt");

        javax.swing.GroupLayout account_history_panelLayout = new javax.swing.GroupLayout(account_history_panel);
        account_history_panel.setLayout(account_history_panelLayout);
        account_history_panelLayout.setHorizontalGroup(
            account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(account_history_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_history_panelLayout.createSequentialGroup()
                        .addComponent(jLabel100)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel94)
                            .addGroup(account_history_panelLayout.createSequentialGroup()
                                .addComponent(jLabel96)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_account_history_balance)))
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_history_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton18)
                        .addContainerGap())
                    .addGroup(account_history_panelLayout.createSequentialGroup()
                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addGroup(account_history_panelLayout.createSequentialGroup()
                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(account_history_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel97)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel9))
                                    .addComponent(jLabel11)
                                    .addGroup(account_history_panelLayout.createSequentialGroup()
                                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel101)
                                            .addGroup(account_history_panelLayout.createSequentialGroup()
                                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel92)
                                                    .addComponent(jLabel93))
                                                .addGap(18, 18, 18)
                                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField19, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                                                    .addComponent(jTextField20))))
                                        .addGap(48, 48, 48)
                                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel102)
                                            .addGroup(account_history_panelLayout.createSequentialGroup()
                                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel103)
                                                    .addComponent(jLabel104))
                                                .addGap(18, 18, 18)
                                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField21)
                                                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(45, 45, 45)
                                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel105)
                                            .addComponent(jCheckBox5)
                                            .addComponent(jCheckBox6))))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
        );
        account_history_panelLayout.setVerticalGroup(
            account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_history_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(account_history_panelLayout.createSequentialGroup()
                        .addComponent(jLabel94)
                        .addGap(8, 8, 8)
                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel96)
                            .addComponent(label_account_history_balance))
                        .addGap(11, 11, 11)
                        .addComponent(jLabel105)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jCheckBox6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(account_history_panelLayout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jLabel100)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel102)
                            .addComponent(jLabel101))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(account_history_panelLayout.createSequentialGroup()
                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel103)
                                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel104)
                                    .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_history_panelLayout.createSequentialGroup()
                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, account_history_panelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel92)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel93))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(account_history_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel97)
                            .addComponent(jLabel9))))
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton18))
        );

        contentPlaceholder_panel.add(account_history_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        ticket_details_panel.setEnabled(false);
        ticket_details_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel172.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel172.setText("Ticket Details");

        jLabel173.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel173.setText("Ticket ID");

        label_ticket_details_ticketID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_ticket_details_ticketID.setText("#id");

        jLabel175.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel175.setText("Cashier ID");

        label_ticket_details_cashierID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_ticket_details_cashierID.setText("#id");

        button_ticket_details_print.setText("Print");
        button_ticket_details_print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ticket_details_printActionPerformed(evt);
            }
        });

        label_ticket_details_transRecID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_ticket_details_transRecID.setText("#id");

        jLabel178.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel178.setText("Description");

        button_ticket_details_back.setText("Back");
        button_ticket_details_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_ticket_details_backActionPerformed(evt);
            }
        });

        jLabel179.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel179.setText("Class");

        label_ticket_details_class.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_class.setText("STD");

        jLabel181.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel181.setText("Start Date");

        label_ticket_details_fromStation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_fromStation.setText("station");

        jLabel183.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel183.setText("From");

        jLabel184.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel184.setText("End Date");

        label_ticket_details_endDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_endDate.setText("date");

        label_ticket_details_toStation.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_toStation.setText("station");

        jLabel187.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel187.setText("To");

        label_ticket_details_startDate.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_startDate.setText("date");

        jLabel189.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel189.setText("Ticket Type");

        label_ticket_details_ticketType.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_ticketType.setText("Off-peak");

        label_ticket_details_isReturn.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_isReturn.setText("Return");

        jLabel192.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel192.setText("Modes");

        label_ticket_details_train.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_train.setText("--");

        label_ticket_details_bus.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_bus.setText("--");

        label_ticket_details_taxi.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        label_ticket_details_taxi.setText("--");

        jLabel196.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel196.setText("Taxi");

        jLabel197.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel197.setText("Train");

        jLabel198.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel198.setText("Bus");

        jLabel199.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel199.setText("Fee (LKR)");

        label_ticket_details_fee.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_ticket_details_fee.setText("#00.00");

        jLabel137.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel137.setText("Transaction ID");

        jLabel201.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel201.setText("Check-Ins");

        textArea_ticket_details_checkInList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "[In]   Ragama", "[Out] Kelaniya", "[In]   Dematagoda", "[Out] Fort" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        textArea_ticket_details_checkInList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(textArea_ticket_details_checkInList);

        jLabel202.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel202.setText("Distance Travelled (KM)");

        label_ticket_details_distanceTravelled.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_ticket_details_distanceTravelled.setText("#0.0");

        javax.swing.GroupLayout ticket_details_panelLayout = new javax.swing.GroupLayout(ticket_details_panel);
        ticket_details_panel.setLayout(ticket_details_panelLayout);
        ticket_details_panelLayout.setHorizontalGroup(
            ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                        .addGap(22, 22, 22)
                                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel179)
                                            .addComponent(jLabel181)
                                            .addComponent(jLabel184)
                                            .addComponent(jLabel183)
                                            .addComponent(jLabel187))
                                        .addGap(22, 22, 22)
                                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(label_ticket_details_class)
                                            .addComponent(label_ticket_details_toStation)
                                            .addComponent(label_ticket_details_fromStation)
                                            .addComponent(label_ticket_details_endDate)
                                            .addComponent(label_ticket_details_startDate))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 117, Short.MAX_VALUE)
                                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel192)
                                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                                .addComponent(jLabel189)
                                                .addGap(18, 18, 18)
                                                .addComponent(label_ticket_details_ticketType)
                                                .addGap(18, 18, 18)
                                                .addComponent(label_ticket_details_isReturn))
                                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel198)
                                                    .addComponent(jLabel197)
                                                    .addComponent(jLabel196))
                                                .addGap(22, 22, 22)
                                                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(label_ticket_details_taxi)
                                                    .addComponent(label_ticket_details_train)
                                                    .addComponent(label_ticket_details_bus)))
                                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                                .addGap(152, 152, 152)
                                                .addComponent(button_ticket_details_back))))
                                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel202)
                                        .addGap(18, 18, 18)
                                        .addComponent(label_ticket_details_distanceTravelled)
                                        .addGap(75, 75, 75)))
                                .addGap(1, 1, 1))
                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel178)
                                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel199)
                                        .addGap(18, 18, 18)
                                        .addComponent(label_ticket_details_fee)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addGap(18, 18, 18))
                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_ticket_details_print)
                            .addComponent(jLabel172)
                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel173)
                                    .addComponent(jLabel175)
                                    .addComponent(jLabel137))
                                .addGap(18, 18, 18)
                                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(label_ticket_details_ticketID)
                                    .addComponent(label_ticket_details_cashierID)
                                    .addComponent(label_ticket_details_transRecID))))
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                .addGap(133, 133, 133)
                                .addComponent(jLabel201))
                            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                                .addGap(143, 143, 143)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        ticket_details_panelLayout.setVerticalGroup(
            ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ticket_details_panelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel172)
                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel173)
                            .addComponent(label_ticket_details_ticketID))
                        .addGap(18, 18, 18)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel175)
                            .addComponent(label_ticket_details_cashierID))
                        .addGap(21, 21, 21)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_ticket_details_transRecID)
                            .addComponent(jLabel137)))
                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel201)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(23, 23, 23)
                .addComponent(jLabel178)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel179)
                    .addComponent(label_ticket_details_class)
                    .addComponent(jLabel189)
                    .addComponent(label_ticket_details_ticketType)
                    .addComponent(label_ticket_details_isReturn))
                .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel181)
                            .addComponent(label_ticket_details_startDate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_ticket_details_endDate)
                            .addComponent(jLabel184, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_ticket_details_fromStation)
                            .addComponent(jLabel183))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel187)
                            .addComponent(label_ticket_details_toStation))
                        .addGap(18, 18, 18)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel202)
                                .addComponent(label_ticket_details_distanceTravelled))
                            .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel199)
                                .addComponent(label_ticket_details_fee)))
                        .addGap(9, 9, 9)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(button_ticket_details_back)
                            .addComponent(button_ticket_details_print)))
                    .addGroup(ticket_details_panelLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel192)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_ticket_details_bus)
                            .addComponent(jLabel198, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_ticket_details_train)
                            .addComponent(jLabel197))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(ticket_details_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel196)
                            .addComponent(label_ticket_details_taxi))))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        contentPlaceholder_panel.add(ticket_details_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        getContentPane().add(contentPlaceholder_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 500, 370));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void button_profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profileActionPerformed
        // TODO add your handling code here:
        //homescreen_panel.setVisible(false);
        hide_panels();
        profile_panel.setVisible(true);
    }//GEN-LAST:event_button_profileActionPerformed

    private void button_profile_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profile_backActionPerformed
        // TODO add your handling code here:
        profile_panel.setVisible(false);
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_profile_backActionPerformed

    private void button_profile_edit_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profile_edit_cancelActionPerformed
        // TODO add your handling code here:
        profile_edit_panel.setVisible(false);
        profile_panel.setVisible(true);
    }//GEN-LAST:event_button_profile_edit_cancelActionPerformed

    private void button_profile_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profile_editActionPerformed
        // TODO add your handling code here:
        profile_panel.setVisible(false);
        profile_edit_panel.setVisible(true);
    }//GEN-LAST:event_button_profile_editActionPerformed

    private void button_buy_ticket_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_buy_ticket_cancelActionPerformed
        // TODO add your handling code here:
        hide_panels();
        passengerController.updateCurrentPassenger();
        populate_home_screen_panel();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_buy_ticket_cancelActionPerformed

    private void label_accountTopUp_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_label_accountTopUp_backActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_label_accountTopUp_backActionPerformed

    private void button_ticket_details_printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ticket_details_printActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(null, "Ticket printed successfully!", "Message",JOptionPane.INFORMATION_MESSAGE);
        
    }//GEN-LAST:event_button_ticket_details_printActionPerformed

    private void button_ticket_details_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_ticket_details_backActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_ticket_details_backActionPerformed

    private void button_new_journeyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_new_journeyActionPerformed
        // TODO add your handling code here:
        hide_panels();
        buy_ticket_panel.setVisible(true);
    }//GEN-LAST:event_button_new_journeyActionPerformed

    private void button_journey_historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_journey_historyActionPerformed
        // TODO add your handling code here:
        hide_panels();
        journey_history_panel.setVisible(true);
    }//GEN-LAST:event_button_journey_historyActionPerformed

    private void button_topupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_topupActionPerformed
        // TODO add your handling code here:
        hide_panels();
        label_accountTopUp_creditBalance.setText(String.valueOf(currentPassenger.getCreditBalance()));
        account_topup_panel.setVisible(true);
    }//GEN-LAST:event_button_topupActionPerformed

    private void button_share_creditsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_share_creditsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_share_creditsActionPerformed

    private void button_account_historyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_account_historyActionPerformed
        // TODO add your handling code here:
        hide_panels();
        try {
            loadTransactionRecordTableData();
        } catch (Exception e) {
            System.out.println("Passenger Terminal: Could not load the Transaction Records Table");
        }
        account_history_panel.setVisible(true);
    }//GEN-LAST:event_button_account_historyActionPerformed

    private void button_buy_ticket_findFeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_buy_ticket_findFeeActionPerformed
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
        
        
        float fee = passengerController.findFee(ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes);
        if(fee == 0.0f){
            JOptionPane.showMessageDialog(null,"Zero fee! Check the But Ticket Form\nor\nContact System Admin!");
        }
        else{
            label_buy_ticket_ticketFee.setText(String.valueOf(fee));
            label_buy_ticket_ticketFee.setVisible(true);
            button_buy_ticket_buy.setEnabled(true);
        }
        
        
    }//GEN-LAST:event_button_buy_ticket_findFeeActionPerformed

    private void button_buy_ticket_buyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_buy_ticket_buyActionPerformed
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
        
        if(passengerController.buyTicket(currentPassenger.getUserID(),ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes)){
            JOptionPane.showMessageDialog(null, "Ticket has been successfully added to your token", "Message", JOptionPane.INFORMATION_MESSAGE);
            label_buy_ticket_ticketFee.setVisible(false);
            button_buy_ticket_buy.setEnabled(false);
            populate_buy_ticket_panel();
        }else{
            JOptionPane.showMessageDialog(null, "Ticket was not added to the token!\nContact Admin.","Message", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_button_buy_ticket_buyActionPerformed

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

    private void dateChooserCombo_buy_ticket_startDateOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooserCombo_buy_ticket_startDateOnSelectionChange
        // TODO add your handling code here:
        dateChooserCombo_buy_ticket_endDate.setSelectedDate(dateChooserCombo_buy_ticket_startDate.getCurrent());
    }//GEN-LAST:event_dateChooserCombo_buy_ticket_startDateOnSelectionChange

    private void button_homescreen_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_logoutActionPerformed
        // TODO add your handling code here:
        new LogoutConfirm(2).setVisible(true);
    }//GEN-LAST:event_button_homescreen_logoutActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void button_accountTopUp_onlineBankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_accountTopUp_onlineBankingActionPerformed
        // TODO add your handling code here:
        float amount = Float.valueOf(text_accountTopUp_amout.getText());
        String[] type = {"1","Account Topup: COM BANK"};
        if(passengerController.accountTopUp(currentPassenger.getUserID(), type, amount)){
            System.out.println("Passenger Terminal: Online Banking: Account topup success!");
            loadPassengerDetails();
            text_accountTopUp_amout.setText(null);
            label_accountTopUp_creditBalance.setText(String.valueOf(currentPassenger.getCreditBalance()));
        }
        else{
            System.out.println("Passenger Terminal: Online Banking: Account topup failed!");
        }
    }//GEN-LAST:event_button_accountTopUp_onlineBankingActionPerformed

    private void button_accountTopUp_debitCredutCardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_accountTopUp_debitCredutCardActionPerformed
        // TODO add your handling code here:
        float amount = Float.valueOf(text_accountTopUp_amout.getText());
        String[] type = {"2","Account Topup: HNB Debit Card"};
        if(passengerController.accountTopUp(currentPassenger.getUserID(), type, amount)){
            System.out.println("Passenger Terminal: Card payment: Account topup success!");
            loadPassengerDetails();
            text_accountTopUp_amout.setText(null);
            label_accountTopUp_creditBalance.setText(String.valueOf(currentPassenger.getCreditBalance()));
        }
        else{
            System.out.println("Passenger Terminal: Card payment: Account topup failed!");
        }
    }//GEN-LAST:event_button_accountTopUp_debitCredutCardActionPerformed

    private void button_accountTopUp_mobilePayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_accountTopUp_mobilePayActionPerformed
        // TODO add your handling code here:
        float amount = Float.valueOf(text_accountTopUp_amout.getText());
        String[] type = {"3","Account Topup: Mobitel MPay"};
        if(passengerController.accountTopUp(currentPassenger.getUserID(), type, amount)){
            System.out.println("Passenger Terminal: Mobile Pay: Account topup success!");
            loadPassengerDetails();
            text_accountTopUp_amout.setText(null);
            label_accountTopUp_creditBalance.setText(String.valueOf(currentPassenger.getCreditBalance()));
        }
        else{
            System.out.println("Passenger Terminal: Mobile Pay: Account topup failed!");
        }
    }//GEN-LAST:event_button_accountTopUp_mobilePayActionPerformed

    private void button_homescreen_viewTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_viewTicketActionPerformed
        // TODO add your handling code here:
        
        String ticketID = selectedTicketID_active_tickets_table();
        Ticket tempTicket = activeTicketList.get(getSeletedTicketIndex_active_tickets_table(ticketID));
        String passengerID = tempTicket.getPassengerID();
        
        label_ticket_details_ticketID.setText(ticketID);
        
        if(loadTransactionRecordList()) {
            for(TransactionRecord _tr: passengerTransactionRecordList){
                if(_tr.getDescription().equals(ticketID)){
                    label_ticket_details_cashierID.setText(_tr.getCashierID());
                    label_ticket_details_transRecID.setText(_tr.getTransID());
                    label_ticket_details_fee.setText(String.valueOf(_tr.getAmount()));
                    break;
                }
            }
        } else {
            label_ticket_details_cashierID.setText("N/A");
            label_ticket_details_transRecID.setText("N/A");
            label_ticket_details_fee.setText("N/A");
            System.out.println("Passenger Terminal: View Ticket: Empty Trans Rec list for Passenger ID" + passengerID);
        }
        
        label_ticket_details_class.setText(tempTicket.getTicketClass());
        label_ticket_details_fromStation.setText(tempTicket.getFromStop());
        label_ticket_details_toStation.setText(tempTicket.getDestinationStop());
        label_ticket_details_distanceTravelled.setText(String.valueOf(tempTicket.getDistanceTravelled()));
        
        Calendar cal_startDate = tempTicket.getStartDate();
        Calendar cal_endDate = tempTicket.getValidUntilDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = dateFormat.format(cal_startDate.getTime());
        String endDate = dateFormat.format(cal_endDate.getTime());
        label_ticket_details_startDate.setText(startDate);
        label_ticket_details_endDate.setText(endDate);
        
        int transModes[] = tempTicket.getTransMode();
        
        if(transModes[0] == 1){
            label_ticket_details_bus.setText("Yes");
        }
        else{
             label_ticket_details_bus.setText(" -- ");
        }
        
        if(transModes[1] == 1){
            label_ticket_details_train.setText("Yes");
        }
        else{
            label_ticket_details_train.setText(" -- ");
        }
        
        if(transModes[2] == 1){
            label_ticket_details_taxi.setText("Yes");
        }
        else{
            label_ticket_details_taxi.setText(" -- ");
        }
        
        
        //code to check the isReturn value
        if (tempTicket.isReturn()) {
            label_ticket_details_isReturn.setVisible(true);
        }
        else{
            label_ticket_details_isReturn.setVisible(false);
        }
        
        
        //code to display CheckIn list 
        textArea_ticket_details_checkInList.setListData(new String[0]);
        try {
            ArrayList<CheckIn> checkInList = tempTicket.getCheckInList();
            //StringBuilder listItem = new StringBuilder();
            String[] itemList = new String[checkInList.size()];
            String format = "%-8s%s";
            for(int i=0; i< checkInList.size() ; i++){
                CheckIn _checkIn = checkInList.get(i);
                String type, station;
                if(_checkIn.getType() == 1){
                    //listItem.append("[IN]");
                    type = "[IN]";
                    
                }
                else{
                    type = "[OUT]";
                }
                itemList[i] = String.format(format, type, _checkIn.getStopLocation()); 
            }
            textArea_ticket_details_checkInList.setListData(itemList);
            
        } catch (Exception e) {
            System.out.println("Passenger Terminal: View ticket: Empty checkIn List");
            textArea_ticket_details_checkInList.setListData(new String[0]);
        }
        
        hide_panels();
        ticket_details_panel.setVisible(true);

    }//GEN-LAST:event_button_homescreen_viewTicketActionPerformed

    private void table_active_ticketsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_active_ticketsMouseClicked
        // TODO add your handling code here:
        button_homescreen_viewTicket.setEnabled(true);
        if(tableModel_active_tickets.getRowCount() != 0){
            button_homescreen_viewTicket.setEnabled(true);
        }
        else{
             button_homescreen_viewTicket.setEnabled(false);
        }
    }//GEN-LAST:event_table_active_ticketsMouseClicked

    private void button_homescreen_homeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_homescreen_homeActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_homescreen_homeActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PassengerTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }catch(InstantiationException ex) {
            java.util.logging.Logger.getLogger(PassengerTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PassengerTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PassengerTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new PassengerTerminal().setVisible(true);
                passengerTerminal= new PassengerTerminal();
                passengerTerminal.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel account_history_panel;
    private javax.swing.JPanel account_topup_panel;
    private javax.swing.JButton button_accountTopUp_debitCredutCard;
    private javax.swing.JButton button_accountTopUp_mobilePay;
    private javax.swing.JButton button_accountTopUp_onlineBanking;
    private javax.swing.JButton button_account_history;
    private javax.swing.JButton button_buy_ticket_buy;
    private javax.swing.JButton button_buy_ticket_cancel;
    private javax.swing.JButton button_buy_ticket_clear;
    private javax.swing.JButton button_buy_ticket_findFee;
    private javax.swing.JButton button_homescreen_home;
    private javax.swing.JButton button_homescreen_logout;
    private javax.swing.JButton button_homescreen_viewTicket;
    private javax.swing.JButton button_journey_history;
    private javax.swing.JButton button_new_journey;
    private javax.swing.JButton button_profile;
    private javax.swing.JButton button_profile_back;
    private javax.swing.JButton button_profile_edit;
    private javax.swing.JButton button_profile_edit_cancel;
    private javax.swing.JButton button_profile_edit_done;
    private javax.swing.JButton button_share_credits;
    private javax.swing.JButton button_ticket_details_back;
    private javax.swing.JButton button_ticket_details_print;
    private javax.swing.JButton button_topup;
    private javax.swing.JPanel buy_ticket_panel;
    private javax.swing.JCheckBox checkbox_buy_ticket_isBus;
    private javax.swing.JCheckBox checkbox_buy_ticket_isReturn;
    private javax.swing.JCheckBox checkbox_buy_ticket_isTaxi;
    private javax.swing.JCheckBox checkbox_buy_ticket_isTrain;
    private javax.swing.JComboBox comboBox_profile_edit_gender;
    private javax.swing.JComboBox combo_buy_ticket_class;
    private javax.swing.JComboBox combo_buy_ticket_fromStation;
    private javax.swing.JComboBox combo_buy_ticket_toStation;
    private javax.swing.JComboBox combo_buy_ticket_type;
    private javax.swing.JPanel contentPlaceholder_panel;
    private datechooser.beans.DateChooserCombo dateChooserCombo_buy_ticket_endDate;
    private datechooser.beans.DateChooserCombo dateChooserCombo_buy_ticket_startDate;
    private javax.swing.JPanel homescreen_panel;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel172;
    private javax.swing.JLabel jLabel173;
    private javax.swing.JLabel jLabel175;
    private javax.swing.JLabel jLabel178;
    private javax.swing.JLabel jLabel179;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel181;
    private javax.swing.JLabel jLabel183;
    private javax.swing.JLabel jLabel184;
    private javax.swing.JLabel jLabel187;
    private javax.swing.JLabel jLabel189;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel192;
    private javax.swing.JLabel jLabel196;
    private javax.swing.JLabel jLabel197;
    private javax.swing.JLabel jLabel198;
    private javax.swing.JLabel jLabel199;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel201;
    private javax.swing.JLabel jLabel202;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
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
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField13;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField20;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JPanel journey_history_panel;
    private javax.swing.JButton label_accountTopUp_back;
    private javax.swing.JLabel label_accountTopUp_creditBalance;
    private javax.swing.JLabel label_account_history_balance;
    private javax.swing.JLabel label_buy_ticket_accountBalance;
    private javax.swing.JLabel label_buy_ticket_ticketFee;
    private javax.swing.JLabel label_homescreen_creditBalance;
    private javax.swing.JLabel label_homescreen_currentUserID;
    private javax.swing.JLabel label_homescreen_currentUsername;
    private javax.swing.JLabel label_homescreen_scheduledTripCount;
    private javax.swing.JLabel label_homescreen_tokenID;
    private javax.swing.JLabel label_homescreen_tokenStatus;
    private javax.swing.JLabel label_profile_userContactNumber;
    private javax.swing.JLabel label_profile_userEmail;
    private javax.swing.JLabel label_profile_userGender;
    private javax.swing.JLabel label_profile_userName;
    private javax.swing.JLabel label_ticket_details_bus;
    private javax.swing.JLabel label_ticket_details_cashierID;
    private javax.swing.JLabel label_ticket_details_class;
    private javax.swing.JLabel label_ticket_details_distanceTravelled;
    private javax.swing.JLabel label_ticket_details_endDate;
    private javax.swing.JLabel label_ticket_details_fee;
    private javax.swing.JLabel label_ticket_details_fromStation;
    private javax.swing.JLabel label_ticket_details_isReturn;
    private javax.swing.JLabel label_ticket_details_startDate;
    private javax.swing.JLabel label_ticket_details_taxi;
    private javax.swing.JLabel label_ticket_details_ticketID;
    private javax.swing.JLabel label_ticket_details_ticketType;
    private javax.swing.JLabel label_ticket_details_toStation;
    private javax.swing.JLabel label_ticket_details_train;
    private javax.swing.JLabel label_ticket_details_transRecID;
    private javax.swing.JPanel mainMenu_panel;
    private javax.swing.JPanel panel_tokenQRCode1;
    private javax.swing.JPanel panel_tokenQRCode2;
    private javax.swing.JPanel panel_tokenQRCode3;
    private javax.swing.JPanel panel_userPic1;
    private javax.swing.JPanel profile_edit_panel;
    private javax.swing.JPanel profile_panel;
    private javax.swing.JTable table_account_history;
    private javax.swing.JTable table_active_tickets;
    private javax.swing.JTable table_journey_history;
    private javax.swing.JTextField textArea_profile_edit_userAddress;
    private javax.swing.JList textArea_ticket_details_checkInList;
    private javax.swing.JTextField text_accountTopUp_amout;
    private javax.swing.JTextField text_profile_edit_userContactNumber;
    private javax.swing.JTextField text_profile_edit_userEmail;
    private javax.swing.JTextField text_profile_edit_userName;
    private javax.swing.JPanel ticket_details_panel;
    // End of variables declaration//GEN-END:variables
}
