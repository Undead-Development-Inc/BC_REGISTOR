import java.util.ArrayList;
import java.util.Date;

public class main {

    static Thread Network_Core = new Thread(Networking::NETWORK_CORE);
    static Thread Network_PINGM = new Thread(Networking::Ping_Master);
    static Thread Network_BC_DB_PUBLISH = new Thread(Networking::ADD_RECORD);



    public static void main(String[] agrs) throws Exception {
        StringUtil.HASH();

        //new TEST_SEQ().Test_Chain();
        Networking.ADD_NET();
        DataBase.Get_Masters();


        Networking.Active_Threads.add(Network_PINGM);
        Networking.Active_Threads.add(Network_Core);
        Networking.Active_Threads.add(Network_BC_DB_PUBLISH);

        for(Thread T: Networking.Active_Threads){
            System.out.println("Checking Thread: "+ T);
            if(!T.isAlive()){
                T.start();
                System.out.println("Starting Thread: "+ T);
            }
        }



    }
}
