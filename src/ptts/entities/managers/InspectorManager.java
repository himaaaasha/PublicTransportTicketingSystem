package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.Inspector;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represent Inspector Manager Class
 */
public class InspectorManager {
    private static ArrayList<Inspector> gateOperatorListList = null;    
    private static final String FILE_NAME_INSPECTORS = "inspectors.ser";
    
    public static Inspector getInspectorByID(String inspectorID) {
        Inspector inspector =  null;
        if (gateOperatorListList!=null) {
            for (Inspector _inspector : gateOperatorListList) {
                if(_inspector.getUserID().equals(inspectorID))
                    inspector =  _inspector;
            }
            return inspector;
        } else {
            System.out.println("Inspector Manager [getInspectorByID]: Null InspectorList!!!");
            return inspector;
        }
    }
    
    public static boolean loadInspectorList(){
        gateOperatorListList = DAController.deSerializeInspectorData(FILE_NAME_INSPECTORS);
        if(gateOperatorListList != null){
            System.out.println("Inspector Manager: Inspector List not NULL");
            return true;
        } else {
            System.out.println("Inspector Manager: Inspector List is NULL");
            return false;
        }
    }
 
    public static boolean addInspector(Inspector inspector){
        if (gateOperatorListList == null) {
            gateOperatorListList = new ArrayList<>();
            gateOperatorListList.add(inspector);
        } else {
            if (getInspectorByID(inspector.getUserID()) == null) {
                gateOperatorListList.add(inspector);
                System.out.println("Inspector Manager: Inspector is added to the tempList");
            } else {
                System.out.println("Inspector Manager: User("+ inspector.getUserID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "Inspector Manager: User("+ inspector.getUserID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(gateOperatorListList, FILE_NAME_INSPECTORS)) {
            System.out.println("Inspector Manager: Inspector data file serialized successfully");
            if (loadInspectorList()) {
                System.out.println("Inspector Manager: Inspector list loaded successfully");
                return true;
            } else {
                System.out.println("Inspector Manager: ERROR: Inspector list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Inspector Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean updateInspector(Inspector currentInspector, Inspector updatedInspector) {
        gateOperatorListList.set(gateOperatorListList.indexOf(currentInspector),updatedInspector);
        
        if (serialize(gateOperatorListList, FILE_NAME_INSPECTORS)) {
            System.out.println("Inspector Manager: Inspector data file serialized successfully");
            if (loadInspectorList()) {
                System.out.println("Inspector Manager: Inspector list loaded successfully");
                return true;
            } else {
                System.out.println("Inspector Manager: ERROR: Inspector list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Inspector Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean deleteInspector(Inspector inspector){
        gateOperatorListList.remove(inspector);
        
        if (serialize(gateOperatorListList, FILE_NAME_INSPECTORS)) {
            System.out.println("Inspector Manager: Inspector data file serialized successfully");
            if (loadInspectorList()) {
                System.out.println("Inspector Manager: Inspector list loaded successfully");
                return true;
            } else {
                System.out.println("Inspector Manager: ERROR: Inspector list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Inspector Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<Inspector> getInspectorList(){
        return gateOperatorListList;
    }
    
    public static void printInspectorList(){
        if (gateOperatorListList!=null) {
            for (Inspector inspector : gateOperatorListList) {
                System.out.println(inspector.getUserID());
            }
        } else {
            System.out.println("Null Inspector List!!!");
        }
    }
}
