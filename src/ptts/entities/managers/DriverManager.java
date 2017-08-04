package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.Driver;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents Driver Manager Class
 */
public class DriverManager {
    private static ArrayList<Driver> driverList = null;    
    private static final String FILE_NAME_DRIVERS = "drivers.ser";
    
    public static Driver getDriverByID(String driverID) {
        Driver driver =  null;
        if (driverList!=null) {
            for (Driver _driver : driverList) {
                if(_driver.getUserID().equals(driverID))
                    driver =  _driver;
            }
            return driver;
        } else {
            System.out.println("Driver Manager [getDriverByID]: Null DriverList!!!");
            return driver;
        }
    }
    
    public static boolean loadDriverList(){
        driverList = DAController.deSerializeDriverData(FILE_NAME_DRIVERS);
        if(driverList != null){
            System.out.println("Driver Manager: Driver List not NULL");
            return true;
        } else {
            System.out.println("Driver Manager: Driver List is NULL");
            return false;
        }
    }
 
    public static boolean addDriver(Driver driver){
        if (driverList == null) {
            driverList = new ArrayList<>();
            driverList.add(driver);
        } else {
            if (getDriverByID(driver.getUserID()) == null) {
                driverList.add(driver);
                System.out.println("Driver Manager: Driver is added to the tempList");
            } else {
                System.out.println("Driver Manager: User("+ driver.getUserID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "Driver Manager: User("+ driver.getUserID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(driverList, FILE_NAME_DRIVERS)) {
            System.out.println("Driver Manager: Driver data file serialized successfully");
            if (loadDriverList()) {
                System.out.println("Driver Manager: Driver list loaded successfully");
                return true;
            } else {
                System.out.println("Driver Manager: ERROR: Driver list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Driver Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean updateDriver(Driver currentDriver, Driver updatedDriver) {
        driverList.set(driverList.indexOf(currentDriver),updatedDriver);
        
        if (serialize(driverList, FILE_NAME_DRIVERS)) {
            System.out.println("Driver Manager: Driver data file serialized successfully");
            if (loadDriverList()) {
                System.out.println("Driver Manager: Driver list loaded successfully");
                return true;
            } else {
                System.out.println("Driver Manager: ERROR: Driver list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Driver Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean deleteDriver(Driver driver){
        driverList.remove(driver);
        
        if (serialize(driverList, FILE_NAME_DRIVERS)) {
            System.out.println("Driver Manager: Driver data file serialized successfully");
            if (loadDriverList()) {
                System.out.println("Driver Manager: Driver list loaded successfully");
                return true;
            } else {
                System.out.println("Driver Manager: ERROR: Driver list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Driver Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<Driver> getDriverList(){
        return driverList;
    }
    
    public static void printDriverList(){
        if (driverList!=null) {
            for (Driver driver : driverList) {
                System.out.println(driver.getUserID());
            }
        } else {
            System.out.println("Null Driver List!!!");
        }
    }
}
