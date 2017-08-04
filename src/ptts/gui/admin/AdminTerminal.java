
package ptts.gui.admin;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import ptts.entities.Administrator;
import ptts.entities.CaseReport;
import ptts.entities.Cashier;
import ptts.entities.Driver;
import ptts.entities.GateOperator;
import ptts.entities.Inspector;
import ptts.entities.Passenger;
import ptts.entities.Ticket;
import ptts.entities.Token;
import ptts.entities.TransactionRecord;
import ptts.entities.User;
import ptts.entities.controllers.PayStationController;
import ptts.entities.managers.AdminManager;
import ptts.entities.managers.CaseReportManager;
import ptts.entities.managers.CashierManager;
import ptts.entities.managers.DriverManager;
import ptts.entities.managers.GateOperatorManager;
import ptts.entities.managers.InspectorManager;
import ptts.entities.managers.PassengerManager;
import ptts.entities.managers.TokenManager;
import ptts.gui.LogoutConfirm;
import ptts.gui.paystation.PayStationTerminal;
import static ptts.gui.paystation.PayStationTerminal.payStationTerminal;
import ptts.main.PublicTransportTicketingSystem;
import static ptts.main.PublicTransportTicketingSystem.adminController;

/**
 *
 * @author Siri@MnS
 */
public class AdminTerminal extends javax.swing.JFrame {

    /**
     * Creates new form AdminTerminal
     */
    //public static JFrame adminTerminal = new AdminTerminal();
    public static JFrame adminTerminal;

    private Administrator currentAdministrator = null;
    private int browseUserType= -1;
    private int totalUserCount;
    
    //temp user lists are preloaded for browsing users
    private ArrayList<Administrator> currentAdminList;
    private ArrayList<Passenger> currentPassengerList;
    private ArrayList<Cashier> currentCashierList;
    private ArrayList<Inspector> currentInspectorList;
    private ArrayList<Driver> currentDriverList;
    private ArrayList<GateOperator> currentGateOperatorList;
    
    //Temp Entity lists for Tokens and Case reports
    private ArrayList<Token> currentTokenList;
    private ArrayList<CaseReport> currentCaseReportList;

    //Browse User Table
    String[] browseUserTableColumns = {"UserID","Name","Status", "Gender", "Contact Number", "Emal", "Address"};
    DefaultTableModel tableModel_browse_user = new DefaultTableModel(browseUserTableColumns, 0);
    TableRowSorter<TableModel> rowSorter_browseUserTable = new TableRowSorter<>(tableModel_browse_user);
    
    
    //Case Report Table
    String[] caseReportsTableColumns = {"Report ID","Inspector ID","Token ID", "Passenger ID", "Station", "Date", "Time"};
    DefaultTableModel tableModel_case_reports = new DefaultTableModel(caseReportsTableColumns, 0);
    TableRowSorter<TableModel> rowSorter_caseReportsTable = new TableRowSorter<>(tableModel_case_reports);
    
    //Token Table
    String[] browseTokensColumns = {"Token ID","Passenger ID","Status", "Ticket Count"};
    DefaultTableModel tableModel_tokens = new DefaultTableModel(browseTokensColumns, 0);
    TableRowSorter<TableModel> rowSorter_tokensTable = new TableRowSorter<>(tableModel_tokens);
    
    
    
    public AdminTerminal() {
        initComponents();
        setLocationRelativeTo(null); //centering the JFrame in the computer display
        hide_panels();
        
        currentAdministrator = adminController.getCurrentAdmin();
        
        //loading temp userlists
        //loadTempUserLists();
        //adminController.loadTransactionRecordList();
        
        
        populate_home_screen_panel();
        populate_profile_and_edit_panels();
        homescreen_panel.setVisible(true);
    }

 
    private void populate_home_screen_panel() {
        label_homescreen_currentUsername.setText(currentAdministrator.getName());
        label_homescreen_currentUserID.setText(currentAdministrator.getUserID());
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(calendar.getTime());
        label_homescreen_systemDate.setText(date);
        
        loadUserLists();
        totalUserCount = 0;
        
        try {
            totalUserCount += currentAdminList.size();
            totalUserCount += currentPassengerList.size();
            totalUserCount += currentCashierList.size();
            totalUserCount += currentInspectorList.size();
            totalUserCount += currentDriverList.size();
            totalUserCount += currentGateOperatorList.size();
            System.out.println("Admin Terminal: Home Screen: Total USer count!" + totalUserCount);
            
        } catch (Exception e) {
            System.out.println("Admin Terminal: Home Screen: Error while get the total user count!");
        }
        
        label_homescreen_userCount.setText(String.valueOf(totalUserCount));
        label_homescreen_totalRevenue.setText(String.valueOf(adminController.calculateTotalRevenue()));
        
        try {
            ArrayList<TransactionRecord> tempTRList = adminController.getTransactionRecordList();

            Calendar calendar_totalRev_startDate = tempTRList.get(0).getDate();
            Calendar calendar_totalRev_endDate = tempTRList.get(tempTRList.size()-1).getDate();
            String totalRevenue_startDate = dateFormat.format(calendar_totalRev_startDate.getTime());
            String totalRevenue_endDate = dateFormat.format(calendar_totalRev_endDate.getTime());
            
            label_homescreen_totRev_startDate.setText(totalRevenue_startDate);
            label_homescreen_totRev_endDate.setText(totalRevenue_endDate);
            
        } catch (Exception e) {
            label_homescreen_totRev_startDate.setText("yy/mm/dd");
            label_homescreen_totRev_endDate.setText("yy/mm/dd");
            System.out.println("Admin Terminal: Empty Transaction Record list!");
        }
        
        try {
            adminController.loadTransactionRecordList();
            Calendar tempDate = adminController.getTransactionRecordList().get(0).getDate();
            tempDate.add(Calendar.DATE, -1);
            dateChooser_homescreen_startDate.setMinDate(tempDate);
            dateChooser_homescreen_startDate.setMaxDate(Calendar.getInstance());
        } catch (Exception e) {
            dateChooser_homescreen_startDate.setEnabled(false);
            dateChooser_homescreen_endDate.setEnabled(false);
            System.out.println("Admin Terminal: Getting the min date: Empty Transaction Record list!");
        }
        
        dateChooser_homescreen_endDate.setMaxDate(Calendar.getInstance());

    }
    
    private void populate_profile_and_edit_panels() {
        label_profile_userName.setText(currentAdministrator.getName());
        if (currentAdministrator.getGender() == 1) {
            label_profile_userGender.setText("Male");
        }
        else{
            label_profile_userGender.setText("Male");
        }
        label_profile_userContactNumber.setText(currentAdministrator.getContactNumber());
        label_profile_userEmail.setText(currentAdministrator.getEmail());
        label_profile_userAddress.setText(currentAdministrator.getAddress());
        
        text_profile_edit_userName.setText(currentAdministrator.getName());
        if (currentAdministrator.getGender() == 1) {
            comboBox_gender.setSelectedIndex(0);
        }
        else{
            comboBox_gender.setSelectedIndex(1);
        }
        text_profile_edit_userContactNumber.setText(currentAdministrator.getContactNumber());
        text_profile_edit_userEmail.setText(currentAdministrator.getEmail());
        textArea_profile_edit_userAddress.setText(currentAdministrator.getAddress());
        
        
    }
    
    private void populate_create_token_browse_token_panel(){
        //load list of passengers who do not have tokens 
        ArrayList<Passenger> passengersWithNoToken = getPassengersWithNoToken();

        if (passengersWithNoToken.size() != 0) {
            String[] passengersWithNoTokenList = new String[passengersWithNoToken.size()];
            for (int i = 0; i < passengersWithNoToken.size(); i++) {
                passengersWithNoTokenList[i] = passengersWithNoToken.get(i).getUserID();
            }
            
            comboBox_create_token_passengerList.setModel(new DefaultComboBoxModel(passengersWithNoTokenList));
            button_create_token.setEnabled(true);
        }
        else{
            button_create_token.setEnabled(false);
            String[] passengersWithNoTokenList = {"Empty List"};
            comboBox_create_token_passengerList.setModel(new DefaultComboBoxModel(passengersWithNoTokenList));
        }
        
        //generating TokenID
        int tokenCount;
        if (currentTokenList != null) {
            tokenCount = currentTokenList.size();
        }
        else{
            tokenCount =0;
        }
        StringBuilder sb = new StringBuilder("T");
        sb.append(Integer.toString(tokenCount+1));
        
        label_create_token_newTokenID.setText(sb.toString());
    }
    
    
    private void populate_browse_user_panel(int userType) {
        
        button_browse_user_delete.setEnabled(false);
        button_browse_user_update.setEnabled(false);
        
        switch(userType){
            
            case 1:
                label_browse_userType.setText("Administrator");
                label_browse_user_userCount.setText(Integer.toString(currentAdminList.size()));
                loadBrowseUserTableData();
                break;
                
            case 2:
                label_browse_userType.setText("Passenger");
                label_browse_user_userCount.setText(Integer.toString(currentPassengerList.size()));
                loadBrowseUserTableData();
                break;
                
            case 3:
                label_browse_userType.setText("Cashier");
                label_browse_user_userCount.setText(Integer.toString(currentCashierList.size()));
                loadBrowseUserTableData();
                break;
                
            case 4:
                label_browse_userType.setText("Inspector");
                label_browse_user_userCount.setText(Integer.toString(currentInspectorList.size()));
                loadBrowseUserTableData();
                break;
                
            case 5:
                label_browse_userType.setText("Driver");
                label_browse_user_userCount.setText(Integer.toString(currentDriverList.size()));
                loadBrowseUserTableData();
                break;
                
            case 6:
                label_browse_userType.setText("Gate Operator");
                label_browse_user_userCount.setText(Integer.toString(currentGateOperatorList.size()));
                loadBrowseUserTableData();
                break;
        }
    }
    
    private void populate_user_management_panel(){
        
        try {
            label_mgt_users_adminCount.setText(String.valueOf(currentAdminList.size()));
        } catch (Exception e) {
            label_mgt_users_adminCount.setText("0");
            System.out.println("User Management panel: Empty Admin list!");
        }
        
        try {
            label_mgt_users_passengerCount.setText(String.valueOf(currentPassengerList.size()));
            System.out.println("User Management panel: User Count" +totalUserCount);
        } catch (Exception e2) {
            label_mgt_users_passengerCount.setText("0");
            System.out.println("User Management panel: Empty Passenger list!");
        }
        
        try {
            label_mgt_users_cashierCount.setText(String.valueOf(currentCashierList.size()));
            
        } catch (Exception e) {
            label_mgt_users_cashierCount.setText("0");
            System.out.println("User Management panel: Empty Cashier list!");
        }
        
        try {
            label_mgt_users_inspectorCount.setText(String.valueOf(currentInspectorList.size()));
        } catch (Exception e) {
            label_mgt_users_inspectorCount.setText("0");
            System.out.println("User Management panel: Empty Inspector list!");
        }
        
        try {
            label_mgt_users_driverCount.setText(String.valueOf(currentDriverList.size()));
        } catch (Exception e) {
            label_mgt_users_driverCount.setText("0");
            System.out.println("User Management panel: Empty Driver list!");
        }
        
        try {
            label_mgt_users_gateOperatorCount.setText(String.valueOf(currentGateOperatorList.size()));
        } catch (Exception e) {
            label_mgt_users_gateOperatorCount.setText("0");
            System.out.println("User Management panel: Empty Gate Operator list!");
        }
    
    }
    
    private void clearFields_create_user_panel() {
        comboBox_userType.setSelectedIndex(0);
        text_userID.setText("");
        text_username.setText("");
        text_userPassword.setText("");
        checkBox_userIsActive.setSelected(false);
        comboBox_gender.setSelectedIndex(0);
        text_userContactNumber.setText("");
        text_userEmail.setText("");
        text_userAddress.setText("");
    }
    
    private void hide_panels(){
        homescreen_panel.setVisible(false);
        profile_panel.setVisible(false);
        profile_edit_panel.setVisible(false);
        
        management_users_panel.setVisible(false);
        create_user_panel.setVisible(false);
        browse_user_panel.setVisible(false);
        update_user_panel.setVisible(false);
        
        token_panel.setVisible(false);
        
        fare_revenue_panel.setVisible(false);
        network_usage_panel.setVisible(false);
        passenger_info_panel.setVisible(false);
        browse_case_reports_panel.setVisible(false);
    }
    
    
    private ArrayList<Passenger> getPassengersWithNoToken(){
        ArrayList<Passenger> passengersWithNoToken = new ArrayList<>();
        for(Passenger _passenger: currentPassengerList){
            if(_passenger.getToken() == null){
                passengersWithNoToken.add(_passenger);
            }
        }
        return passengersWithNoToken;
    }
    
    
    
    // Browse Users Table methods -------------- start
    
    private void loadUserLists(){
        currentAdminList = AdminManager.getAdminList();
        currentPassengerList = PassengerManager.getPassengerList();
        currentCashierList = CashierManager.getCashierList();
        currentInspectorList = InspectorManager.getInspectorList();
        currentDriverList = DriverManager.getDriverList();
        currentGateOperatorList = GateOperatorManager.getGateOperatorList();
    }
    
