
package ptts.entities.controllers;

import java.util.Calendar;
import java.util.Date;
import ptts.entities.Passenger;
import ptts.entities.managers.PassengerManager;
import ptts.services.FareCalculator;
import ptts.services.PaymentController;

/**
 *
 * @author Siri@MnS
 */
public class PassengerController {

    //<editor-fold defaultstate="collapsed" desc="Singleton implementation">
    private static PassengerController instance= null;
    private PassengerController(Passenger passenger){ //stop creation of instances by other entities
        this.currentPassenger = passenger;
    }
    public static PassengerController getInstance(Passenger passenger){
        if (instance == null) {
            instance = new PassengerController(passenger);
        }
        return instance;
    }
//</editor-fold>

    private Passenger currentPassenger=null;
    
    public Passenger getCurrentPassenger(){
        return this.currentPassenger;
    }
    
    public void updateCurrentPassenger(){
        currentPassenger = PassengerManager.getPassengerByID(currentPassenger.getUserID());
    }
    
    public float findFee(String ticketClass, String ticketType, Calendar startDate, Calendar endDate,
                                        String fromStation, String toStation, boolean isReturn, int[] transModes){
        //this contains hardcoded values for various option configurations
        //return 150.00f;
        
        return FareCalculator.calculateFare(ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes);
    }

    public boolean buyTicket(String passengerID, String ticketClass, String ticketType, Calendar startDate, Calendar endDate,
                                        String fromStation, String toStation, boolean isReturn, int[] transModes) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if(PaymentController.payTicket(passengerID, ticketClass, ticketType, startDate, endDate, fromStation, toStation, isReturn, transModes)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean accountTopUp(String passengerID, String[] type, float amount){
        //type[0] : 1=Online Banking, 2=Debit/Credit cards, 3=Mobile Pay
        //type[1]: contains each payment method credentials
        return PaymentController.accountTopup(passengerID, type, amount, "System");
        
    }
}
