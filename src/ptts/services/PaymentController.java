
package ptts.services;

import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JOptionPane;
import ptts.entities.Passenger;
import ptts.entities.Ticket;
import ptts.entities.Token;
import ptts.entities.TransactionRecord;
import ptts.entities.managers.PassengerManager;
import ptts.entities.managers.TicketManager;
import ptts.entities.managers.TokenManager;
import ptts.entities.managers.TransactionRecordManager;
import ptts.main.PublicTransportTicketingSystem;
import static ptts.main.PublicTransportTicketingSystem.payStationController;

/**
 *
 * @author Siri@MnS
 */
public class PaymentController {
    
    private static final String comapanyID = "Metro456";
    
    public static boolean payTicket(String passengerID, String ticketClass, String ticketType, Calendar startDate, Calendar endDate,
                                        String fromStation, String toStation, boolean isReturn, int[] transModes){
        boolean status = false;
        float amount = FareCalculator.calculateFare(ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes);
        int response = JOptionPane.showConfirmDialog(null, "Do you want to proceed the transaction(Debit LKR: "+String.valueOf(amount)+")?", "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) 
        {               
            if (passengerID.equals("Guest")) {
                Ticket ticket = newTicket(passengerID, ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes, amount);
                if(TicketManager.addTicket(ticket)){
                    
                    Calendar date = Calendar.getInstance();
                    String cashierID = payStationController.getCurrentCashier().getUserID();
                    String transID = generateTranRecordID();
                    int type = 1;
                    TransactionRecord tr = newTransactionRecord(transID, cashierID, passengerID, date, ticket.getTicketID(), amount, type);
                    if(TransactionRecordManager.addTransRecord(tr)){
                        status = true; 
                    }
                    else{
                        status = false;
                    }
                }
            }
            else{
                Ticket ticket = newTicket(passengerID, ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes, amount);
                String cashierID = "System"; 
                
                if(debitAccount(passengerID, amount, cashierID, ticket.getTicketID())){
                    if(TicketManager.addTicket(ticket)){
                        ArrayList<TransactionRecord> updatedTRList = TransactionRecordManager.getTransRecordListByPassengerID(passengerID);
                        Passenger passenger = PassengerManager.getPassengerByID(passengerID);
                        passenger.setTransRecordList(updatedTRList);
                        passenger.addTicket(ticket);
                        PassengerManager.updatePassenger(passengerID, passenger);
                        
                        Token token = passenger.getToken();
                        TokenManager.updateToken(token.getTokenID(), token);
                        
                        PublicTransportTicketingSystem.passengerController.updateCurrentPassenger();
                        
                        status = true;
                    }
                }
                else{
                    return status = false;
                }
            }
        }
        else{
            JOptionPane.showMessageDialog(null, "Ticket was not purchased!","Payment Control", JOptionPane.WARNING_MESSAGE);
            status = false;
        }

        return status;
    }
    
    private static Ticket newTicket(String passengerID, String ticketClass, String ticketType, Calendar startDate, Calendar endDate,
                                        String fromStation, String toStation, boolean isReturn, int[] transModes, float fee){
        String ticketID = generateTicketID();
        
        Ticket ticket = new Ticket(ticketID, passengerID, ticketClass, ticketType, startDate, endDate, fromStation, toStation, transModes, toStation,isReturn, fee);
        
        return ticket;
    }
    
    private static String generateTicketID(){
    
        int ticketCount ;
        if(TicketManager.getTicketList() != null){
            ticketCount = TicketManager.getTicketList().size();
        }else{
            ticketCount = 0;
        }
        StringBuilder sb = new StringBuilder("2016T");
        sb.append(ticketCount+1);
        
        return sb.toString();
    }
    
    private static String generateTranRecordID(){
    
        int tranRecordCount ;
        if(TransactionRecordManager.getTransRecordList() != null){
            tranRecordCount = TransactionRecordManager.getTransRecordList().size();
        }else{
            tranRecordCount = 0;
        }
        StringBuilder sb = new StringBuilder("TR");
        sb.append(tranRecordCount+1);
        
        return sb.toString();
    }
    
    private static boolean debitAccount(String passengerID, float amount, String cashierID, String description){
        
        boolean status = false;
        
        Passenger passenger = PassengerManager.getPassengerByID(passengerID);
        if(passenger.deductAccount(amount)>0){
            if(PassengerManager.updatePassenger(passengerID, passenger)){
                PublicTransportTicketingSystem.passengerController.updateCurrentPassenger();
                
                Calendar date = Calendar.getInstance();  
                String transID = generateTranRecordID();
                int type = 1;
                TransactionRecord tr = newTransactionRecord(transID, cashierID, passengerID, date, description, amount, type);
                if(TransactionRecordManager.addTransRecord(tr)){
                    JOptionPane.showMessageDialog(null, "LKR "+amount+" was debited from your account", "Payment Controller", JOptionPane.INFORMATION_MESSAGE);
                    status = true; 
                }else{
                    status = false;
                }
            }else{
                status = false;
            }
            
        }else{
            
            JOptionPane.showMessageDialog(null, "Insuffient credit balance to complete the transaction", "Payment Controller", JOptionPane.ERROR_MESSAGE);
            status = false;
        }
        
        return status;
    }
    
    private static boolean creditAccount(String passengerID, float amount, String cashierID, String description){
        //throw new UnsupportedOperationException("Not supported yet.");
        boolean status = false;
        
        Passenger passenger = PassengerManager.getPassengerByID(passengerID);
        if(amount>0){
            passenger.addAccount(amount);
            if(PassengerManager.updatePassenger(passengerID, passenger)){
                try {
                    PublicTransportTicketingSystem.passengerController.updateCurrentPassenger();
                } catch (Exception e) {
                    System.out.println("Payment Controller: creditAccount: NullPointer exception when using Account Top-Up from PayStation Terminal");
                }
                Calendar date = Calendar.getInstance();  
                String transID = generateTranRecordID();
                int type = 0;
                TransactionRecord tr = newTransactionRecord(transID, cashierID, passengerID, date, description, amount, type);
                if(TransactionRecordManager.addTransRecord(tr)){
                    
                    JOptionPane.showMessageDialog(null, "LKR "+amount+" was credited to your account", "Payment Controller", JOptionPane.INFORMATION_MESSAGE);
                    status = true;
                    
                }else{
                    status =false;
                } 
            }
            else{
                status = false;
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "Negative credit amount!", "Payment Controller", JOptionPane.ERROR_MESSAGE);
            status = false;
        }
        return status;
    }
    
    
    
    private static int connectPaymentGateway(String companyID,String[] type,  float amount){
        //type[0] states the payment method; type[1] states the bank details or card details or mobile acount details
        int status = -1;
        
        //TODO: display JOptionPanes for each case!
        
        switch(type[0]){
            case "1":
                //Online Banking
                JOptionPane.showMessageDialog(null, "LKR "+amount+" was debited from you account.\nAs requested by "+companyID,"Online Banking: "+ type[1],JOptionPane.INFORMATION_MESSAGE);
                status = 1;
                break;
                
            case "2":
                //Debit/Credit card
                
                JOptionPane.showMessageDialog(null, "LKR "+amount+" was debited from you account.\nAs requested by "+companyID,"Debit/Credit Card Payment: "+ type[1],JOptionPane.INFORMATION_MESSAGE);
                status = 2;
                break;
                
            case "3":
                //MobilePay
                
                JOptionPane.showMessageDialog(null, "LKR "+amount+" was debited from you account.\nAs requested by "+companyID,"Mobile Pay:"+ type[1],JOptionPane.INFORMATION_MESSAGE);
                status= 3;
                break;
                
            case "4":
                //MobilePay
                
                JOptionPane.showMessageDialog(null, "LKR "+amount+" was credited to you account.\nAs requested by "+companyID,"PTTS Pay:"+ type[1],JOptionPane.INFORMATION_MESSAGE);
                status= 3;
                break;
        }
        
        return status;
    }
    
    public static boolean accountTopup(String passengerID, String[] type, float amount, String cashierID){
        //type[0] states the payment method; type[1] states the bank details or card details or mobile acount details
        
        boolean status = false;
        int paymentType = -1;
        //String cashierID = "System";
        String description = type[1];
        
        paymentType = connectPaymentGateway(comapanyID, type, amount);
        
        if (paymentType != -1) {
            
            if (creditAccount(passengerID, amount, cashierID, description)) {
                ArrayList<TransactionRecord> updatedTRList = TransactionRecordManager.getTransRecordListByPassengerID(passengerID);
                Passenger passenger = PassengerManager.getPassengerByID(passengerID);
                passenger.setTransRecordList(updatedTRList);
                PassengerManager.updatePassenger(passengerID, passenger);
                try {
                    PublicTransportTicketingSystem.passengerController.updateCurrentPassenger();
                } catch (Exception e) {
                    System.out.println("Payment Controller: accountTopUp: NullPointer exception when using Account Top-Up from PayStation Terminal");
                }
                status = true;
                
            } else {
                status = false;
            }
        }
        else{
            status = false;
        }

        
        return status;

    }

    private static TransactionRecord newTransactionRecord(String transID,String cashierID,String passengerID, 
                                                          Calendar date, String description, float amount, int type) {
        String trID = generateTranRecordID();
        TransactionRecord tr = new TransactionRecord(transID, cashierID, passengerID, date, description, amount, type);
        
        return tr;
    }
    
    
    //Note: only to use by the PayStation Terminal!!!
    public static boolean addTransactionRecord(String cashierID,String passengerID, String description, float amount, int type) {
        boolean status=false;
        String trID = generateTranRecordID();
        String transType;
        TransactionRecord tr = new TransactionRecord( trID, cashierID, passengerID, Calendar.getInstance(), description, amount, type);
        if(type == 0){
            transType = "Credit";
        }
        else{
            transType = "Debit";
        }
        if(TransactionRecordManager.addTransRecord(tr)){
            JOptionPane.showMessageDialog(null, transType + "transaction of " + amount + " completed successfully!", "Payment Controller", JOptionPane.INFORMATION_MESSAGE);
            status = true;
                    
        }else{
            status =false;
        }
        
        return status;
    }



}
