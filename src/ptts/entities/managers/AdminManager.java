
package ptts.entities.managers;

import java.util.ArrayList;
import ptts.entities.Administrator;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents administrator Manager class
 */
public class AdminManager {
    private static ArrayList<Administrator> adminList = null;
    private static final String FILE_NAME_ADMINS = "administrators.ser";
    //private Administrator testAdmin = new Administrator("987852654v",2, "123", "Amal", 2,"0714569785","amal@gmail.com" , "Kosgama");
    
    public static Administrator getAdminByID(String userID) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Administrator tempAdmin =  null;
        if (adminList!=null) {
            for (Administrator _admin : adminList) {
                if(_admin.getUserID().equals(userID))
                    tempAdmin =  _admin;
            }
            return tempAdmin;
        } else{
            System.out.println("Admin Manager [getAdminByID]: Null AdminList!!!");
            return tempAdmin;
        }
    }
    
    public static boolean loadAdminList(){
        adminList = DAController.deSerializeAdminData(FILE_NAME_ADMINS);
        if(adminList != null){
            System.out.println("Admin Manager: Admin List not NULL");
            return true;
        } else{
            System.out.println("Admin Manager: Admin List is NULL");
            return false;
        }
    }
    
    public static boolean addAdministrator(Administrator admin){
        if (adminList == null) {
            adminList = new ArrayList<>();
            adminList.add(admin);
        } else{
            if (getAdminByID(admin.getUserID()) == null) { //check whether the user is already registered in the system
                adminList.add(admin);
                System.out.println("Admin Manager: Admin is added to the tempList");
            } else{
                System.out.println("Admin Manager: User("+ admin.getUserID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(adminList, FILE_NAME_ADMINS)) {
            System.out.println("Admin Manager: Admin data file serialized successfully");
            if(loadAdminList()){
                System.out.println("Admin Manager: Admin list loaded successfully");
                return true;
            } else{
                System.out.println("Admin Manager: ERROR: Admin list NOT loaded!");
                return true;
            } 
        } else{
            System.out.println("Admin Manager: Serialization Error!!");
            return false;
        }   
    }
    
    public static ArrayList<Administrator> getAdminList(){
        return adminList;
    }
    
    public static boolean updateAdministrator(String oldUserID, Administrator updatedAdmin) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
        Administrator oldAdmin = getAdminByID(oldUserID);
        
        
        adminList.set(adminList.indexOf(oldAdmin),updatedAdmin);
        
        if (serialize(adminList, FILE_NAME_ADMINS)) {
            System.out.println("Admin Manager: Admin data file serialized successfully");
            if(loadAdminList()){
                System.out.println("Admin Manager: Admin list loaded successfully");
                return true;       
            }
            else {
                System.out.println("Admin Manager: ERROR: Admin list NOT loaded!");
                return true;
            } 
    
        }
        else{
            System.out.println("Admin Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean deleteAdministrator(Administrator admin){
        adminList.remove(admin);
        
        if (serialize(adminList, FILE_NAME_ADMINS)) {
            System.out.println("Admin Manager: Admin data file serialized successfully");
            if(loadAdminList()){
                System.out.println("Admin Manager: Admin list loaded successfully");
                return true;
            }
            else{
                System.out.println("Admin Manager: ERROR: Admin list NOT loaded!");
                return true;
            } 
        }
        else{
            System.out.println("Admin Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static void printAdminList(){
        if (adminList!=null) {
            for (Administrator _admin : adminList) {
                System.out.println(_admin.getUserID());
            }
        }
        else{
            System.out.println("Null AdminList!!!");
        }
    
    }
}
