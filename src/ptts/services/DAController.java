
package ptts.services;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import ptts.entities.Administrator;
import ptts.entities.CaseReport;
import ptts.entities.Cashier;
import ptts.entities.Driver;
import ptts.entities.GateOperator;
import ptts.entities.Inspector;
import ptts.entities.Passenger;
import ptts.entities.Ticket;
import ptts.entities.Token;
import ptts.entities.TransactionRecord;
import ptts.entities.User;

/**
 * 
 * Represents data access controller  
 */
public class DAController {
    
    public static boolean serialize(Object object, String filename){
        try{
            FileOutputStream fileout = new FileOutputStream("data/"+filename, false);
            ObjectOutputStream out = new ObjectOutputStream(fileout);
            out.writeObject(object);
            out.close();
            fileout.close();
            System.out.println("DAController: /data/"+filename+" is serialized!");
            return true;
        } catch(IOException e1){
            e1.printStackTrace();
            System.out.println("DAController: Error with the OutputStream");
            return false;
        }
    }
    
    public static ArrayList<Passenger> deSerializePassengerData(String filename){
        ArrayList<Passenger> passengerList = null;    
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            passengerList = (ArrayList<Passenger>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return passengerList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: Passenger class not found!!!");
            return null;
        }
    }
    
    public static ArrayList<Administrator> deSerializeAdminData(String filename){   
        ArrayList<Administrator> adminList = null;  
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename );
            ObjectInputStream in = new ObjectInputStream(fileIn);
            adminList = (ArrayList<Administrator>)in.readObject();
            in.close();
            System.out.println("DAController: data/"+ filename +" is deserialized!");
            return adminList;           
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: Administrator class not found!!!");
            return null;
        }  
    }
    
    public static ArrayList<CaseReport> deSerializeCaseReportData(String filename){ 
        ArrayList<CaseReport> CaseReportList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            CaseReportList = (ArrayList<CaseReport>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return CaseReportList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: CaseReport class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<Cashier> deSerializeCashierData(String filename){ 
        ArrayList<Cashier> CashierList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            CashierList = (ArrayList<Cashier>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return CashierList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: Cashier class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<Driver> deSerializeDriverData(String filename){ 
        ArrayList<Driver> DriverList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            DriverList = (ArrayList<Driver>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return DriverList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: Driver class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<GateOperator> deSerializeGateOperatorData(String filename){ 
        ArrayList<GateOperator> GateOperatorList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            GateOperatorList = (ArrayList<GateOperator>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return GateOperatorList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: GateOperator class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<Inspector> deSerializeInspectorData(String filename){ 
        ArrayList<Inspector> InspectorList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            InspectorList = (ArrayList<Inspector>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return InspectorList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: Inspector class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<Ticket> deSerializeTicketData(String filename){ 
        ArrayList<Ticket> TicketList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TicketList = (ArrayList<Ticket>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return TicketList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: Ticket class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<Token> deSerializeTokenData(String filename){ 
        ArrayList<Token> TokenList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TokenList = (ArrayList<Token>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return TokenList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: Token class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<TransactionRecord> deSerializeTransactionRecordData(String filename){ 
        ArrayList<TransactionRecord> TransactionRecordList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TransactionRecordList = (ArrayList<TransactionRecord>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return TransactionRecordList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: TransactionRecord class not found!!!");
            return null;
        }   
    }
    
    public static ArrayList<User> deSerializeUserData(String filename){ 
        ArrayList<User> UserList = null;   
        try {
            FileInputStream fileIn = new FileInputStream("data/" + filename);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            UserList = (ArrayList<User>)in.readObject();
            in.close();
            System.out.println("DAController: /data/"+ filename +" is deserialized!");
            return UserList;     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: "+ filename + " not found!!!");
            return null;
        } catch (IOException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DAController.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("DAController: User class not found!!!");
            return null;
        }   
    }
}
