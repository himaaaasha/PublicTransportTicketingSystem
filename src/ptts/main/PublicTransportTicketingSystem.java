package ptts.main;

import javax.swing.JFrame;
import ptts.entities.Administrator;
import ptts.entities.Passenger;
import ptts.entities.controllers.AdminController;
import ptts.entities.controllers.DriverController;
import ptts.entities.controllers.GateOperatorController;
import ptts.entities.controllers.InspectorController;
import ptts.entities.controllers.PassengerController;
import ptts.entities.controllers.PayStationController;
import ptts.entities.managers.AdminManager;
import ptts.entities.managers.CaseReportManager;
import ptts.entities.managers.CashierManager;
import ptts.entities.managers.DriverManager;
import ptts.entities.managers.GateOperatorManager;
import ptts.entities.managers.InspectorManager;
import ptts.entities.managers.PassengerManager;
import ptts.entities.managers.TicketManager;
import ptts.entities.managers.TokenManager;
import ptts.entities.managers.TransactionRecordManager;
import ptts.gui.TerminalSelector;


/**
 *
 * @author Siri@MnS
 * 
 */

public class PublicTransportTicketingSystem {

    /**
     * @param args the command line arguments
     */
    //public static JFrame terminalSelector = new TerminalSelector();
    public static JFrame terminalSelector;
    
    //Entity Controllers references
    //relevant entity controllers are instantiated by the User Authentication service
    //according to the selected type of terminal
    public static PassengerController passengerController = null;
    public static AdminController adminController = null;
    public static PayStationController payStationController = null;
    public static InspectorController inspectorController = null;
    public static DriverController driverController = null;
    public static GateOperatorController gateOperatorController = null;
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        
        //loading Entity data by Entity Managers from persistant storage
        PassengerManager.loadPassengerList();
        AdminManager.loadAdminList();
        CashierManager.loadCashierList();
        DriverManager.loadDriverList();
        GateOperatorManager.loadGateOperatorList();
        InspectorManager.loadInspectorList();
        
        TokenManager.loadTokenList();
        TransactionRecordManager.loadTransRecordList();
        TicketManager.loadTicketList();
        CaseReportManager.loadCaseReportList();
        
        
        //adminTest();
        passengerTest();
        //TokenTest();
        //TicketTest();
        
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            
            //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(TerminalSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TerminalSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TerminalSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TerminalSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        //JFrame terminalSelector = new TerminalSelector();
        //terminalSelector.setVisible(true);
        //ptts.gui.TerminalSelector.terminalSelector.setVisible(true);
        //new TerminalSelector().setVisible(true);
        
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                terminalSelector = new TerminalSelector();
                terminalSelector.setVisible(true);
            }
        });
        
        
        
    }

    
    //Test Methods
    
    private static void adminTest() {
        
        if (AdminManager.loadAdminList()) {
            System.out.println("Test Main: Admin List loaded successfully");
            System.out.println("Test Main: Printing Admin List");
            AdminManager.printAdminList();
        }
        else{
            System.out.println("Test Main: NULL Admin List!");
        }
        
        
        Administrator testAdmin = new Administrator("987852654v", 1 , "123",true, "Amal", 2,"0714569785","amal@gmail.com" , "Kosgama");
        Administrator testAdmin2 = new Administrator("A123", 1 , "123", true, "Amal2", 1,"0714569785","amal2@gmail.com" , "Kosgama2");
          
//        AdminManager.addAdministrator(testAdmin);
//       AdminManager.addAdministrator(testAdmin2);

        
    }
    
    private static void passengerTest(){
        if (PassengerManager.loadPassengerList()) {
            System.out.println("Test Main: Passenger List loaded successfully");
            System.out.println("Test Main: Printing Passenger List");
            PassengerManager.printPassengerList();
        }
        else{
            System.out.println("Test Main: NULL Passenger List!");
        }
        
        Passenger testPassenger = new Passenger("P123", 1 , "123", true, "Amal2", 2,"0714569785","amal2@gmail.com" , "Kosgama2");
        testPassenger.setCreditBalance(345.00f);
        PassengerManager.addPassenger(testPassenger);
        
//        if (PassengerManager.loadPassengerList()) {
//            System.out.println("Test Main: Passenger List loaded successfully");
//            System.out.println("Test Main: Printing Passenger List");
//            PassengerManager.printPassengerList();
//        }
//        else{
//            System.out.println("Test Main: NULL Passenger List!");
//        }
    }
    
    private static void TokenTest(){
        if (TokenManager.loadTokenList()) {
            System.out.println("Test Main: Token List loaded successfully");
            System.out.println("Test Main: Printing Token List");
            TokenManager.printTokenList();
        }
        else{
            System.out.println("Test Main: NULL Passenger List!");
        } 
    }
    
    private static void TicketTest(){
        if (TicketManager.loadTicketList()) {
            System.out.println("Test Main: Ticket List loaded successfully");
            System.out.println("Test Main: Printing Ticket List");
            TicketManager.printTicketList();
        }
        else{
            System.out.println("Test Main: NULL Passenger List!");
        } 
    }
}
