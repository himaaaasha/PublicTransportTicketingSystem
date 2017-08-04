package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.GateOperator;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents Gate operator class
 */
public class GateOperatorManager {
    private static ArrayList<GateOperator> gateOperatorListList = null;    
    private static final String FILE_NAME_GATEOPERATORS = "gateoperators.ser";
    
    public static GateOperator getGateOperatorByID(String gateOpID) {
        GateOperator gateOperator =  null;
        if (gateOperatorListList!=null) {
            for (GateOperator _gateOperator : gateOperatorListList) {
                if(_gateOperator.getUserID().equals(gateOpID))
                    gateOperator =  _gateOperator;
            }
            return gateOperator;
        } else {
            System.out.println("GateOperator Manager [getGateOperatorByID]: Null GateOperatorList!!!");
            return gateOperator;
        }
    }
    
    public static boolean loadGateOperatorList(){
        gateOperatorListList = DAController.deSerializeGateOperatorData(FILE_NAME_GATEOPERATORS);
        if(gateOperatorListList != null){
            System.out.println("GateOperator Manager: GateOperator List not NULL");
            return true;
        } else {
            System.out.println("GateOperator Manager: GateOperator List is NULL");
            return false;
        }
    }
 
    public static boolean addGateOperator(GateOperator gateOperator){
        if (gateOperatorListList == null) {
            gateOperatorListList = new ArrayList<>();
            gateOperatorListList.add(gateOperator);
        } else {
            if (getGateOperatorByID(gateOperator.getUserID()) == null) {
                gateOperatorListList.add(gateOperator);
                System.out.println("GateOperator Manager: GateOperator is added to the tempList");
            } else {
                System.out.println("GateOperator Manager: User("+ gateOperator.getUserID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "GateOperator Manager: User("+ gateOperator.getUserID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(gateOperatorListList, FILE_NAME_GATEOPERATORS)) {
            System.out.println("GateOperator Manager: GateOperator data file serialized successfully");
            if (loadGateOperatorList()) {
                System.out.println("GateOperator Manager: GateOperator list loaded successfully");
                return true;
            } else {
                System.out.println("GateOperator Manager: ERROR: GateOperator list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("GateOperator Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean updateGateOperator(GateOperator currentGateOperator, GateOperator updatedGateOperator) {
        gateOperatorListList.set(gateOperatorListList.indexOf(currentGateOperator),updatedGateOperator);
        
        if (serialize(gateOperatorListList, FILE_NAME_GATEOPERATORS)) {
            System.out.println("GateOperator Manager: GateOperator data file serialized successfully");
            if (loadGateOperatorList()) {
                System.out.println("GateOperator Manager: GateOperator list loaded successfully");
                return true;
            } else {
                System.out.println("GateOperator Manager: ERROR: GateOperator list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("GateOperator Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean deleteGateOperator(GateOperator gateOp){
        gateOperatorListList.remove(gateOp);
        
        if (serialize(gateOperatorListList, FILE_NAME_GATEOPERATORS)) {
            System.out.println("GateOperator Manager: GateOperator data file serialized successfully");
            if (loadGateOperatorList()) {
                System.out.println("GateOperator Manager: GateOperator list loaded successfully");
                return true;
            } else {
                System.out.println("GateOperator Manager: ERROR: GateOperator list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("GateOperator Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<GateOperator> getGateOperatorList(){
        return gateOperatorListList;
    }
    
    public static void printGateOperatorList(){
        if (gateOperatorListList!=null) {
            for (GateOperator gateOp : gateOperatorListList) {
                System.out.println(gateOp.getUserID());
            }
        } else {
            System.out.println("Null GateOperator List!!!");
        }
    }
}
