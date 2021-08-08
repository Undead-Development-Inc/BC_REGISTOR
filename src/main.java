public class main {
    public static void main(String[] agrs){
        DataBase.Get_Masters();
        Thread Network = new Thread(Networking::Network_Accept);
        Thread Master_Ping = new Thread(Networking::Ping_Master);
        Thread Status_Net = new Thread(Networking::Status_Net);
        Thread Remote_Command = new Thread(Networking::Remote_Command);
        Networking.Active_Threads.add(Network);
        Networking.Active_Threads.add(Master_Ping);
        Networking.Active_Threads.add(Status_Net);
        Remote_Command.start();
        Network.start();
        Master_Ping.start();
        Status_Net.start();

    }
}
