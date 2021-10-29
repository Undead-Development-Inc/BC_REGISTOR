import java.util.ArrayList;
import java.util.Date;

public class Logger {

    public static void Logme(){
        String MyIp = Networking.My_IP();
        ArrayList<String> Old_Logs = new ArrayList<>();
        while (true) {
            for (String L : Networking.Logs) {
                DataBase.ADD_LOG(MyIp + ": " + new Date().getDate() + ": " + new Date().getTime() + " :  " + L);
                Old_Logs.add(L);
            }
            Networking.Logs.removeAll(Old_Logs);
        }
    }
}
