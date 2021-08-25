public class main {
    public static void main(String[] agrs){
        Networking.ADD_NET();
        DataBase.Get_Masters();
        Thread Network_CORE = new Thread(Networking::Network_Accept);
        Thread Network_GET = new Thread(Networking::Network_GET);
        Thread Network_PINGM = new Thread(Networking::Ping_Master);
        Thread Network_VerifyM = new Thread(Networking::VERIFY_MASTER_VER);
        Thread Remote_Command = new Thread(Networking::Remote_Command);
        Thread Network_API_CORE = new Thread(Networking::APINETWORK);
        Networking.Active_Threads.add(Network_CORE);
        Networking.Active_Threads.add(Network_GET);
        Networking.Active_Threads.add(Network_PINGM);
        Networking.Active_Threads.add(Network_VerifyM);
        Networking.Active_Threads.add(Remote_Command);
        Networking.Active_Threads.add(Network_API_CORE);

        for(Thread T: Networking.Active_Threads){
            System.out.println("Checking Thread: "+ T);
            if(!T.isAlive()){
                System.out.println("Starting Thread: "+ T);
            }
        }

    }
}
