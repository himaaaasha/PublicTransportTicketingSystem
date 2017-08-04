
package ptts.services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Siri@MnS
 * FareCalculator - Static Class
 */
public class FareCalculator {
    private int[] tempTransportModes = {};
    private ArrayList<Station> tempRoute;
    /**
     * other needed attributes
     * 
     */
    
    public static float calculateFare(String ticketClass, String ticketType, Calendar startDate, Calendar endDate,
                                        String fromStation, String toStation, boolean isReturn, int[] transModes){
        

    //this contains hardcoded values for various option configurations
        return 150.00f; 
    }
    
    private static ArrayList<Station> findRoute(String startStation,String endStation){
    
        return null;
    }
    
    private static Date calculateTripDuration(){
        
        return null;
    }
    
    private static float calculateDistance(){
        return 60.00f;
    }
    
    private static void chargingModel(){
        //this may use Strategy design pattern to calculate the trip fare
        //according to various charging models
    }
}
