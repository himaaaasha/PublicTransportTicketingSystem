
package ptts.services;

import javax.swing.JOptionPane;
import ptts.entities.Administrator;
import ptts.entities.Cashier;
import ptts.entities.Driver;
import ptts.entities.GateOperator;
import ptts.entities.Inspector;
import ptts.entities.Passenger;
import ptts.entities.User;
import ptts.entities.controllers.AdminController;
import ptts.entities.controllers.DriverController;
import ptts.entities.controllers.GateOperatorController;
import ptts.entities.controllers.InspectorController;
import ptts.entities.controllers.PassengerController;
import ptts.entities.controllers.PayStationController;
import ptts.entities.managers.AdminManager;
import ptts.entities.managers.CashierManager;
import ptts.entities.managers.DriverManager;
import ptts.entities.managers.GateOperatorManager;
import ptts.entities.managers.InspectorManager;
import ptts.entities.managers.PassengerManager;
import ptts.gui.admin.AdminTerminal;
import ptts.gui.driver.DriverTerminal;
import ptts.gui.gateoperator.GateOperatorTerminal;
import ptts.gui.inspector.InspectorTerminal;
import ptts.gui.passenger.PassengerTerminal;
import ptts.gui.paystation.PayStationTerminal;
import ptts.main.PublicTransportTicketingSystem;

/**
 *
 * @author Siri@MnS
 * 
 * UserAuthentication - Static Class
 */
public class  UserAuthentication {
    
    private static User currentUser = null;
    //private int userType = -1;
    
