import java.util.ArrayList;
import java.util.Date;

public class main {

   static Thread Network_CORE = new Thread(Networking::Network_Accept);
    static Thread Network_GET = new Thread(Networking::Network_GET);
    static Thread Network_PINGM = new Thread(Networking::Ping_Master);
   static Thread Network_VerifyM = new Thread(Networking::VERIFY_MASTER_VER);
    static Thread Remote_Command = new Thread(Networking::Remote_Command);
    static Thread Network_API_CORE = new Thread(Networking::APINETWORK);
    static Thread Network_PUSH_SERVER = new Thread(Networking::Network_UPDATE_Server);
    static Thread Network_PUSH_UPDATE = new Thread(Networking::Network_Master_UPDATE);
    static Thread Network_BC_DB_PUBLISH = new Thread(Networking::ADD_RECORD);
    static Thread Network_BD_COREMGT = new Thread(Networking::NET_CMD);


    public static void main(String[] agrs) throws Exception {
        StringUtil.HASH();

        //new TEST_SEQ().Test_Chain();
        Networking.ADD_NET();
        DataBase.Get_Masters();

        Networking.Active_Threads.add(Network_CORE);
        Networking.Active_Threads.add(Network_GET);
        Networking.Active_Threads.add(Network_PINGM);
        Networking.Active_Threads.add(Network_VerifyM);
        Networking.Active_Threads.add(Remote_Command);
        Networking.Active_Threads.add(Network_API_CORE);
        Networking.Active_Threads.add(Network_PUSH_SERVER);
        Networking.Active_Threads.add(Network_PUSH_UPDATE);
        Networking.Active_Threads.add(Network_BC_DB_PUBLISH);

        for(Thread T: Networking.Active_Threads){
            System.out.println("Checking Thread: "+ T);
            if(!T.isAlive()){
                T.start();
                System.out.println("Starting Thread: "+ T);
            }
        }

        Network_BD_COREMGT.start();

    }
}
