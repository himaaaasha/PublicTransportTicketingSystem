package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.Cashier;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents cashier manager class
 */
public class CashierManager {
    private static ArrayList<Cashier> cachierList = null;    
    private static final String FILE_NAME_CASHIERS = "cashiers.ser";
    
    public static Cashier getCashierByID(String cashierID) {
        Cashier cashier =  null;
        if (cachierList!=null) {
            for (Cashier _cashier : cachierList) {
                if(_cashier.getUserID().equals(cashierID))
                    cashier =  _cashier;
            }
            return cashier;
        } else {
            System.out.println("Cashier Manager [getCashierByID]: Null CashierList!!!");
            return cashier;
        }
    }
    
    public static boolean loadCashierList(){
        cachierList = DAController.deSerializeCashierData(FILE_NAME_CASHIERS);
        if(cachierList != null){
            System.out.println("Cashier Manager: Cashier List not NULL");
            return true;
        } else {
            System.out.println("Cashier Manager: Cashier List is NULL");
            return false;
        }
    }
 
    public static boolean addCashier(Cashier cashier){
        if (cachierList == null) {
            cachierList = new ArrayList<>();
            cachierList.add(cashier);
        } else {
            if (getCashierByID(cashier.getUserID()) == null) {
                cachierList.add(cashier);
                System.out.println("Cashier Manager: Cashier is added to the tempList");
            } else {
                System.out.println("Cashier Manager: User("+ cashier.getUserID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "Cashier Manager: User("+ cashier.getUserID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(cachierList, FILE_NAME_CASHIERS)) {
            System.out.println("Cashier Manager: Cashier data file serialized successfully");
            if (loadCashierList()) {
                System.out.println("Cashier Manager: Cashier list loaded successfully");
                return true;
            } else {
                System.out.println("Cashier Manager: ERROR: Cashier list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Cashier Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean updateCashier(Cashier currentCachier, Cashier updatedCashier) {
        cachierList.set(cachierList.indexOf(currentCachier),updatedCashier);
        
        if (serialize(cachierList, FILE_NAME_CASHIERS)) {
            System.out.println("Cashier Manager: Cashier data file serialized successfully");
            if (loadCashierList()) {
                System.out.println("Cashier Manager: Cashier list loaded successfully");
                return true;
            } else {
                System.out.println("Cashier Manager: ERROR: Cashier list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Cashier Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean deleteCashier(Cashier cashier){
        cachierList.remove(cashier);
        
        if (serialize(cachierList, FILE_NAME_CASHIERS)) {
            System.out.println("Cashier Manager: Cashier data file serialized successfully");
            if (loadCashierList()) {
                System.out.println("Cashier Manager: Cashier list loaded successfully");
                return true;
            } else {
                System.out.println("Cashier Manager: ERROR: Cashier list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Cashier Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<Cashier> getCashierList(){
        return cachierList;
    }
    
    public static void printCashierList(){
        if (cachierList!=null) {
            for (Cashier cashier : cachierList) {
                System.out.println(cashier.getUserID());
            }
        } else {
            System.out.println("Null Cashier List!!!");
        }
    }
}
