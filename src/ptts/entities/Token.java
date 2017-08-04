
package ptts.entities;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Siri@MnS
 */
public class Token implements Serializable{
    private String tokenID = null;
    private String passengerID = null;
    private boolean isActive = true;
    private ArrayList<Ticket> activeTickets = new ArrayList<Ticket>();
    
    public Token(String tokenID, String passengerID){
        this.tokenID = tokenID;
        this.passengerID = passengerID;
    }
    
    public String getTokenID() {
        return tokenID;
    }
    
    public String getPassengerID(){
        return passengerID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
    
    public void addTicket(Ticket ticket){
        activeTickets.add(ticket);
    }
    
    public ArrayList<Ticket> getActiveTicketList(){
        return this.activeTickets;
    }
    
    
    //*  Aditional Functionalities  ~ start *//
    
    private Ticket findTicketByID(String ticketID){
        Ticket tempTicket = null;
        for(int i=0; i< activeTickets.size(); i++){
            if(activeTickets.get(i).getTicketID().equals(ticketID)){
                tempTicket = activeTickets.get(i);
            }
        }
        return tempTicket;
    }
    
    public Ticket removeTicketByID(String ticketID){
        Ticket tempTicket = null;

        for(int i=0; i< activeTickets.size(); i++){
            if(activeTickets.get(i).getTicketID().equals(ticketID)){
                tempTicket = activeTickets.get(i);
                activeTickets.remove(i);
            }
        }
        
        return tempTicket;
    }
    
    public Ticket removeTicket(Ticket ticket){
        Ticket tempTicket = activeTickets.get(activeTickets.indexOf(ticket));
        activeTickets.remove(ticket);
        return tempTicket;
    }
    
    
}
