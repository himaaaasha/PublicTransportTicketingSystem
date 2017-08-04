
package ptts.entities;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Siri@MnS
 */
public class TransactionRecord implements Serializable{
    private String transID;
    private String cashierID;
    private String passengerID;
    private Calendar date;
    private String description;  //ticketID, account Top-up, credit share, receipt of credit from a friend
    private float amount = 0.0f;
    private int type = -1; //0 = credit, 1=debit
    
    public TransactionRecord(String transID,String cashierID,String passengerID, Calendar date, String description, float amount, int type){
        this.transID = transID;
        this.cashierID = cashierID;
        this.passengerID = passengerID;
        this.date =date;
        this.description = description;
        this.amount = amount;
        this.type = type;
    }

    public String getTransID() {
        return transID;
    }

    public String getCashierID(){
        return cashierID;
    }
    
    public String getPassengerID(){
        return passengerID;
    }
    
    public Calendar getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public float getAmount() {
        return amount;
    }

    public int getType() {
        return type;
    }
}