    public static boolean searchUser(int userType, String userID, String userPassword){
        boolean status = false;
        switch(userType){
            
            case 1:
                Administrator tempAdmin = null;
                tempAdmin = AdminManager.getAdminByID(userID);
                if(tempAdmin != null){
                    if(userPassword.equals(tempAdmin.getUserPassword())){
                        PublicTransportTicketingSystem.adminController = AdminController.getInstance(tempAdmin);
                        //new AdminTerminal().setVisible(true);
                        //ptts.gui.admin.AdminTerminal.adminTerminal.setVisible(true);
                        ptts.gui.admin.AdminTerminal.adminTerminal = new AdminTerminal();
                        ptts.gui.admin.AdminTerminal.adminTerminal.setVisible(true);
                        status = true;
                    }
                    else{
                        System.out.println("UserID and Password do not match!!!");
                        JOptionPane.showMessageDialog(null, "UserID and Password do not match!");
                        status = false;
                    }
                }
                else {
                    System.out.println("Administrator("+userID+") is not registered in the system");
                    JOptionPane.showMessageDialog(null, "Administrator("+userID+") is not registered in the system");
                    status = false;
                }
                break;
                
            case 2:
                Passenger tempPassenger = null;
                tempPassenger = PassengerManager.getPassengerByID(userID);
                if(tempPassenger != null){
                    if(userPassword.equals(tempPassenger.getUserPassword())){
                        PublicTransportTicketingSystem.passengerController = PassengerController.getInstance(tempPassenger);
                        //ptts.gui.passenger.PassengerTerminal.passengerTerminal.setVisible(true);
                        ptts.gui.passenger.PassengerTerminal.passengerTerminal= new PassengerTerminal();
                        ptts.gui.passenger.PassengerTerminal.passengerTerminal.setVisible(true);
                        status = true;
                    }
                    else{
                        System.out.println("UserID and Password do not match!!!");
                        JOptionPane.showMessageDialog(null, "UserID and Password do not match!");
                        status = false;
                    }
                }
                else {
                    System.out.println("Passenger("+userID+") is not registered in the system");
                    JOptionPane.showMessageDialog(null, "Passenger("+userID+") is not registered in the system");
                    status = false;
                }
                break;
                
            case 3:
                Cashier tempCashier = null;
                tempCashier = CashierManager.getCashierByID(userID);
                if(tempCashier != null){
                    if(userPassword.equals(tempCashier.getUserPassword())){
                        PublicTransportTicketingSystem.payStationController = PayStationController.getInstance(tempCashier);
                        //ptts.gui.paystation.PayStationTerminal.payStationTerminal.setVisible(true);
                        ptts.gui.paystation.PayStationTerminal.payStationTerminal = new PayStationTerminal();
                        ptts.gui.paystation.PayStationTerminal.payStationTerminal.setVisible(true);
                        status = true;
                    }
                    else{
                        System.out.println("UserID and Password do not match!!!");
                        JOptionPane.showMessageDialog(null, "UserID and Password do not match!");
                        status = false;
                    }
                }
                else {
                    System.out.println("Cashier("+userID+") is not registered in the system");
                    JOptionPane.showMessageDialog(null, "Cashier("+userID+") is not registered in the system");
                    status = false;
                }
                break;
                
            case 4:
                Inspector tempInspector = null;
                tempInspector = InspectorManager.getInspectorByID(userID);
                if(tempInspector != null){
                    if(userPassword.equals(tempInspector.getUserPassword())){
                        PublicTransportTicketingSystem.inspectorController = InspectorController.getInstance(tempInspector);
                        //ptts.gui.inspector.InspectorTerminal.inspectorTerminal.setVisible(true);
                        ptts.gui.inspector.InspectorTerminal.inspectorTerminal = new InspectorTerminal();
                        ptts.gui.inspector.InspectorTerminal.inspectorTerminal.setVisible(true);
                        status = true;
                    }
                    else{
                        System.out.println("UserID and Password do not match!!!");
                        JOptionPane.showMessageDialog(null, "UserID and Password do not match!");
                        status = false;
                    }
                }
                else {
                    System.out.println("Inspector("+userID+") is not registered in the system");
                    JOptionPane.showMessageDialog(null, "Inspector("+userID+") is not registered in the system");
                    status = false;
                }
                break;
                
            case 5:
                Driver tempDriver = null;
                tempDriver = DriverManager.getDriverByID(userID);
                if(tempDriver != null){
                    if(userPassword.equals(tempDriver.getUserPassword())){
                        PublicTransportTicketingSystem.driverController = DriverController.getInstance(tempDriver);
                        //ptts.gui.driver.DriverTerminal.driverTerminal.setVisible(true);
                        ptts.gui.driver.DriverTerminal.driverTerminal = new DriverTerminal();
                        ptts.gui.driver.DriverTerminal.driverTerminal.setVisible(true);
                        
                        
                        status = true;
                    }
                    else{
                        System.out.println("UserID and Password do not match!!!");
                        JOptionPane.showMessageDialog(null, "UserID and Password do not match!");
                        status = false;
                    }
                }
                else {
                    System.out.println("Driver("+userID+") is not registered in the system");
                    JOptionPane.showMessageDialog(null, "Driver("+userID+") is not registered in the system");
                    status = false;
                }
                break;
                
            case 6:
                GateOperator tempGateOperator = null;
                tempGateOperator = GateOperatorManager.getGateOperatorByID(userID);
                if(tempGateOperator != null){
                    if(userPassword.equals(tempGateOperator.getUserPassword())){
                        PublicTransportTicketingSystem.gateOperatorController = GateOperatorController.getInstance(tempGateOperator);
                        //ptts.gui.gateoperator.GateOperatorTerminal.gateOperatorTerminal.setVisible(true);
                        ptts.gui.gateoperator.GateOperatorTerminal.gateOperatorTerminal= new GateOperatorTerminal();
                        ptts.gui.gateoperator.GateOperatorTerminal.gateOperatorTerminal.setVisible(true);
                        status = true;
                    }
                    else{
                        System.out.println("UserID and Password do not match!!!");
                        JOptionPane.showMessageDialog(null, "UserID and Password do not match!");
                        status = false;
                    }
                }
                else {
                    System.out.println("Gate Operator("+userID+") is not registered in the system");
                    JOptionPane.showMessageDialog(null, "Gate Operator("+userID+") is not registered in the system");
                    status = false;
                }
                break;

            default:
                System.out.println("User type is not recognized!!!");
        }
        return status;
    }
}
