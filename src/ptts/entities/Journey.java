
package ptts.entities;

import java.io.Serializable;

/**
 *
 * @author Siri@MnS
 */
public class Journey implements Serializable  {
    private String ticketID;
    private String date;
    private String fromStation;
    private String toStation;
    private boolean isReturn;
    private float fee =-0.0f;
    private float distanceTravelled = 0.0f;
    private String startTime;
    private String endTime;
    private String duration;

    public Journey(String ticketID, String date, String fromStation, String toStation, boolean isReturn, 
            float fee, float distanceTravelled, String startTime, String endTime,String duration){
    
        this.ticketID = ticketID;
        this.date = date;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.fee = fee;
        this.distanceTravelled = distanceTravelled;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public String getTicketID() {
        return ticketID;
    }

    public String getDate() {
        return date;
    }

    public String getFromStation() {
        return fromStation;
    }

    public String getToStation() {
        return toStation;
    }

    public boolean isIsReturn() {
        return isReturn;
    }

    public float getFee() {
        return fee;
    }

    public float getDistanceTravelled() {
        return distanceTravelled;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getDuration() {
        return duration;
    }
    
}
