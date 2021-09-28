import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class Commands {

    public static ArrayList<byte[]> EKEYS = new ArrayList<>();
    public String Command = "";
    PrivateKey privateKey;
    PublicKey publicKey;

    public Commands(String c, PrivateKey privateKey, PublicKey publicKey){
        this.privateKey = privateKey;
        this.publicKey = publicKey;
        this.Command += Decrypt_Command(c, privateKey, publicKey);
    }

    public static String Decrypt_Command(String Command, PrivateKey privateKey, PublicKey publicKey){
        for(byte[] ekey : EKEYS){
            if(!StringUtil.verifyECDSASig(publicKey, Command, ekey)){
                return Command;
            }

        }
        return "";
    }
}
