import com.mysql.cj.x.protobuf.MysqlxDatatypes;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Message_Package {

    private String New_Message = "";
     String Recpt_Address = "";
     PublicKey Public_Address_Sender;
     byte[] Encrypted_Key = null;

    public Message_Package(String Message, String Recpt, PrivateKey senderkey, PublicKey senderaddress, String passcode){

        New_Message += Message;
        Recpt_Address += Recpt;
        Public_Address_Sender = senderaddress;

        Encrypted_Key = StringUtil.applyECDSASig(senderkey, this.toString() + passcode);
    }

    public String Get_Message(String Passcode){
        if(StringUtil.verifyECDSASig(this.Public_Address_Sender, this.toString() + Passcode, this.Encrypted_Key)){
            return New_Message;
        }else {
            return "";
        }
    }




}
