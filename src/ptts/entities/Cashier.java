
package ptts.entities;

/**
 *
 * @author Siri@MnS
 */
public class Cashier extends User {

    public Cashier(String userID, int userType, String userPassword, boolean isActive, String name, int gender, String contactNumber, String email, String address) {
        super(userID, userType, userPassword, isActive, name, gender, contactNumber, email, address);
    }
    
}
