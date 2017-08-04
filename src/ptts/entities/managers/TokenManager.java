
package ptts.entities.managers;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import ptts.entities.Token;
import ptts.services.DAController;
import static ptts.services.DAController.serialize;

/**
 *
 * Represents Token Manager Class
 */
public class TokenManager {
    private static ArrayList<Token> tokenList = null;
    private static final String FILE_NAME_TOKENS = "tokens.ser";
    
    public static Token getTokenByID(String id) {
        Token tempToken =  null;
        if (tokenList!=null) {
            for (Token token : tokenList) {
                if(token.getTokenID().equals(id))
                    tempToken = token;
            }
            return tempToken;
        } else {
            System.out.println("Token Manager [getTokenByID]: Null TokenList!!!");
            return tempToken;
        }
    }
    
    public static boolean loadTokenList(){
        tokenList = DAController.deSerializeTokenData(FILE_NAME_TOKENS);
        if(tokenList != null){
            System.out.println("Token Manager: Token List not NULL");
            return true;
        } else {
            System.out.println("Token Manager: Token List is NULL");
            return false;
        }
    }
    
    public static ArrayList<Token> getTokenList(){
        return tokenList;
    }
    
    public static boolean addToken(Token token){
        if (tokenList == null) {
            tokenList = new ArrayList<>();
            tokenList.add(token);
        } else {
            if (getTokenByID(token.getTokenID()) == null) {
                tokenList.add(token);
                System.out.println("Token Manager: Token is added to the tempList");
            } else {
                System.out.println("Token Manager: Token("+ token.getTokenID()+")  is already in the system!");
                JOptionPane.showMessageDialog(null, "Token Manager: Token("+ token.getTokenID()+")  is already in the system!");
                return false;
            }
        }
        
        if (serialize(tokenList, FILE_NAME_TOKENS)) {
            System.out.println("Token Manager: Token data file serialized successfully");
            if (loadTokenList()) {
                System.out.println("Token Manager: Token list loaded successfully");
                return true;
            } else {
                System.out.println("Token Manager: ERROR: Token list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Token Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static boolean updateToken(String oldTokenID, Token updatedToken) {
        //throw new UnsupportedOperationException("Not supported yet.");
        Token oldToken = getTokenByID(oldTokenID);
        
        tokenList.set(tokenList.indexOf(oldToken),updatedToken);
        
        if (serialize(tokenList, FILE_NAME_TOKENS)) {
            System.out.println("Token Manager: Token data file serialized successfully");
            if (loadTokenList()) {
                System.out.println("Token Manager: Token list loaded successfully");
                return true;
            } else {
                System.out.println("Token Manager: ERROR: Token list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Token Manager: Serialization Error!!");
            return false;
        }
    }
    
    public static ArrayList<Token> getTokenListByPassengerID(){
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public static void printTokenList(){
         if (tokenList!=null) {
            for (Token token : tokenList) {
                System.out.println(token.getTokenID());
            }
        } else {
            System.out.println("Null Token List!!!");
        }
    }
    
    
    public static boolean deleteToken(Token token){
        tokenList.remove(token);
        
        if (serialize(tokenList, FILE_NAME_TOKENS)) {
            System.out.println("Token Manager: Token data file serialized successfully");
            if (loadTokenList()) {
                System.out.println("Token Manager: Token list loaded successfully");
                return true;
            } else {
                System.out.println("Token Manager: ERROR: Token list NOT loaded!");
                return true;
            } 
        } else {
            System.out.println("Token Manager: Serialization Error!!");
            return false;
        }
    }
}

