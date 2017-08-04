
package ptts.services;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.Ticket;
import ptts.entities.Token;

/**
 *
 * @author Siri@MnS
 */
public class TokenValidator {
    public static boolean status = true;  //hardcoded
    
    public static boolean validate(Token token, String station ){

        String passengerID = token.getPassengerID();    
        //first check whether the token is an active one
        if (token.isActive()) {                
            if (token.getActiveTicketList() != null) {
                if (checkRouteWithStation(token, station)) {
                    status = true;
                } else {
                    JOptionPane.showMessageDialog(null, station +" is not included in any permitted routes of active tickets !!!","Token Validator", JOptionPane.ERROR_MESSAGE);
                    System.out.println("Token Validator: " + station + "'is not included in any permitted routes of active tickets !!!");
                    status = false;
                }                    
            } else {
                JOptionPane.showMessageDialog(null, passengerID +"'s Token has no active tickets!!!","Token Validator", JOptionPane.ERROR_MESSAGE);
                System.out.println("Token Validator: "+ passengerID +"'s Token has no active tickets!!!");
                status = false;
            }
        }
        else{
            JOptionPane.showMessageDialog(null, passengerID +"'s Token is not active!!!","Token Validator", JOptionPane.ERROR_MESSAGE);
            System.out.println("Token Validator: "+ passengerID +"'s Token is not active!!!");
            status = false;
        }
        
        return status;
    }
    
    private static boolean checkRouteWithStation(Token token, String currentStation){
        //throw new UnsupportedOperationException("Not supported yet.");
        boolean status2 = false;
        ArrayList<Ticket> ticketList = token.getActiveTicketList();
        for(Ticket _ticket : ticketList){
            if(isStationInTheRoute(_ticket.getFromStop(), _ticket.getDestinationStop(), currentStation)){
                status2 = true;
            }
            else{
                status2 = false;
                break;
            }
        }

        return status;
        //return true; //hardcoded
        
    }
    
    
    private static boolean isStationInTheRoute(String fromSTation, String toStation, String currentStation){
        //hardcoded method, always evaluates to true
        
        
        return true;
    }

}
