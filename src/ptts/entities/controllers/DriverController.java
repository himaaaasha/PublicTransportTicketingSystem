
package ptts.entities.controllers;

import ptts.entities.Driver;
import ptts.entities.Passenger;
import ptts.entities.Ticket;
import ptts.entities.Token;
import ptts.entities.managers.PassengerManager;
import ptts.entities.managers.TicketManager;
import ptts.entities.managers.TokenManager;

/**
 *
 * Represents driver controller class
 */
public class DriverController {
    //<editor-fold defaultstate="collapsed" desc="Singleton implementation">
    private static DriverController instance= null;
    private DriverController(Driver passenger){ //stop creation of instances by other entities
        this.currentDriver = passenger;
    }
    public static DriverController getInstance(Driver driver){
        if (instance == null) {
            instance = new DriverController(driver);
        }
        return instance;
    }
//</editor-fold>

    private Driver currentDriver=null;
    
    public Driver getCurrentDriver(){
        return this.currentDriver;
    }
    
    public boolean ticketCheckOutUpdates(Ticket updatedTicket, Token updatedToken, float distanceIncrement){
        if(updateTicketList(updatedTicket)){
            
            if(updateTokenList(updatedToken)){
                
                if(updatePassengerList(updatedToken, distanceIncrement)){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
        else{
            return false;
        }
 
    }

    private boolean updateTicketList(Ticket updatedTicket) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return TicketManager.updateTicket(updatedTicket.getTicketID(), updatedTicket);
    }

    private boolean updateTokenList(Token updatedToken) {
        //throw new UnsupportedOperationException("Not supported yet.");
        return TokenManager.updateToken(updatedToken.getTokenID(), updatedToken);
    }

    private boolean updatePassengerList(Token updatedToken, float distanceIncrement) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Passenger updatedPassenger = PassengerManager.getPassengerByID(updatedToken.getPassengerID());
        updatedPassenger.setToken(updatedToken);
        updatedPassenger.addDistance(distanceIncrement);
        return PassengerManager.updatePassenger(updatedPassenger.getUserID(), updatedPassenger);
    }
}
