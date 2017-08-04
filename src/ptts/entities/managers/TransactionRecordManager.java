
package ptts.entities.managers;

import ptts.entities.TransactionRecord;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents Transaction Manager Class
 */
public class TransactionRecordManager {
    private static ArrayList<TransactionRecord> transRecList = null;  
    private static final String FILE_NAME_TRANSRECORDS = "transaction_records.ser";
    
    public static TransactionRecord getTransactionRecordByID(String transactionID) {
        TransactionRecord tempRecord =  null;
        if (transRecList!=null) {
            for (TransactionRecord record : transRecList) {
                if(record.getTransID().equals(transactionID))
                    tempRecord =  record;
            }
            return tempRecord;
        } else {
            System.out.println("TransactionRecord Manager [getTransactionRecordByID]: Null TransactionRecordList!!!");
            return tempRecord;
        }
    }
    
    public static boolean loadTransRecordList(){
        transRecList = DAController.deSerializeTransactionRecordData(FILE_NAME_TRANSRECORDS);
        if(transRecList != null){
            System.out.println("TransactionRecord Manager: TransRecord List not NULL");
            return true;
        } else {
            System.out.println("TransactionRecord Manager: TransRecord List is NULL");
            return false;
        }
    }
    
    public static ArrayList<TransactionRecord> getTransRecordList(){
        return transRecList;
    }
    
    public static boolean addTransRecord(TransactionRecord transactionRecord){
        if (transRecList == null) {
            transRecList = new ArrayList<>();
            transRecList.add(transactionRecord);
        } else {
            if (getTransactionRecordByID(transactionRecord.getTransID()) == null) { //check whether the user is already registered in the system
                transRecList.add(transactionRecord);
                System.out.println("TransactionRecord Manager: TransactionRecord is added to the tempList");
            } else {
                System.out.println("TransactionRecord Manager: TransactionRecord("+ transactionRecord.getTransID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "TransactionRecord Manager: TransactionRecord("+ transactionRecord.getTransID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(transRecList, FILE_NAME_TRANSRECORDS)) {
            System.out.println("TransactionRecord Manager: TransactionRecord data file serialized successfully");
            if (loadTransRecordList()) {
                System.out.println("TransactionRecord Manager: TransactionRecord list loaded successfully");
                return true;
            } else {
                System.out.println("TransactionRecord Manager: ERROR: TransactionRecord list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("TransactionRecord Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<TransactionRecord> getTransRecordListByPassengerID(String passengerID){
        //throw new UnsupportedOperationException("Not supported yet.");
        ArrayList<TransactionRecord> newTRList = new ArrayList<>();
        for(TransactionRecord _tr : transRecList){
            if(_tr.getPassengerID().equals(passengerID)){
                newTRList.add(_tr);
            }
        }
        
        return newTRList;
    }
    
    public static void printTransRecList(){
         if (transRecList!=null) {
            for (TransactionRecord transcationRecord : transRecList) {
                System.out.println(transcationRecord.getTransID());
            }
        } else {
            System.out.println("Null TransRecord List!!!");
        }
    }
}