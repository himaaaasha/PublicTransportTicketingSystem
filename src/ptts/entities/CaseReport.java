
package ptts.entities;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Siri@MnS
 */
public class CaseReport implements Serializable{
    private String reportID;
    private String inspectorID;
    private String tokenID;
    private String passengerID;
    private String station;
    private Calendar date;

    
    public CaseReport(String reportID, String inspectorID, String tokenID, String passengerID, 
                        String station, Calendar date){
        this.reportID = reportID;
        this.inspectorID = inspectorID;
        this.tokenID = tokenID;
        this.passengerID =passengerID;
        this.station = station;
        this.date =date;
    }
    
    
    public String getReportID() {
        return reportID;
    }
    
    public String getInspectorID() {
        return inspectorID;
    }
    
    public String getTokenID() {
        return tokenID;
    }
    
    public String getPassengerID() {
        return passengerID;
    }
    
    public String getStation() {
        return station;
    }
    
    public Calendar getDate() {
        return date;
    }

}
