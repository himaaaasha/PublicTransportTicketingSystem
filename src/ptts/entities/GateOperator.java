
package ptts.entities;

/**
 *
 * @author Siri@MnS
 */
public class GateOperator extends User {

    public GateOperator(String userID, int userType, String userPassword, boolean isActive, String name, int gender, String contactNumber, String email, String address) {
        super(userID, userType, userPassword, isActive, name, gender, contactNumber, email, address);
    }
    
}
