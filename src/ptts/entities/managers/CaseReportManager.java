
package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.CaseReport;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents Case Report Manager Class
 */
public class CaseReportManager {
    private static ArrayList<CaseReport> caseReportList = null;
    private static final String FILE_NAME_CASE_REPORTS = "case_reports.ser";
    
    public static boolean loadCaseReportList(){
        caseReportList = DAController.deSerializeCaseReportData(FILE_NAME_CASE_REPORTS);
        if(caseReportList != null){
            System.out.println("caseReportList Manager: caseReportList List not NULL");
            return true;
        } else {
            System.out.println("caseReportList Manager: caseReportList List is NULL");
            return false;
        }
    }
    
    public static CaseReport getCaseReportByID(String id) {
        //throw new UnsupportedOperationException("Not supported yet.");
        CaseReport tempCaseReport =  null;
        if (caseReportList!=null) {
            for (CaseReport _caseReport : caseReportList) {
                if(_caseReport.getReportID().equals(id))
                    tempCaseReport =  _caseReport;
            }
            return tempCaseReport;
        } else {
            System.out.println("CaseReport Manager [getCaseReportByID]: Null CaseReportList!!!");
            return tempCaseReport;
        }
    }
    
    public static ArrayList<CaseReport> getCaseReportList(){
        return caseReportList;
    }
    
    public static boolean addCaseReport(CaseReport caseReport){
        if (caseReportList == null) {
            caseReportList = new ArrayList<>();
            caseReportList.add(caseReport);
        } else {
            if (getCaseReportByID(caseReport.getReportID()) == null) { 
                caseReportList.add(caseReport);
                System.out.println("CaseReport Manager: CaseReport is added to the tempList");
            } else {
                System.out.println("CaseReport Manager: User("+ caseReport.getReportID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "CaseReport Manager: Report("+ caseReport.getReportID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(caseReportList, FILE_NAME_CASE_REPORTS)) {
            System.out.println("CaseReport Manager: CaseReport data file serialized successfully");
            if (loadCaseReportList()) {
                System.out.println("CaseReport Manager: CaseReport list loaded successfully");
                return true;
            } else {
                System.out.println("CaseReport Manager: ERROR: CaseReport list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("CaseReport Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean deleteCaseReport(CaseReport caseReport){
        caseReportList.remove(caseReport);
        
        if (serialize(caseReportList, FILE_NAME_CASE_REPORTS)) {
               System.out.println("CaseReport Manager: CaseReport data file serialized successfully");
            if (loadCaseReportList()) {
                System.out.println("CaseReport Manager: CaseReport list loaded successfully");
                return true;
            } else {
                System.out.println("CaseReport Manager: ERROR: CaseReport list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("CaseReport Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<CaseReport> getCaseReportListByPassengerID(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static void printCaseReportList(){
        if (caseReportList!=null) {
            for (CaseReport _caseReport : caseReportList) {
                System.out.println(_caseReport.getReportID());
            }
        } else {
            System.out.println("Null CaseReport List!!!");
        }
    }
}