    private void loadBrowseUserTableData(){
        
        ArrayList<User> tempUserList = new ArrayList<>();
        tableModel_browse_user.setRowCount(0);
        
        switch(browseUserType){
            case 1:
                for(int i=0; i< currentAdminList.size(); i++){
                    tempUserList.add((User)currentAdminList.get(i));
                }
                
                break;
                
            case 2:
                for(int i=0; i< currentPassengerList.size(); i++){
                    tempUserList.add((User)currentPassengerList.get(i));
                }
                break;
                
            case 3:
                for(int i=0; i< currentCashierList.size(); i++){
                    tempUserList.add((User)currentCashierList.get(i));
                }
                
                break;
                
            case 4:
                for(int i=0; i< currentInspectorList.size(); i++){
                    tempUserList.add((User)currentInspectorList.get(i));
                }
                break;
                
            case 5:
                for(int i=0; i< currentDriverList.size(); i++){
                    tempUserList.add((User)currentDriverList.get(i));
                }
                
                break;
                
            case 6:
                for(int i=0; i< currentGateOperatorList.size(); i++){
                    tempUserList.add((User)currentGateOperatorList.get(i));
                }
                break;
        }
        
        
        table_browse_users.setModel(tableModel_browse_user);
        table_browse_users.setRowSorter(rowSorter_browseUserTable);
        
        for(int i=0; i< tempUserList.size() ; i++){
            User tempUser = tempUserList.get(i);
            String userID = tempUser.getUserID() ;
            String isActive;
            if (tempUser.isActive()) {
                isActive = "YES";
            }
            else
            {
                isActive = "NO";
            }
            String name = tempUser.getName();
            String gender;
            if (tempUser.getGender() == 1) {
                gender = "Male";
            }
            else
            {
                gender = "Female";
            }
            String contactNumber = tempUser.getContactNumber();
            String email = tempUser.getEmail();
            String address = tempUser.getAddress();
            
            Object[] data={userID, name, isActive, gender, contactNumber,email, address};
            tableModel_browse_user.addRow(data);

        }
        
    }
    
    private String selectedUserID_browse_user_table() {
      
        String selectedUserID= null;
        
        //retrieving the selected row index
        int row = table_browse_users.getSelectedRow();

        //if a single row is selected from the table, get selectedBookNo
        if (table_browse_users.getRowSelectionAllowed()) {
            selectedUserID =  table_browse_users.getValueAt(row,0).toString();
        }
        
        return selectedUserID;
    }

    private int getSeletedUserIndex_browse_user_table(String selectedUserID) {
        int userIndex = -1;
        switch(browseUserType){
            case 1:
                for(int i=0; i<currentAdminList.size(); i++){
                    if(currentAdminList.get(i).getUserID().equals(selectedUserID)){
                        userIndex = i;
                    }
                }
                break;
            case 2:
                for(int i=0; i<currentPassengerList.size(); i++){
                    if(currentPassengerList.get(i).getUserID().equals(selectedUserID)){
                        userIndex = i;
                    }
                }
                break;
        }
        return userIndex;
    }
    
    // Browse Users Table methods -------------- end
    
    
    
    // Case report Table methods -------------- start
    
    private boolean loadCaseReportList(){
        try {
            currentCaseReportList = CaseReportManager.getCaseReportList();
            label_case_reports_count.setText(String.valueOf(currentCaseReportList.size()));
            return true;
            
        } catch (Exception e) {
            label_case_reports_count.setText("0");
            System.out.println("Pay station: Null Case Report List!");
            return false;
        }
    }
    
    private void loadCaseReportsTableData(){

        table_case_reports.setModel(tableModel_case_reports);
        table_case_reports.setRowSorter(rowSorter_caseReportsTable);
        
        tableModel_case_reports.setRowCount(0);
        
        if (loadCaseReportList()) {
            for (int i = 0; i < currentCaseReportList.size(); i++) {
                
                CaseReport report = currentCaseReportList.get(i);
                
                String reportID = report.getReportID();
                String inspectorID = report.getInspectorID();
                String tokenID = report.getTokenID();
                String passengerID = report.getPassengerID();
                String station = report.getStation();
                
                Calendar calendar_date_time = report.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
                
                String date = dateFormat.format(calendar_date_time.getTime());
                String time = timeFormat.format(calendar_date_time.getTime());
                
                
                Object[] data = {reportID, inspectorID, tokenID, passengerID, station, date, time};
                tableModel_case_reports.addRow(data);
                
            }
        }
        
    }
    
    // Case report Table methods -------------- end
    
    
    
    // Token Table methods -------------- start
    
    private boolean loadTokenList(){
        try {
            currentTokenList = TokenManager.getTokenList();
            label_browse_tokens_count.setText(String.valueOf(currentTokenList.size()));
            return true;
            
        } catch (Exception e) {
            label_browse_tokens_count.setText("0");
            System.out.println("Pay station: Null Token List!");
            return false;
        }
    }
    
    private void loadTokenTableData(){

        table_tokens.setModel(tableModel_tokens);
        table_tokens.setRowSorter(rowSorter_tokensTable);
        tableModel_tokens.setRowCount(0);
        
        if (loadTokenList()) {
            for (int i = 0; i < currentTokenList.size(); i++) {
                
                Token token = currentTokenList.get(i);
                
                String tokenID = token.getTokenID();
                String passengerID = token.getPassengerID();
                String status;
                ArrayList<Ticket> ticketList;
                try {
                    ticketList = token.getActiveTicketList();
                    System.out.println("Admin Terminal: Browse Token: Token "+ token.getTokenID()+" count: "+ ticketList.size());
                } catch (Exception e) {
                    ticketList =  new ArrayList<>();
                    System.out.println("Admin Terminal: Browse Token: Empty ticket list for "+token.getTokenID());
                }
                
                if(token.isActive()){
                    status = "Active";
                }
                else{
                    status = "Inactive";
                }
                
                Object[] data = {tokenID, passengerID, status, String.valueOf(ticketList.size())};
                
                tableModel_tokens.addRow(data);
                
            }
        }
        
    }
     
    // Token Table methods -------------- end
  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        mainMenu_panel = new javax.swing.JPanel();
        button_profile = new javax.swing.JButton();
        button_users = new javax.swing.JButton();
        button_token = new javax.swing.JButton();
        button_network_usage = new javax.swing.JButton();
        button_individual_passenger = new javax.swing.JButton();
        button_pay_station = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        button_create_user = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        contentHolder_panel = new javax.swing.JPanel();
        homescreen_panel = new javax.swing.JPanel();
        label_homescreen_currentUsername = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        label_homescreen_currentUserID = new javax.swing.JLabel();
        panel_userPic = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        label_homescreen_userCount = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        label_homescreen_systemDate = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        label_homescreen_totalRevenue = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        label_homescreen_totRev_startDate = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        label_homescreen_totRev_endDate = new javax.swing.JLabel();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        dateChooser_homescreen_startDate = new datechooser.beans.DateChooserCombo();
        dateChooser_homescreen_endDate = new datechooser.beans.DateChooserCombo();
        jLabel66 = new javax.swing.JLabel();
        label_homescreen_custom_revenue = new javax.swing.JLabel();
        browse_case_reports_panel = new javax.swing.JPanel();
        jScrollPane9 = new javax.swing.JScrollPane();
        table_case_reports = new javax.swing.JTable();
        jLabel118 = new javax.swing.JLabel();
        jLabel122 = new javax.swing.JLabel();
        jLabel123 = new javax.swing.JLabel();
        label_case_reports_count = new javax.swing.JLabel();
        jLabel124 = new javax.swing.JLabel();
        jLabel125 = new javax.swing.JLabel();
        jLabel127 = new javax.swing.JLabel();
        jLabel128 = new javax.swing.JLabel();
        jTextField23 = new javax.swing.JTextField();
        jTextField24 = new javax.swing.JTextField();
        button_browse_user_back2 = new javax.swing.JButton();
        jLabel130 = new javax.swing.JLabel();
        jTextField25 = new javax.swing.JTextField();
        jTextField26 = new javax.swing.JTextField();
        jLabel131 = new javax.swing.JLabel();
        jLabel132 = new javax.swing.JLabel();
        label_browse_user_userCount3 = new javax.swing.JLabel();
        jLabel135 = new javax.swing.JLabel();
        jLabel136 = new javax.swing.JLabel();
        jLabel137 = new javax.swing.JLabel();
        dateChooserCombo2 = new datechooser.beans.DateChooserCombo();
        dateChooserCombo1 = new datechooser.beans.DateChooserCombo();
        profile_edit_panel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        panel_tokenQRCode2 = new javax.swing.JPanel();
        button_profiel_edit_done = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jButton12 = new javax.swing.JButton();
        button_profile_edit_cancel = new javax.swing.JButton();
        text_profile_edit_userName = new javax.swing.JTextField();
        text_profile_edit_userContactNumber = new javax.swing.JTextField();
        text_profile_edit_userEmail = new javax.swing.JTextField();
        combo_profile_edit_userGender = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea_profile_edit_userAddress = new javax.swing.JTextArea();
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
        label_profile_userAddress = new javax.swing.JLabel();
        button_profile_back = new javax.swing.JButton();
        create_user_panel = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        panel_tokenQRCode3 = new javax.swing.JPanel();
        button_addUser = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jButton35 = new javax.swing.JButton();
        jButton36 = new javax.swing.JButton();
        text_username = new javax.swing.JTextField();
        text_userContactNumber = new javax.swing.JTextField();
        text_userEmail = new javax.swing.JTextField();
        text_userAddress = new javax.swing.JTextField();
        comboBox_gender = new javax.swing.JComboBox();
        jLabel41 = new javax.swing.JLabel();
        text_userID = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        comboBox_userType = new javax.swing.JComboBox();
        jLabel47 = new javax.swing.JLabel();
        text_userPassword = new javax.swing.JTextField();
        checkBox_userIsActive = new javax.swing.JCheckBox();
        management_users_panel = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        button_mgt_users_administrator = new javax.swing.JButton();
        button_mgt_users_passenger = new javax.swing.JButton();
        jButton27 = new javax.swing.JButton();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jButton30 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        label_mgt_users_adminCount = new javax.swing.JLabel();
        label_mgt_users_passengerCount = new javax.swing.JLabel();
        label_mgt_users_inspectorCount = new javax.swing.JLabel();
        label_mgt_users_cashierCount = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        label_mgt_users_passengerCount_active = new javax.swing.JLabel();
        label_mgt_users_cashierCount_active = new javax.swing.JLabel();
        label_mgt_users_inspectorCount_active = new javax.swing.JLabel();
        jLabel133 = new javax.swing.JLabel();
        jLabel134 = new javax.swing.JLabel();
        label_mgt_users_driverCount = new javax.swing.JLabel();
        label_mgt_users_gateOperatorCount = new javax.swing.JLabel();
        label_mgt_users_driverCount_active = new javax.swing.JLabel();
        label_mgt_users_gateOperatorCount_active = new javax.swing.JLabel();
        browse_user_panel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_browse_users = new javax.swing.JTable();
        jLabel40 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        label_browse_user_userCount = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        label_browse_userType = new javax.swing.JLabel();
        jLabel70 = new javax.swing.JLabel();
        jTextField9 = new javax.swing.JTextField();
        jTextField10 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        button_browse_user_back = new javax.swing.JButton();
        button_browse_user_update = new javax.swing.JButton();
        button_browse_user_delete = new javax.swing.JButton();
        jLabel94 = new javax.swing.JLabel();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jLabel95 = new javax.swing.JLabel();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jLabel96 = new javax.swing.JLabel();
        update_user_panel = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        panel_tokenQRCode4 = new javax.swing.JPanel();
        button_user_update_update = new javax.swing.JButton();
        jLabel45 = new javax.swing.JLabel();
        jLabel97 = new javax.swing.JLabel();
        jLabel98 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        button_user_update_cancel = new javax.swing.JButton();
        text_update_user_userName = new javax.swing.JTextField();
        text_update_user_contactNumber = new javax.swing.JTextField();
        text_update_user_userEmail = new javax.swing.JTextField();
        combo_update_user_userGender = new javax.swing.JComboBox();
        jScrollPane6 = new javax.swing.JScrollPane();
        textArea_update_user_userAddress = new javax.swing.JTextArea();
        label_update_user_userType = new javax.swing.JLabel();
        label_update_user_userID = new javax.swing.JLabel();
        checkbox_update_user_userIsActive = new javax.swing.JCheckBox();
        jLabel32 = new javax.swing.JLabel();
        passwordField_update_user_userPassword = new javax.swing.JPasswordField();
        token_panel = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        token_create_panel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        comboBox_create_token_passengerList = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        label_create_token_newTokenID = new javax.swing.JLabel();
        button_create_token = new javax.swing.JButton();
        button_create_token_back = new javax.swing.JButton();
        browse_token_panel = new javax.swing.JPanel();
        browse_user_panel1 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        table_tokens = new javax.swing.JTable();
        jLabel110 = new javax.swing.JLabel();
        jLabel111 = new javax.swing.JLabel();
        jLabel112 = new javax.swing.JLabel();
        label_browse_tokens_count = new javax.swing.JLabel();
        jLabel113 = new javax.swing.JLabel();
        jLabel114 = new javax.swing.JLabel();
        jLabel115 = new javax.swing.JLabel();
        jLabel116 = new javax.swing.JLabel();
        jLabel117 = new javax.swing.JLabel();
        jTextField19 = new javax.swing.JTextField();
        jTextField20 = new javax.swing.JTextField();
        jCheckBox7 = new javax.swing.JCheckBox();
        button_browse_user_back1 = new javax.swing.JButton();
        button_browse_user_update1 = new javax.swing.JButton();
        jLabel119 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jTextField22 = new javax.swing.JTextField();
        jLabel120 = new javax.swing.JLabel();
        jLabel121 = new javax.swing.JLabel();
        token_detail_panel = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel99 = new javax.swing.JLabel();
        jLabel100 = new javax.swing.JLabel();
        jLabel101 = new javax.swing.JLabel();
        label_homescreen_currentUserID1 = new javax.swing.JLabel();
        jLabel102 = new javax.swing.JLabel();
        jLabel103 = new javax.swing.JLabel();
        jLabel104 = new javax.swing.JLabel();
        jLabel105 = new javax.swing.JLabel();
        panel_tokenQRCode5 = new javax.swing.JPanel();
        jLabel106 = new javax.swing.JLabel();
        jLabel107 = new javax.swing.JLabel();
        jLabel108 = new javax.swing.JLabel();
        jLabel109 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        fare_revenue_panel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_browse_users1 = new javax.swing.JTable();
        jLabel49 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jCheckBox2 = new javax.swing.JCheckBox();
        jButton16 = new javax.swing.JButton();
        network_usage_panel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        table_browse_users2 = new javax.swing.JTable();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jTextField13 = new javax.swing.JTextField();
        jTextField14 = new javax.swing.JTextField();
        jCheckBox3 = new javax.swing.JCheckBox();
        jButton18 = new javax.swing.JButton();
        passenger_info_panel = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        table_browse_users3 = new javax.swing.JTable();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel92 = new javax.swing.JLabel();
        jLabel93 = new javax.swing.JLabel();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jCheckBox4 = new javax.swing.JCheckBox();
        jButton19 = new javax.swing.JButton();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("PTTS v1.0  Administrator Terminal ");
        setMinimumSize(new java.awt.Dimension(650, 420));
        setResizable(false);

