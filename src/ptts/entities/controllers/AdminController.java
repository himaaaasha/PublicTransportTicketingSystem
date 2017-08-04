
package ptts.entities.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import ptts.entities.Administrator;
import ptts.entities.Cashier;
import ptts.entities.Driver;
import ptts.entities.GateOperator;
import ptts.entities.Inspector;
import ptts.entities.Passenger;
import ptts.entities.Token;
import ptts.entities.TransactionRecord;
import ptts.entities.User;
import ptts.entities.managers.AdminManager;
import ptts.entities.managers.CashierManager;
import ptts.entities.managers.DriverManager;
import ptts.entities.managers.GateOperatorManager;
import ptts.entities.managers.InspectorManager;
import ptts.entities.managers.PassengerManager;
import ptts.entities.managers.TokenManager;
import ptts.entities.managers.TransactionRecordManager;

/**
 *
 * @author Siri@MnS
 * 
 * ManagerController
 * Class Description:
 * 
 */
public class AdminController {
    
    //<editor-fold defaultstate="collapsed" desc="Singleton implementation">
    private static AdminController instance= null;
    private AdminController(Administrator admin){ //stop creation of instances by other entities
        this.currentAdmin = admin;
        loadTempUserLists();  
    }

    
    public static AdminController getInstance(Administrator admin){
        if (instance == null) {
            instance = new AdminController(admin);
        }
        return instance;
    }
//</editor-fold>

    private Administrator currentAdmin=null;
    
    private ArrayList<Passenger> tempPassengerList = null;
    private ArrayList<Administrator> tempAdminList = null;
    private ArrayList<TransactionRecord> currentTransactionRecordList = null;
    
    public void updateCurrentAdmin(){
        currentAdmin = AdminManager.getAdminByID(currentAdmin.getUserID());
    }
    
    private void loadTempUserLists() {
        //load temp user lists
        tempAdminList = AdminManager.getAdminList();
        tempPassengerList = PassengerManager.getPassengerList();
    }
    
    public Administrator getCurrentAdmin(){
        return this.currentAdmin;
    }
    
    public boolean addUser(User user, int userType, String userID){
        boolean status =false;
        switch(userType){
            case 1:
                Administrator tempAdmin = new Administrator(user.getUserID(),1, user.getUserPassword(), user.isActive(), user.getName(),
                                                            user.getGender(), user.getContactNumber(),user.getEmail(), user.getAddress());
                
                if(AdminManager.addAdministrator(tempAdmin)){
                    System.out.println("Admin Controller: New Admin("+ userID +") has been added to the system");
                    status=true;
                }
                else{
                    System.out.println("Admin Controller: ERROR! New Admin("+ userID +") has NOT been added to the system!!!");
                }
                break;
                
            case 2:
                Passenger tempPassenger = new Passenger(user.getUserID(),1, user.getUserPassword(), user.isActive(),user.getName(),
                                                            user.getGender(), user.getContactNumber(),user.getEmail(), user.getAddress()); 
                
                if(PassengerManager.addPassenger(tempPassenger)){
                    System.out.println("Admin Controller: New Passenger("+ userID +") has been added to the system");
                    status = true;
                }
                else{
                    System.out.println("Admin Controller: ERROR! New Passenger("+ userID +") has NOT been added to the system!!!");
                }
                break;
                
            case 3:
                Cashier tempCashier = new Cashier(user.getUserID(),1, user.getUserPassword(), user.isActive(),user.getName(),
                                                            user.getGender(), user.getContactNumber(),user.getEmail(), user.getAddress()); 
                
                if(CashierManager.addCashier(tempCashier)){
                    System.out.println("Admin Controller: New Cashier("+ userID +") has been added to the system");
                    status = true;
                }
                else{
                    System.out.println("Admin Controller: ERROR! New Cashier("+ userID +") has NOT been added to the system!!!");
                }
                break;
                
            case 4:
                Inspector tempInspector = new Inspector(user.getUserID(),1, user.getUserPassword(), user.isActive(),user.getName(),
                                                            user.getGender(), user.getContactNumber(),user.getEmail(), user.getAddress()); 
                
                if(InspectorManager.addInspector(tempInspector)){
                    System.out.println("Admin Controller: New Inspector("+ userID +") has been added to the system");
                    status = true;
                }
                else{
                    System.out.println("Admin Controller: ERROR! New Inspector("+ userID +") has NOT been added to the system!!!");
                }
                break;
                
            case 5:
                Driver tempDriver = new Driver(user.getUserID(),1, user.getUserPassword(), user.isActive(),user.getName(),
                                                            user.getGender(), user.getContactNumber(),user.getEmail(), user.getAddress()); 
                
                if(DriverManager.addDriver(tempDriver)){
                    System.out.println("Admin Controller: New Driver("+ userID +") has been added to the system");
                    status = true;
                }
                else{
                    System.out.println("Admin Controller: ERROR! New Driver("+ userID +") has NOT been added to the system!!!");
                }
                break;
                
            case 6:
                GateOperator  tempGateOperator= new GateOperator(user.getUserID(),1, user.getUserPassword(), user.isActive(),user.getName(),
                                                            user.getGender(), user.getContactNumber(),user.getEmail(), user.getAddress()); 
                
                if(GateOperatorManager.addGateOperator(tempGateOperator)){
                    System.out.println("Admin Controller: New Gate Operator("+ userID +") has been added to the system");
                    status = true;
                }
                else{
                    System.out.println("Admin Controller: ERROR! New Gate Operator("+ userID +") has NOT been added to the system!!!");
                }
                break;  
                
            default:
                
                System.out.println("Admin Controller: Incorrect User Type(-1) !!!");
        }
        return status;
    }

