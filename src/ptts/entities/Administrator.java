
package ptts.entities;

/**
 *
 * @author Siri@MnS
 */
public class Administrator extends User {
    
    public Administrator(String userID, int userType, String userPassword, String name, int gender) {
        super(userID, userType, userPassword, name, gender);
    }
    
    public Administrator(String userID, int userType, String userPassword,boolean isActive, String name, int gender,
            String contactNumber, String email, String address) {
        super(userID, userType, userPassword, isActive, name, gender, contactNumber, email, address);
    }
}
