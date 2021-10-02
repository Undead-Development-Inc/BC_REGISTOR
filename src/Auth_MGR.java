import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Auth_MGR {

    public static void Add_USER(PublicKey publicKey, PrivateKey privateKey, String Password){
        PublicKey publicKey1 = publicKey;
        PrivateKey privateKey1 = privateKey;
        String PassCode = StringUtil.applySha256(Password+privateKey1);
        String Hash = StringUtil.applySha512(publicKey1+privateKey1.toString()+PassCode);
        if(!DataBase.FIND_AUTHUSER(publicKey1, privateKey1, PassCode)) {
            System.out.println("Adding User");
            DataBase.ADD_AUTH_USER(Hash);
        }else {
            System.out.println("User Exists");
        }
        return;
    }

    public static boolean IsUser(PublicKey publicKey, PrivateKey privateKey, String Password){
        PublicKey publicKey1 = publicKey;
        PrivateKey privateKey1 = privateKey;
        String pwdHash = StringUtil.applySha256(Password+privateKey1);
        String Assumed_Hash = StringUtil.applySha512(publicKey1+ privateKey1.toString()+ pwdHash);
        if(DataBase.FIND_AUTHUSER(publicKey1, privateKey1, pwdHash)){
            return true;
        }else {
            return false;
        }
    }

    public static ArrayList<String> Net_INPUT_Q(String Type_Request){
        ArrayList<String> Inputs = new ArrayList<>();
        if(Type_Request.matches("Creds")){
            Inputs.add("SEND Public Key");
            Inputs.add("SEND  Private Key");
            Inputs.add("ENTER PASSWORD");
        }
        return Inputs;
    }
}