    public boolean updateUser(String userID,User updatedUser, int userType) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        boolean status = false;
        switch(userType){
            case 1:
            
                Administrator updatedAdmin = new Administrator(updatedUser.getUserID(),1, updatedUser.getUserPassword(), updatedUser.isActive(), updatedUser.getName(),
                                                            updatedUser.getGender(), updatedUser.getContactNumber(),updatedUser.getEmail(), updatedUser.getAddress());
                
                
                if(AdminManager.updateAdministrator(userID,updatedAdmin)){
                    System.out.println("Admin Controller: Admin("+ userID +") has been updated");
                    this.currentAdmin = updatedAdmin;
                    status=true;
                }
                else{
                    System.out.println("Admin Controller: ERROR! New Admin("+ userID +") has NOT been updated!!!");
                }
                break;
                
            case 2:
                
        }
        
        return status;
    }
    
    public boolean createToken(String tokenID, String passengerID){
        boolean status= false;
        Token tempToken = new Token(tokenID, passengerID); 
        
        Passenger _passenger = PassengerManager.getPassengerByID(passengerID);
        
        if(TokenManager.addToken(tempToken)){
            _passenger.setToken(tempToken);
            if(PassengerManager.updatePassenger(passengerID, _passenger)){
                status = true;
            }
        }
        else{
            status = false;
        }
        
        return status;
    }
    
    private String generateTokenID(){
        int tokenCount;
        if (TokenManager.getTokenList() != null) {
            tokenCount = TokenManager.getTokenList().size();
        }
        else{
            tokenCount =0;
        }
        StringBuilder sb = new StringBuilder("T");
        sb.append(Integer.toString(tokenCount+1));
        
        return sb.toString();
    }
    
    
    public boolean loadTransactionRecordList(){
        try {
            currentTransactionRecordList = TransactionRecordManager.getTransRecordList();
            return true;
            
        } catch (Exception e) {
            System.out.println("Admin Terminal: Homescreen: Null TransRec List!");
            return false;
        }
    }
    
    public ArrayList<TransactionRecord> getTransactionRecordList(){
        return currentTransactionRecordList;
    }
    
    public float calculateTotalRevenue(){
        float amount = 0.0f;
        if (loadTransactionRecordList()) {
            for (TransactionRecord _tr : currentTransactionRecordList) {
                if (_tr.getPassengerID().equals("Guest")) {
                    amount += _tr.getAmount();
                }
                if (_tr.getPassengerID().contains("Account Topup")) {
                    amount += _tr.getAmount();
                }
            }
            System.out.println("Admin terminal: Total Revenue: " + amount);
            return amount;
        }
        else{
            System.out.println("Admin terminal: Transaction record list loading error!");
            return -1.0f;
        }
    }
    
    public float calculateTotalRevenue_customRange(Calendar startDate, Calendar endDate){
        float amount = 0.0f;
        startDate.add(Calendar.DATE, -1);
        endDate.add(Calendar.DATE, 1);
        
        if (loadTransactionRecordList()) {
            for (TransactionRecord _tr : currentTransactionRecordList) {

                if (_tr.getDate().after(startDate) && _tr.getDate().before(endDate)) {
                    if (_tr.getPassengerID().equals("Guest")) {
                        amount += _tr.getAmount();
                    }
                    if (_tr.getPassengerID().contains("Account Topup")) {
                        amount += _tr.getAmount();
                    }
                }
            }
            System.out.println("Admin terminal: Total Revenue: " + amount);
            return amount;
        }
        else{
            System.out.println("Admin terminal: Transaction record list loading error!");
            return -1.0f;
        }
    }
    
}
