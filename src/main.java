public class main {
    public static void main(String[] agrs){
        DataBase.Get_Masters();
        Thread Network = new Thread(Networking::Network_Accept);
        Thread Master_Ping = new Thread(Networking::Ping_Master);
        Thread Status_Net = new Thread(Networking::Status_Net);
        Network.start();
        Master_Ping.start();
        Status_Net.start();

    }
}
