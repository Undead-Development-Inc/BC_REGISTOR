public class Logger {

    public static void Logme(String Log){
        String MyIp = Networking.My_IP();
        if(Networking.Logs.size() >= 3){
            for(String L: Networking.Logs){
                DataBase.ADD_LOG(MyIp + ": " + L);
            }
        }else {
            Networking.Logs.add(Log);
        }

        return;
    }
}