        mainMenu_panel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        button_profile.setText("Profile");
        button_profile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_profileActionPerformed(evt);
            }
        });

        button_users.setText("Users");
        button_users.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_usersActionPerformed(evt);
            }
        });

        button_token.setText("Tokens");
        button_token.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_tokenActionPerformed(evt);
            }
        });

        button_network_usage.setText("<html><center>Network<br>Usage</center>");
        button_network_usage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_network_usageActionPerformed(evt);
            }
        });

        button_individual_passenger.setText("<html><center>Individual<br>Passenger<br>Info</center>");
        button_individual_passenger.setMargin(new java.awt.Insets(2, 5, 2, 5));
        button_individual_passenger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_individual_passengerActionPerformed(evt);
            }
        });

        button_pay_station.setText("<html><center>Pay<br>Station</center>");
        button_pay_station.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_pay_stationActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setText("Management");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setText("Statistics");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel3.setText("Other");

        button_create_user.setText("<html><center>Create<br>User</center>");
        button_create_user.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_create_userActionPerformed(evt);
            }
        });

        jButton5.setText("<html><center>Case<br>Reports</center>");
        jButton5.setMargin(new java.awt.Insets(2, 1, 2, 1));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout mainMenu_panelLayout = new javax.swing.GroupLayout(mainMenu_panel);
        mainMenu_panel.setLayout(mainMenu_panelLayout);
        mainMenu_panelLayout.setHorizontalGroup(
            mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainMenu_panelLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
            .addGroup(mainMenu_panelLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                        .addComponent(jLabel3)
                        .addGroup(mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(button_pay_station, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_network_usage, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_users, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(button_token, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(button_profile, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_create_user, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(button_individual_passenger, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        mainMenu_panelLayout.setVerticalGroup(
            mainMenu_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainMenu_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(button_profile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_create_user, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_users)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(button_token)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button_network_usage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(button_individual_passenger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jLabel3)
                .addGap(18, 18, 18)
                .addComponent(button_pay_station, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        contentHolder_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        label_homescreen_currentUsername.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_homescreen_currentUsername.setText("Hi, User");

        jButton9.setText("Logout");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Admin ID: ");

        label_homescreen_currentUserID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_homescreen_currentUserID.setText("#userID");

        panel_userPic.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panel_userPicLayout = new javax.swing.GroupLayout(panel_userPic);
        panel_userPic.setLayout(panel_userPicLayout);
        panel_userPicLayout.setHorizontalGroup(
            panel_userPicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panel_userPicLayout.setVerticalGroup(
            panel_userPicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel7.setText("Active Users:");

        label_homescreen_userCount.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_homescreen_userCount.setText("numOfActiveUsers");

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel9.setText("System Date:");

        label_homescreen_systemDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_homescreen_systemDate.setText("yy/mm/dd");

        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel11.setText("Custom Range");

        jLabel12.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel12.setText("From");

        label_homescreen_totalRevenue.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_homescreen_totalRevenue.setText("totalRevenue");

        jLabel14.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel14.setText("Total Revenue:");

        label_homescreen_totRev_startDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_homescreen_totRev_startDate.setText("yy/mm/dd");

        jLabel61.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel61.setText("To");

        label_homescreen_totRev_endDate.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_homescreen_totRev_endDate.setText("yy/mm/dd");

        jLabel63.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel63.setText("Start Date");

        jLabel64.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel64.setText("End Date");

        dateChooser_homescreen_startDate.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
            public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
                dateChooser_homescreen_startDateOnSelectionChange(evt);
            }
        });

        dateChooser_homescreen_endDate.addSelectionChangedListener(new datechooser.events.SelectionChangedListener() {
            public void onSelectionChange(datechooser.events.SelectionChangedEvent evt) {
                dateChooser_homescreen_endDateOnSelectionChange(evt);
            }
        });

        jLabel66.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel66.setText("Revenue:");

        label_homescreen_custom_revenue.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        label_homescreen_custom_revenue.setText(" -- ");

        javax.swing.GroupLayout homescreen_panelLayout = new javax.swing.GroupLayout(homescreen_panel);
        homescreen_panel.setLayout(homescreen_panelLayout);
        homescreen_panelLayout.setHorizontalGroup(
            homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homescreen_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(10, 10, 10)
                                .addComponent(label_homescreen_totRev_startDate)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel61)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_homescreen_totRev_endDate))
                            .addComponent(jLabel11)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel63)
                                    .addComponent(jLabel64))
                                .addGap(18, 18, 18)
                                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dateChooser_homescreen_endDate, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                    .addComponent(dateChooser_homescreen_startDate, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel66)
                                .addGap(18, 18, 18)
                                .addComponent(label_homescreen_custom_revenue)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_homescreen_currentUsername)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_homescreen_currentUserID))
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(label_homescreen_userCount))
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(label_homescreen_totalRevenue)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 133, Short.MAX_VALUE)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel9)
                            .addGroup(homescreen_panelLayout.createSequentialGroup()
                                .addComponent(panel_userPic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton9))
                            .addComponent(label_homescreen_systemDate)))))
        );
        homescreen_panelLayout.setVerticalGroup(
            homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(homescreen_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_userPic, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_homescreen_currentUsername)
                            .addComponent(jButton9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(label_homescreen_currentUserID, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(33, 33, 33)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(label_homescreen_userCount, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(label_homescreen_systemDate))
                .addGap(54, 54, 54)
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(label_homescreen_totalRevenue))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel61)
                    .addComponent(label_homescreen_totRev_endDate)
                    .addComponent(label_homescreen_totRev_startDate))
                .addGap(43, 43, 43)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(homescreen_panelLayout.createSequentialGroup()
                        .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel63)
                            .addComponent(dateChooser_homescreen_startDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13)
                        .addComponent(jLabel64))
                    .addComponent(dateChooser_homescreen_endDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(homescreen_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel66)
                    .addComponent(label_homescreen_custom_revenue))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        contentHolder_panel.add(homescreen_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 440));

        browse_case_reports_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_case_reports.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Report ID", "Inspector ID", "Token ID", "Passenger ID", "Station", "Date", "Time"
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
        table_case_reports.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table_case_reports.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_case_reportsMouseClicked(evt);
            }
        });
        jScrollPane9.setViewportView(table_case_reports);

        jLabel118.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel118.setText("resultCount");

        jLabel122.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel122.setText("Browse Case Reports");

        jLabel123.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel123.setText("Total");

        label_case_reports_count.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_case_reports_count.setText("#reportCount");

        jLabel124.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel124.setText("Inspector ID");

        jLabel125.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel125.setText("Token ID");

        jLabel127.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel127.setText("Search Results");

        jLabel128.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel128.setText("Filter");

        jTextField23.setText("userName");
        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });

        jTextField24.setText("userID");
        jTextField24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField24ActionPerformed(evt);
            }
        });

        button_browse_user_back2.setText("Back");
        button_browse_user_back2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browse_user_back2ActionPerformed(evt);
            }
        });

        jLabel130.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel130.setText("Station");

        jTextField25.setText("userID");
        jTextField25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField25ActionPerformed(evt);
            }
        });

        jTextField26.setText("userName");
        jTextField26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField26ActionPerformed(evt);
            }
        });

        jLabel131.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel131.setText("Passenger ID");

        jLabel132.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel132.setText("Frequency");

        label_browse_user_userCount3.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_browse_user_userCount3.setText("#frequency");

        jLabel135.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel135.setText("Date");

        jLabel136.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel136.setText("From");

        jLabel137.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel137.setText("To");

        javax.swing.GroupLayout browse_case_reports_panelLayout = new javax.swing.GroupLayout(browse_case_reports_panel);
        browse_case_reports_panel.setLayout(browse_case_reports_panelLayout);
        browse_case_reports_panelLayout.setHorizontalGroup(
            browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(button_browse_user_back2)
                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, browse_case_reports_panelLayout.createSequentialGroup()
                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel127)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel118))
                                    .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                                .addComponent(jLabel124)
                                                .addGap(5, 5, 5)
                                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTextField23)
                                                    .addComponent(jTextField24, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE)))
                                            .addComponent(jLabel125))
                                        .addGap(18, 18, 18)
                                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel136)
                                            .addComponent(jLabel137)
                                            .addComponent(jLabel135))
                                        .addGap(18, 18, 18)
                                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(28, 28, 28)
                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel131)
                                    .addComponent(jLabel130))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField26, javax.swing.GroupLayout.DEFAULT_SIZE, 68, Short.MAX_VALUE)
                                    .addComponent(jTextField25))))
                        .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                            .addComponent(jLabel122)
                            .addGap(126, 126, 126)
                            .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                    .addComponent(jLabel123)
                                    .addGap(37, 37, 37)
                                    .addComponent(label_case_reports_count))
                                .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                    .addComponent(jLabel132)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(label_browse_user_userCount3)))))
                    .addComponent(jLabel128))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        browse_case_reports_panelLayout.setVerticalGroup(
            browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browse_case_reports_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel122)
                    .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel123)
                            .addComponent(label_case_reports_count))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel132)
                            .addComponent(label_browse_user_userCount3))))
                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel128)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel124))
                                .addGap(5, 5, 5)
                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel125)
                                    .addComponent(jTextField23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel130))
                                .addGap(5, 5, 5)
                                .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel131)
                                    .addComponent(jTextField26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(27, 27, 27)
                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel127)
                            .addComponent(jLabel118))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(button_browse_user_back2)
                        .addGap(14, 14, 14))
                    .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(jLabel135)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(browse_case_reports_panelLayout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(jLabel136)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, browse_case_reports_panelLayout.createSequentialGroup()
                                .addComponent(dateChooserCombo1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addGroup(browse_case_reports_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel137)
                            .addComponent(dateChooserCombo2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        contentHolder_panel.add(browse_case_reports_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 390));

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

        button_profiel_edit_done.setText("Done");
        button_profiel_edit_done.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_profiel_edit_doneActionPerformed(evt);
            }
        });

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

        combo_profile_edit_userGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        jScrollPane2.setPreferredSize(new java.awt.Dimension(173, 54));

        textArea_profile_edit_userAddress.setColumns(20);
        textArea_profile_edit_userAddress.setLineWrap(true);
        textArea_profile_edit_userAddress.setRows(5);
        textArea_profile_edit_userAddress.setText("userAddress");
        textArea_profile_edit_userAddress.setPreferredSize(new java.awt.Dimension(173, 54));
        jScrollPane2.setViewportView(textArea_profile_edit_userAddress);

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
                        .addComponent(button_profiel_edit_done))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_edit_panelLayout.createSequentialGroup()
                        .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel23)
                            .addComponent(jLabel25)
                            .addComponent(jLabel33)
                            .addComponent(jLabel36)
                            .addComponent(jLabel37))
                        .addGap(18, 18, 18)
                        .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(text_profile_edit_userName)
                                .addComponent(text_profile_edit_userContactNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                .addComponent(text_profile_edit_userEmail)
                                .addComponent(combo_profile_edit_userGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE)
                        .addComponent(button_profile_edit_cancel))
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
                    .addComponent(button_profiel_edit_done))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_tokenQRCode2, javax.swing.GroupLayout.DEFAULT_SIZE, 97, Short.MAX_VALUE)
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addGap(0, 74, Short.MAX_VALUE)
                        .addComponent(jButton12)))
                .addGap(29, 29, 29)
                .addGroup(profile_edit_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(button_profile_edit_cancel)
                    .addGroup(profile_edit_panelLayout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel25)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel33)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel36)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel37)
                        .addGap(33, 33, 33))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, profile_edit_panelLayout.createSequentialGroup()
                        .addComponent(text_profile_edit_userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(combo_profile_edit_userGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(text_profile_edit_userContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(text_profile_edit_userEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        contentHolder_panel.add(profile_edit_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

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
            .addGap(0, 98, Short.MAX_VALUE)
        );
        panel_tokenQRCode1Layout.setVerticalGroup(
            panel_tokenQRCode1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );

        button_profile_edit.setText("Edit");
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

        label_profile_userAddress.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_profile_userAddress.setText("userAddress");

        button_profile_back.setText("Back");
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
                            .addComponent(label_profile_userAddress))
                        .addGap(0, 250, Short.MAX_VALUE))
                    .addGroup(profile_panelLayout.createSequentialGroup()
                        .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(profile_panelLayout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button_profile_edit))
                            .addGroup(profile_panelLayout.createSequentialGroup()
                                .addComponent(panel_tokenQRCode1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_profile_back)
                .addContainerGap())
        );
        profile_panelLayout.setVerticalGroup(
            profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, profile_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(profile_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(button_profile_edit))
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
                    .addComponent(label_profile_userAddress)
                    .addComponent(jLabel31))
                .addGap(24, 24, 24)
                .addComponent(button_profile_back)
                .addContainerGap())
        );

        contentHolder_panel.add(profile_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        create_user_panel.setEnabled(false);
        create_user_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel18.setText("Create User");

        jLabel24.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel24.setText("Name");

        jLabel26.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel26.setText("Gender");

        panel_tokenQRCode3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_tokenQRCode3.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout panel_tokenQRCode3Layout = new javax.swing.GroupLayout(panel_tokenQRCode3);
        panel_tokenQRCode3.setLayout(panel_tokenQRCode3Layout);
        panel_tokenQRCode3Layout.setHorizontalGroup(
            panel_tokenQRCode3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        panel_tokenQRCode3Layout.setVerticalGroup(
            panel_tokenQRCode3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );

        button_addUser.setText("Add User");
        button_addUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_addUserActionPerformed(evt);
            }
        });

        jLabel34.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel34.setText("Contact Number");

        jLabel38.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel38.setText("Email");

        jLabel39.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel39.setText("Address");

        jButton35.setText("Change Picture");

        jButton36.setText("Cancel");
        jButton36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton36ActionPerformed(evt);
            }
        });

        text_username.setText("userName");

        text_userContactNumber.setText("userContactNumber");

        text_userEmail.setText("userEmail");

        text_userAddress.setText("userAddress");

        comboBox_gender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        jLabel41.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel41.setText("UserID");

        text_userID.setText("userUD");
        text_userID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_userIDActionPerformed(evt);
            }
        });

        jLabel35.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel35.setText("User Type");

        comboBox_userType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Administrator", "Passenger", "Cashier", "Inspector", "Driver", "Gate Controller" }));

        jLabel47.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel47.setText("Password");

        text_userPassword.setText("userPassword");

        checkBox_userIsActive.setText("Active");

        javax.swing.GroupLayout create_user_panelLayout = new javax.swing.GroupLayout(create_user_panel);
        create_user_panel.setLayout(create_user_panelLayout);
        create_user_panelLayout.setHorizontalGroup(
            create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(create_user_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(create_user_panelLayout.createSequentialGroup()
                        .addComponent(jLabel18)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button_addUser))
                    .addGroup(create_user_panelLayout.createSequentialGroup()
                        .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(create_user_panelLayout.createSequentialGroup()
                                .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel41)
                                    .addComponent(jLabel35))
                                .addGap(59, 59, 59)
                                .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(create_user_panelLayout.createSequentialGroup()
                                        .addComponent(comboBox_userType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(checkBox_userIsActive))
                                    .addComponent(text_userID, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(create_user_panelLayout.createSequentialGroup()
                                .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel24)
                                    .addComponent(jLabel26)
                                    .addComponent(jLabel34)
                                    .addComponent(jLabel38)
                                    .addComponent(jLabel39)
                                    .addComponent(jLabel47))
                                .addGap(18, 18, 18)
                                .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(text_username)
                                    .addComponent(text_userContactNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                    .addComponent(text_userEmail)
                                    .addComponent(text_userAddress)
                                    .addComponent(comboBox_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(text_userPassword))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                        .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton35, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(panel_tokenQRCode3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, create_user_panelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton36)))
                .addContainerGap())
        );
        create_user_panelLayout.setVerticalGroup(
            create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, create_user_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(button_addUser))
                .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(create_user_panelLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(panel_tokenQRCode3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton36)
                        .addContainerGap())
                    .addGroup(create_user_panelLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel35)
                            .addComponent(comboBox_userType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(checkBox_userIsActive))
                        .addGap(11, 11, 11)
                        .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel41)
                            .addComponent(text_userID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel47)
                            .addComponent(text_userPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                        .addGroup(create_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(create_user_panelLayout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel26)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel34)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel38)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel39))
                            .addGroup(create_user_panelLayout.createSequentialGroup()
                                .addComponent(text_username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboBox_gender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(text_userContactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(text_userEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(text_userAddress, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(39, 39, 39))))
        );

        contentHolder_panel.add(create_user_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        jLabel15.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel15.setText("Management ~ Users");

        jLabel44.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel44.setText("Browse User");

        button_mgt_users_administrator.setText("Administrator");
        button_mgt_users_administrator.setMargin(new java.awt.Insets(2, 5, 2, 5));
        button_mgt_users_administrator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_mgt_users_administratorActionPerformed(evt);
            }
        });

        button_mgt_users_passenger.setText("Passenger");
        button_mgt_users_passenger.setMargin(new java.awt.Insets(2, 5, 2, 5));
        button_mgt_users_passenger.setPreferredSize(new java.awt.Dimension(81, 23));
        button_mgt_users_passenger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_mgt_users_passengerActionPerformed(evt);
            }
        });

        jButton27.setText("Cashier");
        jButton27.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton27.setPreferredSize(new java.awt.Dimension(81, 51));
        jButton27.setRequestFocusEnabled(false);
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jButton28.setText("Inspector");
        jButton28.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton28.setPreferredSize(new java.awt.Dimension(81, 23));
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setText("Driver");
        jButton29.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton29.setPreferredSize(new java.awt.Dimension(81, 23));
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jButton30.setText("<html><center>Gate<br>Controller</center>");
        jButton30.setMargin(new java.awt.Insets(2, 5, 2, 5));
        jButton30.setPreferredSize(new java.awt.Dimension(81, 51));
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton15.setText("Back");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jLabel54.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel54.setText("Users Statistics");

        jLabel55.setText("Adminstrators");

        jLabel56.setText("Passengers");

        jLabel57.setText("Inspectors");

        jLabel58.setText("Cashiers");

        jLabel59.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel59.setText("Total");

        jLabel60.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel60.setText("Active");

        label_mgt_users_adminCount.setText("#count");

        label_mgt_users_passengerCount.setText("#count");

        label_mgt_users_inspectorCount.setText("#count");

        label_mgt_users_cashierCount.setText("#count");

        jLabel65.setText("#count");

        label_mgt_users_passengerCount_active.setText("#count");

        label_mgt_users_cashierCount_active.setText("#count");

        label_mgt_users_inspectorCount_active.setText("#count");

        jLabel133.setText("Drivers");

        jLabel134.setText("Gate Operators");

        label_mgt_users_driverCount.setText("#count");

        label_mgt_users_gateOperatorCount.setText("#count");

        label_mgt_users_driverCount_active.setText("#count");

        label_mgt_users_gateOperatorCount_active.setText("#count");

        javax.swing.GroupLayout management_users_panelLayout = new javax.swing.GroupLayout(management_users_panel);
        management_users_panel.setLayout(management_users_panelLayout);
        management_users_panelLayout.setHorizontalGroup(
            management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(management_users_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(management_users_panelLayout.createSequentialGroup()
                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel133)
                            .addComponent(jLabel134))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton15))
                    .addGroup(management_users_panelLayout.createSequentialGroup()
                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel15)
                            .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel44)
                            .addComponent(jLabel54)
                            .addGroup(management_users_panelLayout.createSequentialGroup()
                                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(management_users_panelLayout.createSequentialGroup()
                                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(management_users_panelLayout.createSequentialGroup()
                                                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel58)
                                                    .addComponent(jLabel57))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(label_mgt_users_inspectorCount)
                                                    .addComponent(label_mgt_users_cashierCount)))
                                            .addGroup(management_users_panelLayout.createSequentialGroup()
                                                .addComponent(jLabel56)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(label_mgt_users_passengerCount))
                                            .addGroup(management_users_panelLayout.createSequentialGroup()
                                                .addComponent(jLabel55)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(label_mgt_users_adminCount))
                                            .addGroup(management_users_panelLayout.createSequentialGroup()
                                                .addComponent(button_mgt_users_administrator)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(button_mgt_users_passenger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, management_users_panelLayout.createSequentialGroup()
                                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(label_mgt_users_driverCount)
                                            .addComponent(label_mgt_users_gateOperatorCount))
                                        .addGap(10, 10, 10)))
                                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton27, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(management_users_panelLayout.createSequentialGroup()
                                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(label_mgt_users_inspectorCount_active)
                                            .addComponent(label_mgt_users_cashierCount_active)
                                            .addComponent(label_mgt_users_passengerCount_active)
                                            .addComponent(jLabel65)
                                            .addComponent(jLabel60)
                                            .addComponent(label_mgt_users_driverCount_active)
                                            .addComponent(label_mgt_users_gateOperatorCount_active))
                                        .addGap(7, 7, 7)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(management_users_panelLayout.createSequentialGroup()
                                .addGap(137, 137, 137)
                                .addComponent(jLabel59)))
                        .addGap(0, 35, Short.MAX_VALUE)))
                .addContainerGap())
        );
        management_users_panelLayout.setVerticalGroup(
            management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(management_users_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel15)
                .addGap(18, 18, 18)
                .addComponent(jLabel44)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(button_mgt_users_passenger, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(button_mgt_users_administrator, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton29, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton28, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton27, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton30, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel54)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel59)
                    .addComponent(jLabel60))
                .addGap(5, 5, 5)
                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(management_users_panelLayout.createSequentialGroup()
                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel55)
                            .addComponent(label_mgt_users_adminCount))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel56)
                            .addComponent(label_mgt_users_passengerCount))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel58)
                            .addGroup(management_users_panelLayout.createSequentialGroup()
                                .addComponent(label_mgt_users_cashierCount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(label_mgt_users_inspectorCount)
                                    .addComponent(jLabel57)))))
                    .addGroup(management_users_panelLayout.createSequentialGroup()
                        .addComponent(jLabel65)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_mgt_users_passengerCount_active)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_mgt_users_cashierCount_active)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(label_mgt_users_inspectorCount_active)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(management_users_panelLayout.createSequentialGroup()
                        .addComponent(jLabel133)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel134))
                    .addGroup(management_users_panelLayout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jButton15))
                    .addGroup(management_users_panelLayout.createSequentialGroup()
                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label_mgt_users_driverCount_active)
                            .addComponent(label_mgt_users_driverCount))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(management_users_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(label_mgt_users_gateOperatorCount_active)
                            .addComponent(label_mgt_users_gateOperatorCount))))
                .addContainerGap())
        );

        contentHolder_panel.add(management_users_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 420));

        browse_user_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_browse_users.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "User ID", "Name", "Status", "Gender", "Contact Number", "Email", "Address"
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
        table_browse_users.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        table_browse_users.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_browse_usersMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_browse_users);

        jLabel40.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel40.setText("resultCount");

        jLabel42.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel42.setText("Browse User");

        jLabel43.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel43.setText("Total");

        label_browse_user_userCount.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_browse_user_userCount.setText("#totalUserCount");

        jLabel46.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel46.setText("User ID");

        jLabel48.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel48.setText("Name");

        jLabel50.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel50.setText("Status");

        jLabel53.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel53.setText("Search Results");

        label_browse_userType.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        label_browse_userType.setText("User Type");

        jLabel70.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel70.setText("Filter");

        jTextField9.setText("userName");
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });

        jTextField10.setText("userID");
        jTextField10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField10ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Active");

        button_browse_user_back.setText("Back");
        button_browse_user_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browse_user_backActionPerformed(evt);
            }
        });

        button_browse_user_update.setText("Update");
        button_browse_user_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browse_user_updateActionPerformed(evt);
            }
        });

        button_browse_user_delete.setText("Delete");
        button_browse_user_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browse_user_deleteActionPerformed(evt);
            }
        });

        jLabel94.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel94.setText("Gender");

        jCheckBox5.setText("Male");

        jCheckBox6.setText("Female");

        jLabel95.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel95.setText("Email");

        jTextField17.setText("userID");
        jTextField17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField17ActionPerformed(evt);
            }
        });

        jTextField18.setText("userName");
        jTextField18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField18ActionPerformed(evt);
            }
        });

        jLabel96.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel96.setText("Address");

        javax.swing.GroupLayout browse_user_panelLayout = new javax.swing.GroupLayout(browse_user_panel);
        browse_user_panel.setLayout(browse_user_panelLayout);
        browse_user_panelLayout.setHorizontalGroup(
            browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(browse_user_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(browse_user_panelLayout.createSequentialGroup()
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel70)
                            .addGroup(browse_user_panelLayout.createSequentialGroup()
                                .addComponent(jLabel46)
                                .addGap(18, 18, 18)
                                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20, 20, 20)
                                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel50)
                                    .addComponent(jCheckBox1))
                                .addGap(20, 20, 20)
                                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox6)
                                    .addComponent(jLabel94)
                                    .addComponent(jCheckBox5)))
                            .addGroup(browse_user_panelLayout.createSequentialGroup()
                                .addComponent(jLabel53)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel40))
                            .addComponent(jLabel48))
                        .addGap(18, 18, 18)
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel96)
                            .addComponent(jLabel95))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField18, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                            .addComponent(jTextField17))
                        .addGap(24, 24, 24))
                    .addGroup(browse_user_panelLayout.createSequentialGroup()
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(browse_user_panelLayout.createSequentialGroup()
                                .addComponent(button_browse_user_update)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(button_browse_user_delete)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button_browse_user_back))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
                        .addContainerGap())
                    .addGroup(browse_user_panelLayout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addGap(18, 18, 18)
                        .addComponent(label_browse_userType)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel43)
                        .addGap(18, 18, 18)
                        .addComponent(label_browse_user_userCount)
                        .addGap(24, 24, 24))))
        );
        browse_user_panelLayout.setVerticalGroup(
            browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browse_user_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(label_browse_userType)
                    .addComponent(jLabel43)
                    .addComponent(label_browse_user_userCount))
                .addGap(15, 15, 15)
                .addComponent(jLabel70)
                .addGap(11, 11, 11)
                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(browse_user_panelLayout.createSequentialGroup()
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel46)
                            .addComponent(jLabel94)
                            .addComponent(jLabel50))
                        .addGap(5, 5, 5)
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jCheckBox5)
                            .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel48)
                                .addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCheckBox1))))
                    .addGroup(browse_user_panelLayout.createSequentialGroup()
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel95))
                        .addGap(5, 5, 5)
                        .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel96)
                            .addComponent(jTextField18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel53)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(browse_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_browse_user_back)
                    .addComponent(button_browse_user_update)
                    .addComponent(button_browse_user_delete))
                .addGap(14, 14, 14))
        );

        contentHolder_panel.add(browse_user_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        update_user_panel.setEnabled(false);
        update_user_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jLabel20.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel20.setText("Update User");

        jLabel28.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel28.setText("Name");

        jLabel29.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel29.setText("Gender");

        panel_tokenQRCode4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        panel_tokenQRCode4.setPreferredSize(new java.awt.Dimension(100, 100));

        javax.swing.GroupLayout panel_tokenQRCode4Layout = new javax.swing.GroupLayout(panel_tokenQRCode4);
        panel_tokenQRCode4.setLayout(panel_tokenQRCode4Layout);
        panel_tokenQRCode4Layout.setHorizontalGroup(
            panel_tokenQRCode4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 98, Short.MAX_VALUE)
        );
        panel_tokenQRCode4Layout.setVerticalGroup(
            panel_tokenQRCode4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        button_user_update_update.setText("Update");
        button_user_update_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_user_update_updateActionPerformed(evt);
            }
        });

        jLabel45.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel45.setText("Contact Number");

        jLabel97.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel97.setText("Email");

        jLabel98.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel98.setText("Address");

        jButton13.setText("Change Picture");

        button_user_update_cancel.setText("Back");
        button_user_update_cancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_user_update_cancelActionPerformed(evt);
            }
        });

        text_update_user_userName.setText("userName");

        text_update_user_contactNumber.setText("userContactNumber");

        text_update_user_userEmail.setText("userEmail");

        combo_update_user_userGender.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Male", "Female" }));

        jScrollPane6.setPreferredSize(new java.awt.Dimension(173, 54));

        textArea_update_user_userAddress.setColumns(20);
        textArea_update_user_userAddress.setLineWrap(true);
        textArea_update_user_userAddress.setRows(5);
        textArea_update_user_userAddress.setText("userAddress");
        textArea_update_user_userAddress.setPreferredSize(new java.awt.Dimension(173, 54));
        jScrollPane6.setViewportView(textArea_update_user_userAddress);

        label_update_user_userType.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_update_user_userType.setText("#UserType");

        label_update_user_userID.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_update_user_userID.setText("#UserID");

        checkbox_update_user_userIsActive.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        checkbox_update_user_userIsActive.setText("Active");

        jLabel32.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel32.setText("Password");

        passwordField_update_user_userPassword.setText("****");

        javax.swing.GroupLayout update_user_panelLayout = new javax.swing.GroupLayout(update_user_panel);
        update_user_panel.setLayout(update_user_panelLayout);
        update_user_panelLayout.setHorizontalGroup(
            update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(update_user_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(update_user_panelLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addGap(18, 18, 18)
                        .addComponent(label_update_user_userType)
                        .addGap(44, 44, 44)
                        .addComponent(label_update_user_userID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button_user_update_update))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, update_user_panelLayout.createSequentialGroup()
                        .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel28)
                            .addComponent(jLabel29)
                            .addComponent(jLabel45)
                            .addComponent(jLabel97)
                            .addComponent(jLabel98))
                        .addGap(18, 18, 18)
                        .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(update_user_panelLayout.createSequentialGroup()
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(button_user_update_cancel))
                            .addGroup(update_user_panelLayout.createSequentialGroup()
                                .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(text_update_user_userName)
                                    .addComponent(text_update_user_contactNumber, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                    .addComponent(text_update_user_userEmail)
                                    .addComponent(combo_update_user_userGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(37, 37, 37)
                                .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(checkbox_update_user_userIsActive)
                                    .addComponent(jLabel32)
                                    .addComponent(passwordField_update_user_userPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 45, Short.MAX_VALUE))))
                    .addGroup(update_user_panelLayout.createSequentialGroup()
                        .addComponent(panel_tokenQRCode4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton13)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        update_user_panelLayout.setVerticalGroup(
            update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, update_user_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(button_user_update_update)
                    .addComponent(label_update_user_userType)
                    .addComponent(label_update_user_userID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_tokenQRCode4, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addGroup(update_user_panelLayout.createSequentialGroup()
                        .addGap(0, 75, Short.MAX_VALUE)
                        .addComponent(jButton13)))
                .addGap(26, 26, 26)
                .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(update_user_panelLayout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel29)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel45)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel97)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel98)
                        .addGap(33, 33, 33))
                    .addGroup(update_user_panelLayout.createSequentialGroup()
                        .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(update_user_panelLayout.createSequentialGroup()
                                .addComponent(text_update_user_userName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(combo_update_user_userGender, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(update_user_panelLayout.createSequentialGroup()
                                .addComponent(jLabel32)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(passwordField_update_user_userPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(update_user_panelLayout.createSequentialGroup()
                                .addGroup(update_user_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(update_user_panelLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(text_update_user_contactNumber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(text_update_user_userEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(update_user_panelLayout.createSequentialGroup()
                                        .addGap(21, 21, 21)
                                        .addComponent(checkbox_update_user_userIsActive)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(2, 2, 2))
                            .addGroup(update_user_panelLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 111, Short.MAX_VALUE)
                                .addComponent(button_user_update_cancel)))))
                .addContainerGap())
        );

        contentHolder_panel.add(update_user_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        token_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jTabbedPane1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabbedPane1MouseClicked(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel4.setText("Passenger ID");

        comboBox_create_token_passengerList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel6.setText("Token ID");

        label_create_token_newTokenID.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        label_create_token_newTokenID.setText("#Token ID");

        button_create_token.setText("<html><center>Create<br>Token</center>");
        button_create_token.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_create_tokenActionPerformed(evt);
            }
        });

        button_create_token_back.setText("Back");
        button_create_token_back.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_create_token_backActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout token_create_panelLayout = new javax.swing.GroupLayout(token_create_panel);
        token_create_panel.setLayout(token_create_panelLayout);
        token_create_panelLayout.setHorizontalGroup(
            token_create_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(token_create_panelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(token_create_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(token_create_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(comboBox_create_token_passengerList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(label_create_token_newTokenID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(48, 48, 48)
                .addComponent(button_create_token, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(181, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, token_create_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(button_create_token_back)
                .addGap(34, 34, 34))
        );
        token_create_panelLayout.setVerticalGroup(
            token_create_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(token_create_panelLayout.createSequentialGroup()
                .addGap(66, 66, 66)
                .addGroup(token_create_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(button_create_token, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                    .addGroup(token_create_panelLayout.createSequentialGroup()
                        .addGroup(token_create_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(comboBox_create_token_passengerList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(token_create_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(label_create_token_newTokenID))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 225, Short.MAX_VALUE)
                .addComponent(button_create_token_back)
                .addGap(38, 38, 38))
        );

        jTabbedPane1.addTab("Create Token", token_create_panel);

        browse_token_panel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        browse_user_panel1.setPreferredSize(new java.awt.Dimension(500, 370));

        table_tokens.setAutoCreateRowSorter(true);
        table_tokens.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Token ID", "Passenger ID", "Status", "Ticket Count"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        table_tokens.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_tokensMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(table_tokens);

        jLabel110.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel110.setText("resultCount");

        jLabel111.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel111.setText("Browse Tokens");

        jLabel112.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel112.setText("Total");

        label_browse_tokens_count.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_browse_tokens_count.setText("#totalTokenCount");

        jLabel113.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel113.setText("User ID");

        jLabel114.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel114.setText("Name");

        jLabel115.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel115.setText("Status");

        jLabel116.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel116.setText("Search Results");

        jLabel117.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel117.setText("Filter");

        jTextField19.setText("userName");
        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });

        jTextField20.setText("userID");
        jTextField20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField20ActionPerformed(evt);
            }
        });

        jCheckBox7.setText("Active");

        button_browse_user_back1.setText("Back");
        button_browse_user_back1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browse_user_back1ActionPerformed(evt);
            }
        });

        button_browse_user_update1.setText("More");
        button_browse_user_update1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                button_browse_user_update1ActionPerformed(evt);
            }
        });

        jLabel119.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel119.setText("Min");

        jTextField21.setText("userID");
        jTextField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField21ActionPerformed(evt);
            }
        });

        jTextField22.setText("userName");
        jTextField22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField22ActionPerformed(evt);
            }
        });

        jLabel120.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel120.setText("Max");

        jLabel121.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel121.setText("Ticket Count");

        javax.swing.GroupLayout browse_user_panel1Layout = new javax.swing.GroupLayout(browse_user_panel1);
        browse_user_panel1.setLayout(browse_user_panel1Layout);
        browse_user_panel1Layout.setHorizontalGroup(
            browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(browse_user_panel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel117)
                            .addGroup(browse_user_panel1Layout.createSequentialGroup()
                                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                                        .addComponent(jLabel113)
                                        .addGap(18, 18, 18)
                                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel114))
                                .addGap(31, 31, 31)
                                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox7)
                                    .addComponent(jLabel115))
                                .addGap(30, 30, 30)
                                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel120)
                                            .addComponent(jLabel119))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField22)
                                            .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(jLabel121)))
                            .addGroup(browse_user_panel1Layout.createSequentialGroup()
                                .addComponent(jLabel116)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel110)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                        .addComponent(jLabel111)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel112)
                        .addGap(18, 18, 18)
                        .addComponent(label_browse_tokens_count)
                        .addGap(24, 24, 24))
                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                        .addComponent(button_browse_user_update1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(button_browse_user_back1)
                        .addGap(30, 30, 30))))
        );
        browse_user_panel1Layout.setVerticalGroup(
            browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browse_user_panel1Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel111)
                    .addComponent(jLabel112)
                    .addComponent(label_browse_tokens_count))
                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                        .addGap(45, 45, 45)
                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(browse_user_panel1Layout.createSequentialGroup()
                                .addComponent(jLabel121)
                                .addGap(8, 8, 8)
                                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel119)))
                            .addGroup(browse_user_panel1Layout.createSequentialGroup()
                                .addComponent(jLabel115)
                                .addGap(8, 8, 8)
                                .addComponent(jCheckBox7)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel120)
                            .addComponent(jTextField22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(browse_user_panel1Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel117)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel113))
                        .addGap(5, 5, 5)
                        .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel114)
                            .addComponent(jTextField19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel116)
                    .addComponent(jLabel110))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(browse_user_panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(button_browse_user_back1)
                    .addComponent(button_browse_user_update1))
                .addGap(14, 14, 14))
        );

        browse_token_panel.add(browse_user_panel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        token_detail_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        jTable2.setAutoCreateRowSorter(true);
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
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

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane7.setViewportView(jTable2);

        jLabel99.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel99.setText("tripCount");

        jLabel100.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel100.setText("Token Detail");

        jLabel101.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel101.setText("Passenger ID:");

        label_homescreen_currentUserID1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        label_homescreen_currentUserID1.setText("passengerID");

        jLabel102.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel102.setText("Token ID:");

        jLabel103.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel103.setText("tokenID");

        jLabel104.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel104.setText("Token Status:");

        jLabel105.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel105.setText("tokenStatus");

        panel_tokenQRCode5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout panel_tokenQRCode5Layout = new javax.swing.GroupLayout(panel_tokenQRCode5);
        panel_tokenQRCode5.setLayout(panel_tokenQRCode5Layout);
        panel_tokenQRCode5Layout.setHorizontalGroup(
            panel_tokenQRCode5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );
        panel_tokenQRCode5Layout.setVerticalGroup(
            panel_tokenQRCode5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 60, Short.MAX_VALUE)
        );

        jLabel106.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel106.setText("Credit Balance");

        jLabel107.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel107.setText("00.00");

        jLabel108.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel108.setText("LKR");

        jLabel109.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel109.setText("Scheduled Trips:");

        jButton2.setText("Back");

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jButton1.setForeground(new java.awt.Color(153, 0, 0));
        jButton1.setText("<html><center>Invalidate<br>Token</center>");

        javax.swing.GroupLayout token_detail_panelLayout = new javax.swing.GroupLayout(token_detail_panel);
        token_detail_panel.setLayout(token_detail_panelLayout);
        token_detail_panelLayout.setHorizontalGroup(
            token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(token_detail_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(token_detail_panelLayout.createSequentialGroup()
                        .addComponent(jLabel109)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel99)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(token_detail_panelLayout.createSequentialGroup()
                        .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, token_detail_panelLayout.createSequentialGroup()
                                .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(token_detail_panelLayout.createSequentialGroup()
                                        .addComponent(panel_tokenQRCode5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(token_detail_panelLayout.createSequentialGroup()
                                                .addComponent(jLabel102)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel103))
                                            .addGroup(token_detail_panelLayout.createSequentialGroup()
                                                .addComponent(jLabel104)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel105))))
                                    .addComponent(jLabel100)
                                    .addGroup(token_detail_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel101)
                                        .addGap(18, 18, 18)
                                        .addComponent(label_homescreen_currentUserID1)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel106, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(token_detail_panelLayout.createSequentialGroup()
                                            .addComponent(jLabel108)
                                            .addGap(27, 27, 27)
                                            .addComponent(jLabel107))))))
                        .addContainerGap())))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, token_detail_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addContainerGap())
        );
        token_detail_panelLayout.setVerticalGroup(
            token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, token_detail_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel100)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(token_detail_panelLayout.createSequentialGroup()
                        .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel101)
                            .addComponent(label_homescreen_currentUserID1))
                        .addGap(18, 18, 18)
                        .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(token_detail_panelLayout.createSequentialGroup()
                                .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel102)
                                    .addComponent(jLabel103))
                                .addGap(18, 18, 18)
                                .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel104)
                                    .addComponent(jLabel105)))
                            .addComponent(panel_tokenQRCode5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(token_detail_panelLayout.createSequentialGroup()
                        .addComponent(jLabel106)
                        .addGap(9, 9, 9)
                        .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel108)
                            .addComponent(jLabel107))
                        .addGap(18, 18, 18)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(token_detail_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel109)
                    .addComponent(jLabel99))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addGap(7, 7, 7))
        );

        browse_token_panel.add(token_detail_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        jTabbedPane1.addTab("Browse Token", browse_token_panel);

        javax.swing.GroupLayout token_panelLayout = new javax.swing.GroupLayout(token_panel);
        token_panel.setLayout(token_panelLayout);
        token_panelLayout.setHorizontalGroup(
            token_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, token_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );
        token_panelLayout.setVerticalGroup(
            token_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(token_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.getAccessibleContext().setAccessibleName("Create Token");

        contentHolder_panel.add(token_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 450));

        fare_revenue_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_browse_users1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User ID", "Name", "Status", "Update", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(table_browse_users1);

        jLabel49.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel49.setText("resultCount");

        jLabel51.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel51.setText("Fare Revenue");

        jLabel52.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel52.setText("Total");

        jLabel69.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel69.setText("#totalUserCount");

        jLabel71.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel71.setText("User ID");

        jLabel72.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel72.setText("Name");

        jLabel73.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel73.setText("Status");

        jLabel74.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel74.setText("Search Results");

        jLabel75.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel75.setText("Filter");

        jTextField11.setText("userName");
        jTextField11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField11ActionPerformed(evt);
            }
        });

        jTextField12.setText("userID");
        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });

        jCheckBox2.setText("Active");

        jButton16.setText("Back");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout fare_revenue_panelLayout = new javax.swing.GroupLayout(fare_revenue_panel);
        fare_revenue_panel.setLayout(fare_revenue_panelLayout);
        fare_revenue_panelLayout.setHorizontalGroup(
            fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(fare_revenue_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(fare_revenue_panelLayout.createSequentialGroup()
                        .addGroup(fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addGroup(fare_revenue_panelLayout.createSequentialGroup()
                                .addComponent(jLabel71)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel72)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel73)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox2))
                            .addGroup(fare_revenue_panelLayout.createSequentialGroup()
                                .addGroup(fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(fare_revenue_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel52)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel69))
                                    .addComponent(jLabel75))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(fare_revenue_panelLayout.createSequentialGroup()
                        .addGroup(fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel51)
                            .addGroup(fare_revenue_panelLayout.createSequentialGroup()
                                .addComponent(jLabel74)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel49)))
                        .addGap(0, 297, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fare_revenue_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton16)
                .addContainerGap())
        );
        fare_revenue_panelLayout.setVerticalGroup(
            fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, fare_revenue_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel51)
                .addGap(15, 15, 15)
                .addGroup(fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel52)
                    .addComponent(jLabel69))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel75)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73)
                    .addComponent(jCheckBox2)
                    .addComponent(jLabel72)
                    .addComponent(jTextField11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel71))
                .addGap(18, 18, 18)
                .addGroup(fare_revenue_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel74)
                    .addComponent(jLabel49))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton16)
                .addGap(8, 8, 8))
        );

        contentHolder_panel.add(fare_revenue_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        network_usage_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_browse_users2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User ID", "Name", "Status", "Update", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane4.setViewportView(table_browse_users2);

        jLabel76.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel76.setText("resultCount");

        jLabel77.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel77.setText("Network Usage");

        jLabel78.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel78.setText("Total");

        jLabel79.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel79.setText("#totalUserCount");

        jLabel80.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel80.setText("User ID");

        jLabel81.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel81.setText("Name");

        jLabel82.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel82.setText("Status");

        jLabel83.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel83.setText("Search Results");

        jLabel84.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel84.setText("Filter");

        jTextField13.setText("userName");
        jTextField13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField13ActionPerformed(evt);
            }
        });

        jTextField14.setText("userID");
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });

        jCheckBox3.setText("Active");

        jButton18.setText("Back");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout network_usage_panelLayout = new javax.swing.GroupLayout(network_usage_panel);
        network_usage_panel.setLayout(network_usage_panelLayout);
        network_usage_panelLayout.setHorizontalGroup(
            network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(network_usage_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(network_usage_panelLayout.createSequentialGroup()
                        .addGroup(network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addGroup(network_usage_panelLayout.createSequentialGroup()
                                .addComponent(jLabel80)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel81)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel82)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox3))
                            .addGroup(network_usage_panelLayout.createSequentialGroup()
                                .addGroup(network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(network_usage_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel78)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel79))
                                    .addComponent(jLabel84))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(network_usage_panelLayout.createSequentialGroup()
                        .addGroup(network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel77)
                            .addGroup(network_usage_panelLayout.createSequentialGroup()
                                .addComponent(jLabel83)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel76)))
                        .addGap(0, 297, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, network_usage_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton18)
                .addContainerGap())
        );
        network_usage_panelLayout.setVerticalGroup(
            network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, network_usage_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel77)
                .addGap(15, 15, 15)
                .addGroup(network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(jLabel79))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel84)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel82)
                    .addComponent(jCheckBox3)
                    .addComponent(jLabel81)
                    .addComponent(jTextField13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel80))
                .addGap(18, 18, 18)
                .addGroup(network_usage_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel83)
                    .addComponent(jLabel76))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton18)
                .addGap(8, 8, 8))
        );

        contentHolder_panel.add(network_usage_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        passenger_info_panel.setPreferredSize(new java.awt.Dimension(500, 370));

        table_browse_users3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "User ID", "Name", "Status", "Update", "Delete"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane5.setViewportView(table_browse_users3);

        jLabel85.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel85.setText("resultCount");

        jLabel86.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel86.setText("Passenger Info");

        jLabel87.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel87.setText("Total");

        jLabel88.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel88.setText("#totalUserCount");

        jLabel89.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel89.setText("User ID");

        jLabel90.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel90.setText("Name");

        jLabel91.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel91.setText("Status");

        jLabel92.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel92.setText("Search Results");

        jLabel93.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel93.setText("Filter");

        jTextField15.setText("userName");
        jTextField15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField15ActionPerformed(evt);
            }
        });

        jTextField16.setText("userID");
        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });

        jCheckBox4.setText("Active");

        jButton19.setText("Back");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout passenger_info_panelLayout = new javax.swing.GroupLayout(passenger_info_panel);
        passenger_info_panel.setLayout(passenger_info_panelLayout);
        passenger_info_panelLayout.setHorizontalGroup(
            passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(passenger_info_panelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(passenger_info_panelLayout.createSequentialGroup()
                        .addGroup(passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
                            .addGroup(passenger_info_panelLayout.createSequentialGroup()
                                .addComponent(jLabel89)
                                .addGap(18, 18, 18)
                                .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel90)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel91)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jCheckBox4))
                            .addGroup(passenger_info_panelLayout.createSequentialGroup()
                                .addGroup(passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(passenger_info_panelLayout.createSequentialGroup()
                                        .addComponent(jLabel87)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel88))
                                    .addComponent(jLabel93))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(passenger_info_panelLayout.createSequentialGroup()
                        .addGroup(passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel86)
                            .addGroup(passenger_info_panelLayout.createSequentialGroup()
                                .addComponent(jLabel92)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel85)))
                        .addGap(0, 297, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, passenger_info_panelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton19)
                .addContainerGap())
        );
        passenger_info_panelLayout.setVerticalGroup(
            passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, passenger_info_panelLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel86)
                .addGap(15, 15, 15)
                .addGroup(passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel87)
                    .addComponent(jLabel88))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel93)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField16, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel91)
                    .addComponent(jCheckBox4)
                    .addComponent(jLabel90)
                    .addComponent(jTextField15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel89))
                .addGap(18, 18, 18)
                .addGroup(passenger_info_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel92)
                    .addComponent(jLabel85))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton19)
                .addGap(8, 8, 8))
        );

        contentHolder_panel.add(passenger_info_panel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 370));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(mainMenu_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentHolder_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(contentHolder_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainMenu_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
    
    private void button_profile_edit_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profile_edit_cancelActionPerformed
        // TODO add your handling code here:
        //profile_edit_panel.setVisible(false);
        hide_panels();
        profile_panel.setVisible(true);
    }//GEN-LAST:event_button_profile_edit_cancelActionPerformed

    private void button_profile_editActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profile_editActionPerformed
        // TODO add your handling code here:
        //profile_panel.setVisible(false);
        hide_panels();
        profile_edit_panel.setVisible(true);
    }//GEN-LAST:event_button_profile_editActionPerformed

    private void button_profile_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profile_backActionPerformed
        // TODO add your handling code here:
        //profile_panel.setVisible(false);
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_profile_backActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        new LogoutConfirm(1).setVisible(true);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void button_profileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profileActionPerformed
        // TODO add your handling code here:
        //homescreen_panel.setVisible(false);
        hide_panels();
        profile_panel.setVisible(true);
    }//GEN-LAST:event_button_profileActionPerformed

    private void jButton36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton36ActionPerformed
        // TODO add your handling code here:
        create_user_panel.setVisible(false);
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_jButton36ActionPerformed

    private void text_userIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_userIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_userIDActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        // TODO add your handling code here:
        browseUserType = 4;
        populate_browse_user_panel(4);
        hide_panels();
        browse_user_panel.setVisible(true);
    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        // TODO add your handling code here:
        browseUserType = 3;
        populate_browse_user_panel(3);
        hide_panels();
        browse_user_panel.setVisible(true);
    }//GEN-LAST:event_jButton27ActionPerformed

    private void button_usersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_usersActionPerformed
        // TODO add your handling code here:
        //homescreen_panel.setVisible(false);
        hide_panels();
        populate_user_management_panel();
        management_users_panel.setVisible(true);
    }//GEN-LAST:event_button_usersActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        management_users_panel.setVisible(false);
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_jButton15ActionPerformed

    private void button_create_userActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_create_userActionPerformed
        // TODO add your handling code here:
        //homescreen_panel.setVisible(false);
        hide_panels();
        create_user_panel.setVisible(true);
    }//GEN-LAST:event_button_create_userActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField10ActionPerformed

    private void button_mgt_users_administratorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_mgt_users_administratorActionPerformed
        // TODO add your handling code here:
        //management_users_panel.setVisible(false);
        browseUserType = 1;
        populate_browse_user_panel(1);
        hide_panels();
        browse_user_panel.setVisible(true);

        //TODO: load the admin data file
    }//GEN-LAST:event_button_mgt_users_administratorActionPerformed

    private void button_browse_user_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browse_user_backActionPerformed
        // TODO add your handling code here:
        hide_panels();
        populate_user_management_panel();
        management_users_panel.setVisible(true);
    }//GEN-LAST:event_button_browse_user_backActionPerformed

    private void button_addUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_addUserActionPerformed
        // TODO add your handling code here:
        int userType = comboBox_userType.getSelectedIndex()+1;
        String userID = text_userID.getText();
        String username = text_username.getText();
        String userPassword = text_userPassword.getText();
        boolean isActive = checkBox_userIsActive.isSelected();
        int gender = comboBox_gender.getSelectedIndex();
        String contactNumber = text_userContactNumber.getText();
        String email = text_userEmail.getText();
        String address = text_userAddress.getText();
        
        User tempUser =  new User(userID, userType, userPassword, isActive,username, gender,contactNumber, email, address );
        
        switch(userType){
            case 1:
                if(PublicTransportTicketingSystem.adminController.addUser(tempUser, userType, userID)){
                    JOptionPane.showMessageDialog(null, "Administrator("+ userID +") has been successfully added to the system");
                    clearFields_create_user_panel();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Administrator("+ userID +") NOT added to the system","Inane error",JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 2:
                if(PublicTransportTicketingSystem.adminController.addUser(tempUser, userType, userID)){
                    JOptionPane.showMessageDialog(null, "Passenger("+ userID +") has been successfully added to the system");
                    clearFields_create_user_panel();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Passenger("+ userID +") NOT added to the system","Inane error",JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 3:
                if(PublicTransportTicketingSystem.adminController.addUser(tempUser, userType, userID)){
                    JOptionPane.showMessageDialog(null, "Cashier("+ userID +") has been successfully added to the system");
                    clearFields_create_user_panel();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Cashier("+ userID +") NOT added to the system","Inane error",JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 4:
                if(PublicTransportTicketingSystem.adminController.addUser(tempUser, userType, userID)){
                    JOptionPane.showMessageDialog(null, "Inspector("+ userID +") has been successfully added to the system");
                    clearFields_create_user_panel();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Inspector("+ userID +") NOT added to the system","Inane error",JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 5:
                if(PublicTransportTicketingSystem.adminController.addUser(tempUser, userType, userID)){
                    JOptionPane.showMessageDialog(null, "Driver("+ userID +") has been successfully added to the system");
                    clearFields_create_user_panel();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Driver("+ userID +") NOT added to the system","Inane error",JOptionPane.ERROR_MESSAGE);
                }
                break;
                
            case 6:
                if(PublicTransportTicketingSystem.adminController.addUser(tempUser, userType, userID)){
                    JOptionPane.showMessageDialog(null, "Gate Operator("+ userID +") has been successfully added to the system");
                    clearFields_create_user_panel();
                }
                else{
                    JOptionPane.showMessageDialog(this, "Gate Operator("+ userID +") NOT added to the system","Inane error",JOptionPane.ERROR_MESSAGE);
                }
                break;
                
        }
    }//GEN-LAST:event_button_addUserActionPerformed

    private void button_profiel_edit_doneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_profiel_edit_doneActionPerformed
        // TODO add your handling code here:
        int userType = currentAdministrator.getUserType();
        String userID = currentAdministrator.getUserID();
        String userPassword = currentAdministrator.getUserPassword();
        String username = text_profile_edit_userName.getText();
        int gender;
        if(combo_profile_edit_userGender.getSelectedIndex()==0){
            gender = 1;
        }
        else{
            gender = 2;
        }
        String contactNumber = text_profile_edit_userContactNumber.getText();
        String email = text_profile_edit_userEmail.getText();
        String address = textArea_profile_edit_userAddress.getText();
      
        Administrator updatedAdmin =  new Administrator(userID, userType, userPassword,currentAdministrator.isActive(), username, gender,contactNumber, email, address );
      
        if(PublicTransportTicketingSystem.adminController.updateUser(userID,updatedAdmin, userType)){
                    
            JOptionPane.showMessageDialog(null, "Administrator("+ userID +") has been successfully updated");
            //currentAdministrator = tempAdmin;
            currentAdministrator = PublicTransportTicketingSystem.adminController.getCurrentAdmin();
            populate_home_screen_panel();
            populate_profile_and_edit_panels();
            //profile_edit_panel.setVisible(false);
            hide_panels();
            profile_panel.setVisible(true);
                    
        }
        else{
            JOptionPane.showMessageDialog(this, "Administrator("+ userID +") NOT updated!","Error",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_button_profiel_edit_doneActionPerformed

    private void button_mgt_users_passengerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_mgt_users_passengerActionPerformed
        // TODO add your handling code here:
        //management_users_panel.setVisible(false);
        browseUserType = 2;
        populate_browse_user_panel(2);
        hide_panels();
        browse_user_panel.setVisible(true);
        
    }//GEN-LAST:event_button_mgt_users_passengerActionPerformed

    private void jTextField11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField11ActionPerformed

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jTextField13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField13ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField13ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jTextField15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField15ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField15ActionPerformed

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton19ActionPerformed

    private void button_tokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_tokenActionPerformed
        // TODO add your handling code here:
        hide_panels();
        populate_create_token_browse_token_panel();
        token_panel.setVisible(true);
    }//GEN-LAST:event_button_tokenActionPerformed

    private void button_network_usageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_network_usageActionPerformed
        // TODO add your handling code here:
        hide_panels();
        network_usage_panel.setVisible(true);
    }//GEN-LAST:event_button_network_usageActionPerformed

    private void button_individual_passengerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_individual_passengerActionPerformed
        // TODO add your handling code here:
        hide_panels();
        passenger_info_panel.setVisible(true);
    }//GEN-LAST:event_button_individual_passengerActionPerformed

    private void button_pay_stationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_pay_stationActionPerformed
        // TODO add your handling code here:
        new PayStationTerminal(currentAdministrator).setVisible(true);

    }//GEN-LAST:event_button_pay_stationActionPerformed

    private void button_browse_user_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browse_user_deleteActionPerformed
        // TODO add your handling code here:
        String userID = selectedUserID_browse_user_table();
        
        int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this user?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
        if(response == JOptionPane.YES_OPTION)
        {
            switch(browseUserType){
                case 1:
                    Administrator tempAdmin = currentAdminList.get(getSeletedUserIndex_browse_user_table(userID));
                   
                    if(AdminManager.deleteAdministrator(tempAdmin)){
                        currentAdminList.remove(tempAdmin);
                        populate_browse_user_panel(browseUserType);
                        loadBrowseUserTableData();
                        JOptionPane.showMessageDialog(rootPane, "Administrator "+tempAdmin.getUserID()+" has been deleted successfully");
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "Administrator deletion error!!!");
                    }
                    break;
                    
                case 2:
                    Passenger tempPassenger = currentPassengerList.get(getSeletedUserIndex_browse_user_table(userID));
                   
                    if(PassengerManager.deletePassenger(tempPassenger)){
                        currentPassengerList.remove(tempPassenger);
                        populate_browse_user_panel(browseUserType);
                        loadBrowseUserTableData();
                        JOptionPane.showMessageDialog(rootPane, "Passenger " + tempPassenger.getUserID() + " has been deleted successfully");
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "Passenger deletion error!!!");
                    }
                    break;
                    
                case 3:
                    Cashier tempCashier = currentCashierList.get(getSeletedUserIndex_browse_user_table(userID));
                   
                    if(CashierManager.deleteCashier(tempCashier)){
                        currentCashierList.remove(tempCashier);
                        populate_browse_user_panel(browseUserType);
                        loadBrowseUserTableData();
                        JOptionPane.showMessageDialog(rootPane, "Cashier "+tempCashier.getUserID()+" has been deleted successfully");
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "Cashier deletion error!!!");
                    }
                    break;
                    
                case 4:
                    Inspector tempInspector = currentInspectorList.get(getSeletedUserIndex_browse_user_table(userID));
                   
                    if(InspectorManager.deleteInspector(tempInspector)){
                        currentInspectorList.remove(tempInspector);
                        populate_browse_user_panel(browseUserType);
                        loadBrowseUserTableData();
                        JOptionPane.showMessageDialog(rootPane, "Inspector "+tempInspector.getUserID()+" has been deleted successfully");
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "Inspector deletion error!!!");
                    }
                    break;
                    
                case 5:
                    Driver tempDriver = currentDriverList.get(getSeletedUserIndex_browse_user_table(userID));
                   
                    if(DriverManager.deleteDriver(tempDriver)){
                        currentDriverList.remove(tempDriver);
                        populate_browse_user_panel(browseUserType);
                        loadBrowseUserTableData();
                        JOptionPane.showMessageDialog(rootPane, "Driver "+tempDriver.getUserID()+" has been deleted successfully");
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "Driver deletion error!!!");
                    }
                    break;
                    
                case 6:
                    GateOperator tempGateOperator = currentGateOperatorList.get(getSeletedUserIndex_browse_user_table(userID));
                   
                    if(GateOperatorManager.deleteGateOperator(tempGateOperator)){
                        currentGateOperatorList.remove(tempGateOperator);
                        populate_browse_user_panel(browseUserType);
                        loadBrowseUserTableData();
                        JOptionPane.showMessageDialog(rootPane, "GateOperator "+tempGateOperator.getUserID()+" has been deleted successfully");
                        
                    }
                    else{
                        JOptionPane.showMessageDialog(rootPane, "GateOperator deletion error!!!");
                    }
                    break;
                    
            }
 
        }
        else 
        {               
            button_browse_user_delete.setVisible(false);
        }
    }//GEN-LAST:event_button_browse_user_deleteActionPerformed

    private void jTextField17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField17ActionPerformed

    private void jTextField18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField18ActionPerformed

    private void table_browse_usersMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_browse_usersMouseClicked
        // TODO add your handling code here:
        button_browse_user_update.setEnabled(true);
        button_browse_user_delete.setEnabled(true);
    }//GEN-LAST:event_table_browse_usersMouseClicked

    private void button_user_update_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_user_update_updateActionPerformed
        // TODO add your handling code here:
        String userID = selectedUserID_browse_user_table();
        
        int userType = browseUserType;
        boolean isUserActive = checkbox_update_user_userIsActive.isSelected();
        String userPassword = String.valueOf(passwordField_update_user_userPassword.getPassword());
        String username = text_update_user_userName.getText();
        int gender = -1;
        if(combo_update_user_userGender.getSelectedIndex()== 0){
            gender = 1;
        }
        else{
            gender = 2;
        }
        String contactNumber = text_update_user_contactNumber.getText();
        String email = text_update_user_userEmail.getText();
        String address = textArea_update_user_userAddress.getText();
        
        switch(browseUserType){
            case 1:
                
                Administrator newAdmin =  new Administrator(userID, userType, userPassword,isUserActive, username, gender,contactNumber, email, address );
                
                int response = JOptionPane.showConfirmDialog(null, "Do you want to update this user?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        
                if(response == JOptionPane.YES_OPTION){
                    if(PublicTransportTicketingSystem.adminController.updateUser(userID,newAdmin, userType)){
                    JOptionPane.showMessageDialog(null, "Administrator("+ userID +") has been successfully updated");
                    //tempAdminList.set(tempAdminList.indexOf(tempAdmin), newAdmin);
                    loadUserLists();
                    populate_browse_user_panel(browseUserType);
                    loadBrowseUserTableData();
                    hide_panels();
                    
                    checkbox_update_user_userIsActive.setSelected(newAdmin.isActive());
                    passwordField_update_user_userPassword.setText(newAdmin.getUserPassword());
                    text_update_user_userName.setText(newAdmin.getName());
                    if (newAdmin.getGender() == 1) {
                        combo_update_user_userGender.setSelectedIndex(0);
                    }
                    else{
                        combo_update_user_userGender.setSelectedIndex(1);
                    }
                    text_update_user_contactNumber.setText(newAdmin.getContactNumber());
                    text_update_user_userEmail.setText(newAdmin.getEmail());
                    textArea_update_user_userAddress.setText(newAdmin.getAddress());
                    
                    update_user_panel.setVisible(true);

                    }
                    else{
                        JOptionPane.showMessageDialog(this, "Administrator("+ userID +") NOT updated!","Message",JOptionPane.WARNING_MESSAGE);
                    }
                }

                break;
            
            default:
                JOptionPane.showMessageDialog(rootPane, "AdminTerminal: Browse User -> Update: case("+browseUserType+") not defined!!!");
        }
            
        
        
        
      
        
    }//GEN-LAST:event_button_user_update_updateActionPerformed

    private void button_user_update_cancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_user_update_cancelActionPerformed
        // TODO add your handling code here:
        hide_panels();
        browse_user_panel.setVisible(true);
    }//GEN-LAST:event_button_user_update_cancelActionPerformed

    private void button_browse_user_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browse_user_updateActionPerformed
        // TODO add your handling code here:
        String userID = selectedUserID_browse_user_table();
        
        
        switch(browseUserType){
            case 1:
                label_update_user_userType.setText("Administrator");
                label_update_user_userID.setText(userID);
                Administrator tempAdmin = currentAdminList.get(getSeletedUserIndex_browse_user_table(userID));
                checkbox_update_user_userIsActive.setSelected(tempAdmin.isActive());
                passwordField_update_user_userPassword.setText(tempAdmin.getUserPassword());
                text_update_user_userName.setText(tempAdmin.getName());
                if (tempAdmin.getGender() == 1) {
                    combo_update_user_userGender.setSelectedIndex(0);
                }
                else{
                    combo_update_user_userGender.setSelectedIndex(1);
                }
                text_update_user_contactNumber.setText(tempAdmin.getContactNumber());
                text_update_user_userEmail.setText(tempAdmin.getEmail());
                textArea_update_user_userAddress.setText(tempAdmin.getAddress());
                hide_panels();
                update_user_panel.setVisible(true);
                
                break;
            
            case 2:
                label_update_user_userType.setText("Passenger");
                label_update_user_userID.setText(userID);
                Passenger tempPassenger = currentPassengerList.get(getSeletedUserIndex_browse_user_table(userID));
                checkbox_update_user_userIsActive.setSelected(tempPassenger.isActive());
                passwordField_update_user_userPassword.setText(tempPassenger.getUserPassword());
                text_update_user_userName.setText(tempPassenger.getName());
                if (tempPassenger.getGender() == 1) {
                    combo_update_user_userGender.setSelectedIndex(0);
                }
                else{
                    combo_update_user_userGender.setSelectedIndex(1);
                }
                text_update_user_contactNumber.setText(tempPassenger.getContactNumber());
                text_update_user_userEmail.setText(tempPassenger.getEmail());
                textArea_update_user_userAddress.setText(tempPassenger.getAddress());
                hide_panels();
                update_user_panel.setVisible(true);
                
                break;    
            
            case 3:
                label_update_user_userType.setText("Cashier");
                label_update_user_userID.setText(userID);
                Cashier tempCashier = currentCashierList.get(getSeletedUserIndex_browse_user_table(userID));
                checkbox_update_user_userIsActive.setSelected(tempCashier.isActive());
                passwordField_update_user_userPassword.setText(tempCashier.getUserPassword());
                text_update_user_userName.setText(tempCashier.getName());
                if (tempCashier.getGender() == 1) {
                    combo_update_user_userGender.setSelectedIndex(0);
                }
                else{
                    combo_update_user_userGender.setSelectedIndex(1);
                }
                text_update_user_contactNumber.setText(tempCashier.getContactNumber());
                text_update_user_userEmail.setText(tempCashier.getEmail());
                textArea_update_user_userAddress.setText(tempCashier.getAddress());
                hide_panels();
                update_user_panel.setVisible(true);
                
                break;    
            
            case 4:
                label_update_user_userType.setText("Inspector");
                label_update_user_userID.setText(userID);
                Inspector tempInspector = currentInspectorList.get(getSeletedUserIndex_browse_user_table(userID));
                checkbox_update_user_userIsActive.setSelected(tempInspector.isActive());
                passwordField_update_user_userPassword.setText(tempInspector.getUserPassword());
                text_update_user_userName.setText(tempInspector.getName());
                if (tempInspector.getGender() == 1) {
                    combo_update_user_userGender.setSelectedIndex(0);
                }
                else{
                    combo_update_user_userGender.setSelectedIndex(1);
                }
                text_update_user_contactNumber.setText(tempInspector.getContactNumber());
                text_update_user_userEmail.setText(tempInspector.getEmail());
                textArea_update_user_userAddress.setText(tempInspector.getAddress());
                hide_panels();
                update_user_panel.setVisible(true);
                
                break;
                
            case 5:
                label_update_user_userType.setText("Driver");
                label_update_user_userID.setText(userID);
                Driver tempDriver = currentDriverList.get(getSeletedUserIndex_browse_user_table(userID));
                checkbox_update_user_userIsActive.setSelected(tempDriver.isActive());
                passwordField_update_user_userPassword.setText(tempDriver.getUserPassword());
                text_update_user_userName.setText(tempDriver.getName());
                if (tempDriver.getGender() == 1) {
                    combo_update_user_userGender.setSelectedIndex(0);
                }
                else{
                    combo_update_user_userGender.setSelectedIndex(1);
                }
                text_update_user_contactNumber.setText(tempDriver.getContactNumber());
                text_update_user_userEmail.setText(tempDriver.getEmail());
                textArea_update_user_userAddress.setText(tempDriver.getAddress());
                hide_panels();
                update_user_panel.setVisible(true);
                
                break;
                
            case 6:
                label_update_user_userType.setText("Gate Operator");
                label_update_user_userID.setText(userID);
                GateOperator tempGateOperator = currentGateOperatorList.get(getSeletedUserIndex_browse_user_table(userID));
                checkbox_update_user_userIsActive.setSelected(tempGateOperator.isActive());
                passwordField_update_user_userPassword.setText(tempGateOperator.getUserPassword());
                text_update_user_userName.setText(tempGateOperator.getName());
                if (tempGateOperator.getGender() == 1) {
                    combo_update_user_userGender.setSelectedIndex(0);
                }
                else{
                    combo_update_user_userGender.setSelectedIndex(1);
                }
                text_update_user_contactNumber.setText(tempGateOperator.getContactNumber());
                text_update_user_userEmail.setText(tempGateOperator.getEmail());
                textArea_update_user_userAddress.setText(tempGateOperator.getAddress());
                hide_panels();
                update_user_panel.setVisible(true);
                
                break;
                
            default:
                JOptionPane.showMessageDialog(rootPane, "AdminTerminal: Browse User -> Update: case("+browseUserType+") not defined!!!");
        }
    }//GEN-LAST:event_button_browse_user_updateActionPerformed

    private void table_tokensMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_tokensMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_table_tokensMouseClicked

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jTextField20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField20ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField20ActionPerformed

    private void button_browse_user_back1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browse_user_back1ActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_browse_user_back1ActionPerformed

    private void button_browse_user_update1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browse_user_update1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_button_browse_user_update1ActionPerformed

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jTextField22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField22ActionPerformed

    private void button_create_tokenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_create_tokenActionPerformed
        // TODO add your handling code here:
        String passengerID = (String)comboBox_create_token_passengerList.getSelectedItem();
        String tokenID = label_create_token_newTokenID.getText();
        
        if(adminController.createToken(tokenID, passengerID)){
            JOptionPane.showMessageDialog(rootPane, "Token("+tokenID+") has been successfully created!");
            loadTokenList();
            populate_create_token_browse_token_panel();
        }
        else{
            JOptionPane.showMessageDialog(null, "Token("+tokenID+") not added to the system!","Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_button_create_tokenActionPerformed

    private void button_create_token_backActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_create_token_backActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_create_token_backActionPerformed

    private void table_case_reportsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_case_reportsMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_table_case_reportsMouseClicked

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jTextField24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField24ActionPerformed

    private void button_browse_user_back2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_button_browse_user_back2ActionPerformed
        // TODO add your handling code here:
        hide_panels();
        homescreen_panel.setVisible(true);
    }//GEN-LAST:event_button_browse_user_back2ActionPerformed

    private void jTextField25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField25ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField25ActionPerformed

    private void jTextField26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField26ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        hide_panels();
        loadCaseReportsTableData();
        browse_case_reports_panel.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        // TODO add your handling code here:
        browseUserType = 5;
        populate_browse_user_panel(5);
        hide_panels();
        browse_user_panel.setVisible(true);
    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        // TODO add your handling code here:
        browseUserType = 6;
        populate_browse_user_panel(6);
        hide_panels();
        browse_user_panel.setVisible(true);
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jTabbedPane1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabbedPane1MouseClicked
        // TODO add your handling code here:
        loadTokenTableData();
    }//GEN-LAST:event_jTabbedPane1MouseClicked

    private void dateChooser_homescreen_startDateOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooser_homescreen_startDateOnSelectionChange
        // TODO add your handling code here:
        
        dateChooser_homescreen_endDate.setMinDate(dateChooser_homescreen_startDate.getCurrent());
        dateChooser_homescreen_endDate.setSelectedDate(dateChooser_homescreen_startDate.getCurrent());
        
