import org.junit.jupiter.api.parallel.Execution;

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
    public static ArrayList<String> INACTIVE_IPS = new ArrayList<>();

    public static void Network_Accept(){
        try{
            System.out.println("Waiting For Connection!!!");
            ServerSocket serverSocket = new ServerSocket(20);
            Socket socket = serverSocket.accept();
            System.out.println("[LOG] Got Connection From "+ socket.getInetAddress());
            System.out.println("[LOG] Checking Server Ver on "+ socket.getInetAddress());

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

    public static void Ping_Master(){
        ArrayList<String> Current_Checking_IP = new ArrayList<>(1);
        while (true) {
            try{
                for (String IP: IPs){
                    System.out.println("[LOG] Trying to Ping: "+ IP);
                    Current_Checking_IP.add(IP);
                    Socket socket = new Socket(IP, 10000);
                    socket.setSoTimeout(10);
                    System.out.println("[LOG] IP: "+ IP + " Is Active: " + socket.isConnected());
                    socket.close();
                }
            }catch (Exception EX){
                System.out.println("[LOG] Removing: " + Current_Checking_IP.get(0));
                INACTIVE_IPS.add(Current_Checking_IP.get(0));
                IPs.remove(Current_Checking_IP.get(0));
                Current_Checking_IP.remove(0);

            }
        }
    }

    public static void Status_Net(){
        while(true){
            try{
                ServerSocket serverSocket = new ServerSocket(30);
                Socket socket = serverSocket.accept();
                System.out.println("GOT REQUEST FROM DEVICE: "+ socket.getInetAddress());


                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                String request = (String) objectInputStream.readObject();

                if (request.matches("Net_Status")){
                    objectOutputStream.writeObject("Active");
                }
            }catch (Exception ex){

            }
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