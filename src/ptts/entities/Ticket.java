package ptts.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Siri@MnS
 * 
 * Note: //planned to use 'int' value to represent the ticketClass in future version for quick access of data 
 */
public class Ticket implements Serializable{
    private String ticketID=null;
    private String passengerID=null;    //null for guest tickets
    private String ticketClass=null;    //states the ticket class such 'Standard', 'First', 'Standard-reserved'
    private String ticketType=null;     //Peak, Off-Peak, Weekend etc
    private Calendar startDate=null;
    private Calendar endDate = null;
    private String fromStop=null;
    private String destinationStop=null;
    private int[] transMode = {0,0,0}; //{1,0,0} = {bus,-,-}, {1,1,0} = {bus,train,-}, {0,1,1} = {-,train,taxi}
    private boolean isReturn = false;
    private String route=null; // eg: Gampaha-Ragama-Colombo
    private float fee=0.0f;
    private boolean isExpired= false;
    private ArrayList<CheckIn>  checkInList =new ArrayList();
    private float distanceTravelled = 0.0f;

    
    
 
    public Ticket(  String ticketID, String passengerID, String ticketClass, String ticketType,
                    Calendar startDate, Calendar validUntilDate, String fromStop, String destinationStop,
                    int[] transMode, String route, boolean isReturn, float fare ){
        this.ticketID = ticketID;
        this.passengerID = passengerID;
        this.ticketClass = ticketClass;
        this.ticketType = ticketType;
        this.startDate =  startDate;
        this.endDate= validUntilDate;
        this.fromStop = fromStop;
        this.destinationStop= destinationStop;
        this.transMode= transMode;
        this.isReturn = isReturn;
        this.route = route;
        this.fee = fare;
    }
    
    // <editor-fold defaultstate="collapsed" desc="Getters">
    
    public String getTicketID() {
        return ticketID;
    }

    public String getPassengerID() {
        return passengerID;
    }

    public String getTicketClass() {
        return ticketClass;
    }

    public String getTicketType() {
        return ticketType;
    }

    public Calendar getStartDate() {
        return startDate;
    }

    public Calendar getValidUntilDate() {
        return endDate;
    }

    public String getFromStop() {
        return fromStop;
    }

    public String getDestinationStop() {
        return destinationStop;
    }

    public int[] getTransMode() {
        return transMode;
    }

    public String getRoute() {
        return route;
    }

    public float getFee() {
        return fee;
    }
    
    public boolean isExpired(){
        return isExpired;
    }
    
    public ArrayList<CheckIn> getCheckInList(){
        return checkInList;
    }
    
    public float getDistanceTravelled(){
        return distanceTravelled;
    }
    
    public boolean isReturn(){
        return isReturn;
    }
    
    // </editor-fold> 
    

    public void setIsExpired(boolean isExpired) {
        this.isExpired = isExpired;
    }

    public void addCheckIn( CheckIn chkIn) {
        checkInList.add(chkIn);
    }
    
    public void addDistance(float increment){
        this.distanceTravelled =+ increment;
    }
         
    
}
