
package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.Ticket;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents Ticket Manager Class
 */

public class TicketManager {
    private static ArrayList<Ticket> ticketList = null;   
    private static final String FILE_NAME_TICKETS = "tickets.ser";
    
    public static Ticket getTicketByID(String ticketId) {
        Ticket ticket =  null;
        if (ticketList!=null) {
            for (Ticket _ticket : ticketList) {
                if(_ticket.getTicketID().equals(ticketId))
                    ticket =  _ticket;
            }
            return ticket;
        } else {
            System.out.println("Ticket Manager [getTicketByID]: Null TicketList!!!");
            return ticket;
        }
    }
    
    public static boolean loadTicketList(){
        ticketList = DAController.deSerializeTicketData(FILE_NAME_TICKETS);
        if(ticketList != null){
            System.out.println("Ticket Manager: Ticket List not NULL");
            return true;
        } else {
            System.out.println("Ticket Manager: Ticket List is NULL");
            return false;
        }
    }
    
    public static ArrayList<Ticket> getTicketList(){
        return ticketList;
    }
    
    public static boolean addTicket(Ticket ticket){
        if (ticketList == null) {
            ticketList = new ArrayList<>();
            ticketList.add(ticket);
        } else {
            if (getTicketByID(ticket.getTicketID()) == null) {
                ticketList.add(ticket);
                System.out.println("Ticket Manager: Ticket is added to the tempList");
            } else {
                System.out.println("Ticket Manager: User("+ ticket.getTicketID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "Ticket Manager: Ticket("+ ticket.getTicketID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(ticketList, FILE_NAME_TICKETS)) {
            System.out.println("Ticket Manager: Ticket data file serialized successfully");
            if (loadTicketList()) {
                System.out.println("Ticket Manager: Ticket list loaded successfully");
                return true;
            } else {
                System.out.println("Ticket Manager: ERROR: Ticket list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Ticket Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean updateTicket(String oldTicketID, Ticket updatedTicket) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Ticket oldTicket = getTicketByID(oldTicketID);
        
        ticketList.set(ticketList.indexOf(oldTicket),updatedTicket);
        
        if (serialize(ticketList, FILE_NAME_TICKETS)) {
            System.out.println("Ticket Manager: Ticket data file serialized successfully");
            if (loadTicketList()) {
                System.out.println("Ticket Manager: Ticket list loaded successfully");
                return true;
            } else {
                System.out.println("Ticket Manager: ERROR: Ticket list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Ticket Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<Ticket> getTicketListByPassengerID(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static ArrayList<Ticket> makeTicketsExpire(ArrayList<Ticket> newTicketList){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static void printTicketList(){
        if (ticketList!=null) {
            for (Ticket ticket : ticketList) {
                System.out.println(ticket.getTicketID());
            }
        } else {
            System.out.println("Null Ticket List!!!");
        }
    }
}