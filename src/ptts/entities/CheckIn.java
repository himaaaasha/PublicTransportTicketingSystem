
package ptts.entities;

import java.io.Serializable;
import java.util.Calendar;

/**
 *
 * @author Siri@MnS
 */
public class CheckIn implements Serializable{
    private String stopLocation= null;
    private Calendar timeStamp= null;//system uses SimpleDateFormat to convert it to DateFormat and then to a Date object
    private int type=-1; // 1=in,  0=out,  -1=not set
    
    public CheckIn(String stopLocation, Calendar timeStamp, int type){
        this.stopLocation= stopLocation;
        this.timeStamp = timeStamp;
        this.type=type;
    }
    
    
    public String getStopLocation(){
        return this.stopLocation;
    }
    
    public Calendar getTimeStamp(){
        return this.timeStamp;
    }
    
    public int getType(){
        return this.type;
    }
}
