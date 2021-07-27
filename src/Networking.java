import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class Networking {

    public static ArrayList<String> IPs = new ArrayList<>();

    public static void Network_Accept(){
        try{
            System.out.println("Waiting For Connection!!!");
            ServerSocket serverSocket = new ServerSocket(10);
            Socket socket = serverSocket.accept();
            System.out.println("Got Connection From "+ socket.getInetAddress());
            System.out.println("Checking Server Ver on "+ socket.getInetAddress());

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            String SVER = (String) ois.readObject();

            if(SVER.matches(Curr_Ver())){
                if(!DataBase.FIND_Masters(socket.getInetAddress().toString())){
                    DataBase.ADD_Master(socket.getInetAddress().toString(), SVER);
                    oos.writeObject(IPs);
                }
            }else {
                System.out.println("ERROR ON SERVER: VER DOSE NOT MATCH HOST");
                oos.writeObject(0);
            }
        }catch (Exception ex){
            System.out.println("Error in Network_Accept: "+ ex);
        }
    }

    public static String Curr_Ver(){
        String Ver = "";
        try{
            URL url = new URL("http://qcnetworks.ca/master/verr.php");

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String line;

            while ((line = in.readLine()) !=  null){
                System.out.println("Current Server Ver is: "+ line);
                Ver += line;
            }
        }catch (Exception exception){
            System.out.println(exception);
        }
        return Ver;
    }
}
