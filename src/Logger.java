public class Logger {

    public static void Logme(String Log){
        String MyIp = Networking.My_IP();
        DataBase.ADD_LOG(MyIp + ": " + Log);
        return;
    }
}
