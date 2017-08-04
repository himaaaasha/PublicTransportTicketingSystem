
package ptts.entities.controllers;

import java.util.Calendar;
import ptts.entities.Cashier;
import ptts.entities.TransactionRecord;
import ptts.entities.managers.TransactionRecordManager;
import ptts.services.FareCalculator;
import ptts.services.PaymentController;

/**
 *
 * @author Siri@MnS
 */
public class PayStationController {
    //<editor-fold defaultstate="collapsed" desc="Singleton implementation">
    private static PayStationController instance= null;
    private PayStationController(Cashier cashier){ //stop creation of instances by other entities
        this.currentCashier = cashier;
    }
    public static PayStationController getInstance(Cashier cashier){
        if (instance == null) {
            instance = new PayStationController(cashier);
        }
        return instance;
    }
//</editor-fold>

    private Cashier currentCashier=null;
    
    public Cashier getCurrentCashier(){
        return this.currentCashier;
    }
    
    public boolean buyTicket(String ticketClass, String ticketType, Calendar startDate, Calendar endDate, String fromStation, String toStation, boolean isReturn, int[] transModes){
        if(PaymentController.payTicket("Guest", ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes)){
            return true;
        }
        else{
            return false;
        }
    }
    
    public float findFee(String ticketClass, String ticketType, Calendar startDate, Calendar endDate,
                                        String fromStation, String toStation, boolean isReturn, int[] transModes){
        //this contains hardcoded values for various option configurations
        //return 150.00f;
        
        return FareCalculator.calculateFare(ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes);
    }

    public boolean accountTopUp(String passengerID, float amount){
        //type[0] : 1=Online Banking, 2=Debit/Credit cards, 3=Mobile Pay, 4 = cash payment
        //type[1]: contains each payment method credentials
        String[] type = {"4","Account Topup: PTTS Pay"};
        
        return (PaymentController.accountTopup(passengerID, type, amount, currentCashier.getUserID()));

        
    }
    
    public boolean newTransactionRecord(String passengerID, String description, float amount, int type){
        
        return PaymentController.addTransactionRecord(currentCashier.getUserID(), passengerID, description, amount, type);
        
    }
}
