import com.mysql.cj.log.Log;

import java.security.Security;
import java.util.ArrayList;
import java.util.Date;

public class main {

    static Thread Network_Core = new Thread(Networking::NETWORK_CORE);
    static Thread Network_PINGM = new Thread(Networking::Ping_Master);
    static Thread Network_BC_DB_PUBLISH = new Thread(Networking::ADD_RECORD);
    static Thread Log_Mgmt = new Thread(Logger::Logme);
    static Thread Networking_Client_MGR = new Thread(Networking::MakeMatch_Clients);
    static Thread Networking_Client_SS = new Thread(Networking::Data_Client);



    public static void main(String[] agrs) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        StringUtil.HASH();


        //new TEST_SEQ().Test_Chain();
        Networking.ADD_NET();
        DataBase.Get_Masters();


        Networking.Active_Threads.add(Network_PINGM);
        Networking.Active_Threads.add(Network_Core);
        Networking.Active_Threads.add(Network_BC_DB_PUBLISH);
        Networking.Active_Threads.add(Log_Mgmt);
        Networking.Active_Threads.add(Networking_Client_MGR);
        Networking.Active_Threads.add(Networking_Client_SS);
        Networking.Logs.add("Starting System");

        for(Thread T: Networking.Active_Threads){
            System.out.println("Checking Thread: "+ T);
            if(!T.isAlive()){
                T.start();
                System.out.println("Starting Thread: "+ T);
            }
        }



    }
}
