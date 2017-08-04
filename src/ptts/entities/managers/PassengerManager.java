
package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.Passenger;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;


/**
 *
 * Represents Passenger Manager class
 */
public class PassengerManager {

    private static ArrayList<Passenger> passengerList = null;    
    private static final String FILE_NAME_PASSENGERS = "passengers.ser";
    private Passenger testPassenger = new Passenger("123456789v",2, "123", true, "Amali", 2,"0714569785","amali@gmail.com" , "Ragama");
    
    public static Passenger getPassengerByID(String userID) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Passenger tempPassenger =  null;
        if (passengerList!=null) {
            for (Passenger _passenger : passengerList) {
                if(_passenger.getUserID().equals(userID))
                    tempPassenger =  _passenger;
            }
            return tempPassenger;
        } else {
            System.out.println("Passenger Manager [getPassengerByID]: Null PassengerList!!!");
            return tempPassenger;
        }
    }
    
    public static boolean loadPassengerList(){
        passengerList = DAController.deSerializePassengerData(FILE_NAME_PASSENGERS);
        if(passengerList != null){
            System.out.println("Passenger Manager: Passenger List not NULL");
            return true;
        } else {
            System.out.println("Passenger Manager: Passenger List is NULL");
            return false;
        }
    }
 
    public static boolean addPassenger(Passenger passenger){
        if (passengerList == null) {
            passengerList = new ArrayList<>();
            passengerList.add(passenger);
        } else {
            if (getPassengerByID(passenger.getUserID()) == null) { //check whether the user is already registered in the system
                passengerList.add(passenger);
                System.out.println("Passenger Manager: Passenger is added to the tempList");
            } else {
                System.out.println("Passenger Manager: User("+ passenger.getUserID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "Passenger Manager: User("+ passenger.getUserID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(passengerList, FILE_NAME_PASSENGERS)) {
            System.out.println("Passenger Manager: Passenger data file serialized successfully");
            if (loadPassengerList()) {
                System.out.println("Passenger Manager: Passenger list loaded successfully");
                return true;
            } else {
                System.out.println("Passenger Manager: ERROR: Passenger list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Passenger Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean updatePassenger(String oldPassengerID, Passenger updatedPassenger) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Passenger oldPassenger = getPassengerByID(oldPassengerID);
        
        passengerList.set(passengerList.indexOf(oldPassenger),updatedPassenger);
        
        if (serialize(passengerList, FILE_NAME_PASSENGERS)) {
            System.out.println("Passenger Manager: Passenger data file serialized successfully");
            if (loadPassengerList()) {
                System.out.println("Passenger Manager: Passenger list loaded successfully");
                return true;
            } else {
                System.out.println("Passenger Manager: ERROR: Passenger list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Passenger Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean deletePassenger(Passenger passenger){
        passengerList.remove(passenger);
        
        if (serialize(passengerList, FILE_NAME_PASSENGERS)) {
            System.out.println("Passenger Manager: Passenger data file serialized successfully");
            if (loadPassengerList()) {
                System.out.println("Passenger Manager: Passenger list loaded successfully");
                return true;
            } else {
                System.out.println("Passenger Manager: ERROR: Passenger list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Passenger Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<Passenger> getPassengerList(){
        return passengerList;
    }
    
    public static void getActivePassengerList(){
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public static void printPassengerList(){
        //throw new UnsupportedOperationException("Not supported yet.");
        if (passengerList!=null) {
            for (Passenger _passenger : passengerList) {
                System.out.println(_passenger.getUserID()+":"+ _passenger.getCreditBalance());
            }
        } else {
            System.out.println("Null Passenger List!!!");
        }
    }
}
