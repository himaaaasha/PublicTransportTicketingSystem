
package ptts.entities;

import java.util.ArrayList;

/**
 *
 * @author Siri@MnS
 */
public class Passenger extends User{
    
    private ArrayList<Journey> journeyList = new ArrayList<Journey>();
    private ArrayList<TransactionRecord> transRecordList = new ArrayList<TransactionRecord>();
    
    private Token token = null;
    private float creditBalance = -0.0f;
    private float distanceTravelled = 0.0f;
    
    private ArrayList<Ticket> expiredTicketList = new ArrayList<Ticket>();
    private ArrayList<Ticket> reliasedTicketList = new ArrayList<Ticket>(); //the list which is used for reliasing the journey list

    public Passenger(String userID, int userType, String userPassword, String name, int gender) {
        super(userID, userType, userPassword, name, gender);
    }
    
    public Passenger(String userID, int userType, String userPassword, boolean isActive, String name, int gender,
            String contactNumber, String email, String address) {
        super(userID, userType, userPassword, isActive, name, gender, contactNumber, email, address);
    }
 
    public ArrayList<Journey> getJourneyList() {
        return journeyList;
    }
    
    public void setJourneyList(ArrayList<Journey> journeyList) {
        this.journeyList = journeyList;
    }
    
    public ArrayList<TransactionRecord> getTransRecordList() {
        return transRecordList;
    }
    
    public void setTransRecordList(ArrayList<TransactionRecord> transRecordList) {
        this.transRecordList = transRecordList;
    }
    
    public Token getToken() {
        return token;
    }
    
    public void setToken(Token token) {
        this.token = token;
    }
    
    public float getCreditBalance() {
        return creditBalance;
    }
    
    public void setCreditBalance(float creditBalance) {
        this.creditBalance = creditBalance;
    }
    
    public void setDistanceTravelled(float distance){
        this.distanceTravelled = distance;
    }
    
    public ArrayList<Ticket> getExpiredTicketList() {
        return expiredTicketList;
    }
    
    public void setExpiredTicketList(ArrayList<Ticket> expiredTicketList) {
        this.expiredTicketList = expiredTicketList;
    }
    
    public ArrayList<Ticket> getReliasedTicketList() {
        return reliasedTicketList;
    }
    
    public void setReliasedTicketList(ArrayList<Ticket> reliasedTicketList) {
        this.reliasedTicketList = reliasedTicketList;
    }
    
    public void addTicket(Ticket ticket){
        this.token.addTicket(ticket);
    }
    
    public ArrayList<Ticket> getActiveTicketList(){
        return this.token.getActiveTicketList();
    }
    
    public void removeTokenTicketByID(String ticketID){
        
    }
    
    public void invalidateToken(){
        this.token.setIsActive(false);
    }
    
    public float deductAccount(float amount){
        if ((creditBalance - amount)>0) {
            creditBalance = creditBalance - amount;
        }
        else{
            return -1.0f;
        }
        
        return creditBalance;
    }
    
    public float addAccount(float amount){
        creditBalance = creditBalance +amount;
        
        return creditBalance;
    }
    
    public void addDistance(float distanceIncrement){
        distanceTravelled += distanceIncrement;
    }
    
    
}
