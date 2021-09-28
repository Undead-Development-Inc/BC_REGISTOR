import com.mysql.cj.log.Log;
import com.mysql.cj.x.protobuf.MysqlxExpr;
import org.junit.jupiter.api.parallel.Execution;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Networking {

    public static ArrayList<String> IPs = new ArrayList<>();
    public static ArrayList<String> INACTIVE_IPS = new ArrayList<>();
    public static ArrayList<Thread> Active_Threads = new ArrayList<>();
    public static ArrayList<String> Logs = new ArrayList<>();
    public static Package_Blocks package_blocks;
    public static ArrayList<String> IPDNS_SYNC_NODES = new ArrayList<>();
    public static ArrayList<Object> Obj_2_Push = new ArrayList<>();
    public static ArrayList<Block> Sus_Chain = new ArrayList<>();
    Logger loger = new Logger();

    public static void NETWORK_CORE(){
        try{
            while (true){
                ServerSocket serverSocket = new ServerSocket(10000);
                Socket socket = serverSocket.accept();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());



                if(objectInputStream.readObject() == String.class){
                    String Str = (String) objectInputStream.readObject();
                    Logger.Logme("Got Command: " + Str);

                    if(Str.matches("Curr_Ver")){
                        objectOutputStream.writeObject(Curr_Ver());
                        Logger.Logme("SENT VER: "+ Curr_Ver());
                    }
                    if(Str.matches("Run_Commands")){
                        ArrayList<String> Commands = (ArrayList<String>) objectInputStream.readObject();
                        for(String C: Commands){
                            Process p = Runtime.getRuntime().exec(C);
                        }
                    }
                }

                if(objectInputStream.readObject() == ArrayList.class.asSubclass(Block.class)){
                   ArrayList<Block> blockArrayList = (ArrayList<Block>) objectInputStream.readObject();
                   for(Block block: blockArrayList){
                       if(!Blockchain.BlockChain.contains(block)){
                           if(!Blockchain.MBlocks_NV.contains(block)){
                               Logger.Logme("Adding New Block: "+ block.getBlockHash());
                               Blockchain.MBlocks_NV.add(block);
                           }
                       }
                   }
                }

                if(objectInputStream.readObject() == ArrayList.class.asSubclass(Transaction.class)){
                    ArrayList<Transaction> New_Transactions = (ArrayList<Transaction>) objectInputStream.readObject();
                    for(Transaction transaction: New_Transactions){
                        if(!Blockchain.Mine_Transactions.contains(transaction)){
                            Logger.Logme("Got New_Transaction: " + transaction);
                            Blockchain.Mine_Transactions.add(transaction);
                        }
                    }
                }

                if(objectInputStream.readObject() == ArrayList.class.asSubclass(Blockchain.BlockChain.getClass())){
                    ArrayList<Block> Recived_Blockchain = (ArrayList<Block>) objectInputStream.readObject();
                    for(Block block: Recived_Blockchain){
                        Sus_Chain.add(block);
                    }
                }

                objectOutputStream.flush();
                objectOutputStream.close();
                objectInputStream.close();
                socket.close();
            }
        }catch (Exception ex){
            Logger.Logme(ex.toString());
        }
    }

    public static Boolean Verify_Chain(ArrayList<Block> Sus_Chain){
        try{
            while (true){
                Socket socket = new Socket();
            }
        }catch (Exception ex){
            Logger.Logme("ERROR VERIFY CHAIN: "+ ex);
            System.out.println(ex);
        }
        return true;
    }

    public static void ADD_NET() {
        if (!DataBase.FIND_DNSNODE(My_IP())) {
            System.out.println("Adding self to registry");
            DataBase.ADD_REGISTOR();
        }
        return;
    }

    public static void ADD_RECORD() {
        while (true) {
            try {
                for (Block block : Blockchain.BlockChain) {
                    if (!DataBase.FIND_RECORD_BLOCK(block)) {
                        DataBase.ADD_BC_RECORD(block);
                    }
                }
                Thread.sleep(1000);
            } catch (Exception ex) {

            }
        }
    }


    public static void Ping_Master(){   //PINGS ANY MASTER SERVER
        ArrayList<String> Current_Checking_IP = new ArrayList<>(1);
        while (true) {
            try{
                for (String IP: IPs){
                    Logs.add("Trying to Ping: "+ IP);
                    if(!IP.matches(My_IP())) {
                        System.out.println("[LOG] Trying to Ping: " + IP);
                        Current_Checking_IP.add(IP);
                        Socket socket = new Socket(IP, 10000);
                        socket.setSoTimeout(10);
                        System.out.println("[LOG] IP: " + IP + " Is Active: " + socket.isConnected());
                        Logs.add("Ping ALIVE: " + IP);
                        socket.close();
                    }
                }
            }catch (Exception EX){
                Logs.add("FAILED to Ping: "+ Current_Checking_IP.get(0));
                System.out.println("[LOG] Removing: " + Current_Checking_IP.get(0));
                INACTIVE_IPS.add(Current_Checking_IP.get(0));
                IPs.remove(Current_Checking_IP.get(0));
                Current_Checking_IP.remove(0);

            }
        }
    }

    public static String Curr_Ver(){   //THIS PRODUCES THE CURRENT NETWORK (MASTER) VERSION EXPECTED
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

    public static String My_IP(){
        String PublicIP = "";
        try{
            URL checkip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(checkip.openStream()));
            String IP = in.readLine();
            PublicIP += IP;
            return IP;
        }catch (Exception ex){

        }
        return PublicIP;
    }

    public static void Stop_Connections(){
        for(Thread thread: Active_Threads){
            System.out.println("Closing Thread: "+ thread);
            thread.stop();
        }
        Active_Threads.clear();
        return;
    }


    public static void Pack_ME(){
        Package_Blocks package_blocks1 = new Package_Blocks();
        package_blocks1.blockchain = Blockchain.BlockChain;
        package_blocks1.Newly_MinedBlocks = Blockchain.MBlocks_NV;
        package_blocks1.Newly_CreatedTransactions = Blockchain.Mine_Transactions;
        package_blocks = package_blocks1;
        return;
    }
}
