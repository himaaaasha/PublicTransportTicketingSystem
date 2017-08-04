package ptts.entities;

import java.io.Serializable;

/**
 *
 * @author Siri@MnS
 */
public class User implements Serializable{
    private String userID= null;
    private int userType = -1; // 1=admin, 2=passenger, 3=guest, 4=inspector, 5=gate controller, 6=driver, 7=cachier
    private String userPassword = null;
    private boolean isActive = false;
    
    private String name = null;
    private int gender = -1; //1= Male, 2= Female
    private String contactNumber = null;
    private String email= null;
    private String address = null;
    private String profilePic = null;
    
    //as the contact number, email and address of a user is not mandatory those are not included
    //in the constructor method.
    public User(String userID, int userType, String userPassword, String name, int gender){
        this.userID = userID;
        this.userType = userType;
        this.userPassword = userPassword;
        this.name = name;
        this.gender = gender;
    }
    
    public User(String userID, int userType, String userPassword, boolean isActive, String name, int gender,
                String contactNumber, String email, String address){
        this.userID = userID;
        this.userType = userType;
        this.userPassword = userPassword;
        this.isActive = isActive;
        this.name = name;
        this.gender = gender;
        this.contactNumber = contactNumber;
        this.email = email;
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public int getUserType() {
        return userType;
    }

    public String getUserPassword() {
        return userPassword;
    }
    
    public boolean isActive(){
        return isActive;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
    
    public String getProfilePic(){
        return profilePic;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    
    public void setIsActive(boolean isActive){
        this.isActive = isActive;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    public void setProfilePic(String profilePic){
        this.profilePic= profilePic;
    }
    
    
}
