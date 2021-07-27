public class main {
    public static void main(String[] agrs){
        DataBase.Get_Masters();
        Thread Network = new Thread(Networking::Network_Accept);
        Network.start();

    }
}