//        float amount = adminController.calculateTotalRevenue_customRange(dateChooser_homescreen_startDate.getCurrent(), dateChooser_homescreen_endDate.getCurrent());
//        if (amount>0) {
//            label_homescreen_custom_revenue.setText(String.valueOf(amount));
//        }
//        else{
//            label_homescreen_custom_revenue.setText(" -- ");
//        }
        
    }//GEN-LAST:event_dateChooser_homescreen_startDateOnSelectionChange

    private void dateChooser_homescreen_endDateOnSelectionChange(datechooser.events.SelectionChangedEvent evt) {//GEN-FIRST:event_dateChooser_homescreen_endDateOnSelectionChange
        // TODO add your handling code here:
        float amount = adminController.calculateTotalRevenue_customRange(dateChooser_homescreen_startDate.getCurrent(), dateChooser_homescreen_endDate.getCurrent());
        if (amount>0) {
            label_homescreen_custom_revenue.setText(String.valueOf(amount));
        }
        else{
            label_homescreen_custom_revenue.setText(" -- ");
        }
        
    }//GEN-LAST:event_dateChooser_homescreen_endDateOnSelectionChange

    

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
            java.util.logging.Logger.getLogger(AdminTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminTerminal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //new AdminTerminal().setVisible(true);
                //adminTerminal= new AdminTerminal();
                adminTerminal.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel browse_case_reports_panel;
    private javax.swing.JPanel browse_token_panel;
    private javax.swing.JPanel browse_user_panel;
    private javax.swing.JPanel browse_user_panel1;
    private javax.swing.JButton button_addUser;
    private javax.swing.JButton button_browse_user_back;
    private javax.swing.JButton button_browse_user_back1;
    private javax.swing.JButton button_browse_user_back2;
    private javax.swing.JButton button_browse_user_delete;
    private javax.swing.JButton button_browse_user_update;
    private javax.swing.JButton button_browse_user_update1;
    private javax.swing.JButton button_create_token;
    private javax.swing.JButton button_create_token_back;
    private javax.swing.JButton button_create_user;
    private javax.swing.JButton button_individual_passenger;
    private javax.swing.JButton button_mgt_users_administrator;
    private javax.swing.JButton button_mgt_users_passenger;
    private javax.swing.JButton button_network_usage;
    private javax.swing.JButton button_pay_station;
    private javax.swing.JButton button_profiel_edit_done;
    private javax.swing.JButton button_profile;
    private javax.swing.JButton button_profile_back;
    private javax.swing.JButton button_profile_edit;
    private javax.swing.JButton button_profile_edit_cancel;
    private javax.swing.JButton button_token;
    private javax.swing.JButton button_user_update_cancel;
    private javax.swing.JButton button_user_update_update;
    private javax.swing.JButton button_users;
    private javax.swing.JCheckBox checkBox_userIsActive;
    private javax.swing.JCheckBox checkbox_update_user_userIsActive;
    private javax.swing.JComboBox comboBox_create_token_passengerList;
    private javax.swing.JComboBox comboBox_gender;
    private javax.swing.JComboBox comboBox_userType;
    private javax.swing.JComboBox combo_profile_edit_userGender;
    private javax.swing.JComboBox combo_update_user_userGender;
    private javax.swing.JPanel contentHolder_panel;
    private javax.swing.JPanel create_user_panel;
    private datechooser.beans.DateChooserCombo dateChooserCombo1;
    private datechooser.beans.DateChooserCombo dateChooserCombo2;
    private datechooser.beans.DateChooserCombo dateChooser_homescreen_endDate;
    private datechooser.beans.DateChooserCombo dateChooser_homescreen_startDate;
    private javax.swing.JPanel fare_revenue_panel;
    private javax.swing.JPanel homescreen_panel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton35;
    private javax.swing.JButton jButton36;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel100;
    private javax.swing.JLabel jLabel101;
    private javax.swing.JLabel jLabel102;
    private javax.swing.JLabel jLabel103;
    private javax.swing.JLabel jLabel104;
    private javax.swing.JLabel jLabel105;
    private javax.swing.JLabel jLabel106;
    private javax.swing.JLabel jLabel107;
    private javax.swing.JLabel jLabel108;
    private javax.swing.JLabel jLabel109;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel110;
    private javax.swing.JLabel jLabel111;
    private javax.swing.JLabel jLabel112;
    private javax.swing.JLabel jLabel113;
    private javax.swing.JLabel jLabel114;
    private javax.swing.JLabel jLabel115;
    private javax.swing.JLabel jLabel116;
    private javax.swing.JLabel jLabel117;
    private javax.swing.JLabel jLabel118;
    private javax.swing.JLabel jLabel119;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel120;
    private javax.swing.JLabel jLabel121;
    private javax.swing.JLabel jLabel122;
    private javax.swing.JLabel jLabel123;
    private javax.swing.JLabel jLabel124;
    private javax.swing.JLabel jLabel125;
    private javax.swing.JLabel jLabel127;
    private javax.swing.JLabel jLabel128;
    private javax.swing.JLabel jLabel130;
    private javax.swing.JLabel jLabel131;
    private javax.swing.JLabel jLabel132;
    private javax.swing.JLabel jLabel133;
    private javax.swing.JLabel jLabel134;
    private javax.swing.JLabel jLabel135;
    private javax.swing.JLabel jLabel136;
    private javax.swing.JLabel jLabel137;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
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
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JLabel jLabel92;
    private javax.swing.JLabel jLabel93;
    private javax.swing.JLabel jLabel94;
    private javax.swing.JLabel jLabel95;
    private javax.swing.JLabel jLabel96;
    private javax.swing.JLabel jLabel97;
    private javax.swing.JLabel jLabel98;
    private javax.swing.JLabel jLabel99;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
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
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField24;
    private javax.swing.JTextField jTextField25;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField9;
    private javax.swing.JLabel label_browse_tokens_count;
    private javax.swing.JLabel label_browse_userType;
    private javax.swing.JLabel label_browse_user_userCount;
    private javax.swing.JLabel label_browse_user_userCount3;
    private javax.swing.JLabel label_case_reports_count;
    private javax.swing.JLabel label_create_token_newTokenID;
    private javax.swing.JLabel label_homescreen_currentUserID;
    private javax.swing.JLabel label_homescreen_currentUserID1;
    private javax.swing.JLabel label_homescreen_currentUsername;
    private javax.swing.JLabel label_homescreen_custom_revenue;
    private javax.swing.JLabel label_homescreen_systemDate;
    private javax.swing.JLabel label_homescreen_totRev_endDate;
    private javax.swing.JLabel label_homescreen_totRev_startDate;
    private javax.swing.JLabel label_homescreen_totalRevenue;
    private javax.swing.JLabel label_homescreen_userCount;
    private javax.swing.JLabel label_mgt_users_adminCount;
    private javax.swing.JLabel label_mgt_users_cashierCount;
    private javax.swing.JLabel label_mgt_users_cashierCount_active;
    private javax.swing.JLabel label_mgt_users_driverCount;
    private javax.swing.JLabel label_mgt_users_driverCount_active;
    private javax.swing.JLabel label_mgt_users_gateOperatorCount;
    private javax.swing.JLabel label_mgt_users_gateOperatorCount_active;
    private javax.swing.JLabel label_mgt_users_inspectorCount;
    private javax.swing.JLabel label_mgt_users_inspectorCount_active;
    private javax.swing.JLabel label_mgt_users_passengerCount;
    private javax.swing.JLabel label_mgt_users_passengerCount_active;
    private javax.swing.JLabel label_profile_userAddress;
    private javax.swing.JLabel label_profile_userContactNumber;
    private javax.swing.JLabel label_profile_userEmail;
    private javax.swing.JLabel label_profile_userGender;
    private javax.swing.JLabel label_profile_userName;
    private javax.swing.JLabel label_update_user_userID;
    private javax.swing.JLabel label_update_user_userType;
    private javax.swing.JPanel mainMenu_panel;
    private javax.swing.JPanel management_users_panel;
    private javax.swing.JPanel network_usage_panel;
    private javax.swing.JPanel panel_tokenQRCode1;
    private javax.swing.JPanel panel_tokenQRCode2;
    private javax.swing.JPanel panel_tokenQRCode3;
    private javax.swing.JPanel panel_tokenQRCode4;
    private javax.swing.JPanel panel_tokenQRCode5;
    private javax.swing.JPanel panel_userPic;
    private javax.swing.JPanel passenger_info_panel;
    private javax.swing.JPasswordField passwordField_update_user_userPassword;
    private javax.swing.JPanel profile_edit_panel;
    private javax.swing.JPanel profile_panel;
    private javax.swing.JTable table_browse_users;
    private javax.swing.JTable table_browse_users1;
    private javax.swing.JTable table_browse_users2;
    private javax.swing.JTable table_browse_users3;
    private javax.swing.JTable table_case_reports;
    private javax.swing.JTable table_tokens;
    private javax.swing.JTextArea textArea_profile_edit_userAddress;
    private javax.swing.JTextArea textArea_update_user_userAddress;
    private javax.swing.JTextField text_profile_edit_userContactNumber;
    private javax.swing.JTextField text_profile_edit_userEmail;
    private javax.swing.JTextField text_profile_edit_userName;
    private javax.swing.JTextField text_update_user_contactNumber;
    private javax.swing.JTextField text_update_user_userEmail;
    private javax.swing.JTextField text_update_user_userName;
    private javax.swing.JTextField text_userAddress;
    private javax.swing.JTextField text_userContactNumber;
    private javax.swing.JTextField text_userEmail;
    private javax.swing.JTextField text_userID;
    private javax.swing.JTextField text_userPassword;
    private javax.swing.JTextField text_username;
    private javax.swing.JPanel token_create_panel;
    private javax.swing.JPanel token_detail_panel;
    private javax.swing.JPanel token_panel;
    private javax.swing.JPanel update_user_panel;
    // End of variables declaration//GEN-END:variables
}
