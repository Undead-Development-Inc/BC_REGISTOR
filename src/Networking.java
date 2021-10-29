import com.google.inject.Key;
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
import java.security.spec.ECGenParameterSpec;
import java.util.Arrays;
import java.util.Objects;
import java.security.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Networking {

    public static ArrayList<String> IPs = new ArrayList<>();
    public static ArrayList<String> INACTIVE_IPS = new ArrayList<>();
    public static ArrayList<Thread> Active_Threads = new ArrayList<>();
    public static ArrayList<String> Logs = new ArrayList<>();
    public static Package_Blocks package_blocks;
    public static ArrayList<String> IPDNS_SYNC_NODES = new ArrayList<>();
    public static ArrayList<Object> Obj_2_Push = new ArrayList<>();
    public static ArrayList<Block> Sus_Chain = new ArrayList<>();
    public static ArrayList<String> Client_Conn_Allowed = new ArrayList<>();
    public static ArrayList<String> Clients_Connected = new ArrayList<>();
    public static ArrayList<ClientHandler> New_Clients = new ArrayList<>();
    public static ExecutorService CLIENTS_THREADS = Executors.newFixedThreadPool(900000);
    public static ExecutorService WAITLIST_NOCLIENTS = Executors.newFixedThreadPool(900000);
    

    public static void NETWORK_CORE() {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        while (true) {
            try (ServerSocket server = new ServerSocket(10000)) {
                if(server.isClosed()){
                    Networking.Logs.add("NETWORK CORE: CLOSED");
                }
                if(!server.isClosed()){
                    Networking.Logs.add("NETWORK CORE: ACTIVE");
                }

                try {
                    while (true) {
                        Socket socket = server.accept();
                        Networking.Logs.add("NETWORK CORE: GOT CONNECTION FROM "+ socket.getInetAddress());
                        socket.setSoTimeout(300);
                        System.out.println("GOT CONNECTION FROM: " + socket.getInetAddress());
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
//                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        System.out.println("REACHED 3");
                        //ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        System.out.println("REACHED 4");


                        Object Req = (Object) objectInputStream.readObject();



                        if (Req.getClass() == String.class) {
                            String R = (String) Req;
                            switch (R) {
                                case "Curr_Ver":
                                    objectOutputStream.writeObject(Curr_Ver());
                                case "Status":
                                    objectOutputStream.writeObject("Alive");

                                case "Command":
                                     for(String Input_Q: Auth_MGR.Net_INPUT_Q("Creds")){
                                        objectOutputStream.writeObject(Input_Q);
                                     }
                                     PublicKey pkey = (PublicKey) objectInputStream.readObject();
                                     PrivateKey privateKey = (PrivateKey) objectInputStream.readObject();
                                     String Passcode = (String) objectInputStream.readObject();
                                     if(Auth_MGR.IsUser(pkey, privateKey, Passcode)){
                                         objectOutputStream.writeObject(Settings.GREEN+ "LOGIN SUCCESS" + Settings.RESET);
                                         String Command = (String) objectInputStream.readObject();
                                         System.out.println("GOT COMMAND: "+ Command);
                                         Process process = Runtime.getRuntime().exec(Command);
                                     }else {
                                         objectOutputStream.writeObject(Settings.RED +"AUTH BAD!" + Settings.RESET);
                                     }

                                case "Contains_Block":
                                    Block block = (Block) objectInputStream.readObject();
                                    objectOutputStream.writeObject(block);
                                case "AGREE_CHAIN":
                                    ArrayList<Block> Longest_Offered = (ArrayList<Block>) objectInputStream.readObject();
                                    objectOutputStream.writeObject(Longest_Offered == Blockchain.LONGEST_BLOCKCHAIN());
                                    if (Longest_Offered == Blockchain.LONGEST_BLOCKCHAIN()) {
                                        Blockchain.PENDING_ACCEPTED_BLOCKCHAIN.addAll(Longest_Offered);
                                    }
                                case "WHOS_CLIENT":
                                    String Client_Address = (String) objectInputStream.readObject();
                                    if(!Clients_Connected.contains(Client_Address)){
                                        objectOutputStream.writeObject(false);
                                    }else {
                                        Socket socket1 = new Socket(My_IP(), 65534);
                                        socket1.close();
                                        objectOutputStream.writeObject(socket1);
                                    }
                                case "New_Mined_Block":
                                    Block block1 = (Block) objectInputStream.readObject();
                                    if (!Blockchain.BlockChain.contains(block1)) {
                                        if (!Blockchain.MBlocks_NV.contains(block1)) {
                                            Blockchain.MBlocks_NV.add(block1);
                                        }
                                    }
                                case "ADD_AUTHUSER":
//                                    PublicKey publicKey = (PublicKey) objectInputStream.readObject();
//                                    PrivateKey privateKey = (PrivateKey) objectInputStream.readObject();
//                                    String Password = (String) objectInputStream.readObject();
//                                    Auth_MGR.Add_USER(publicKey, privateKey, Password);

                            }
                        }
                        if (Req.getClass() == Package_Blocks.class) {
                            Package_Blocks pkg = (Package_Blocks) Req;
                        }

                        if (server.isClosed()) {
                            System.out.println("CLOSING");
                            socket.close();
                        }
                        socket.close();
                        Networking.Logs.add("NETWORK CORE: CONNECTION CLOSED "+ socket.getInetAddress());

                   }
                } finally {
                    try {
                        server.close();
                    } catch (Exception ex) {
                        System.out.println(ex);
                        Networking.Logs.add("ERROR IN NETWORKING: "+ ex);
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }
    }

    public static void Data_Client(){
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        while (true) {
            try (ServerSocket server = new ServerSocket(65534)) {
                if(server.isClosed()){
                    Networking.Logs.add("NETWORK CORE: CLOSED");
                }
                if(!server.isClosed()){
                    Networking.Logs.add("NETWORK CORE: ACTIVE");
                }

                try {
                    while (true) {
                        Socket socket = server.accept();
                        Networking.Logs.add("NETWORK CLIENT: GOT CONNECTION FROM "+ socket.getInetAddress());
                        socket.setSoTimeout(300);
                        System.out.println("GOT CONNECTION FROM: " + socket.getInetAddress());
                        System.out.println("REACHED 2");


                        ClientHandler client = new ClientHandler(socket);
                        New_Clients.add(client);
                        Logs.add(My_IP()+ " Created New Client: "+ client);


                    }
                } finally {
                    try {

                    } catch (Exception ex) {
                        System.out.println(ex);
                        Networking.Logs.add("ERROR IN CLIENT NETWORKING: "+ ex);
                        ex.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                System.out.println(ex.getStackTrace());
            }
        }

    }

    public static void MakeMatch_Clients(){
        Thread Client_Conn;
        try{
            while (true) {
                if(!(New_Clients.size() <= 1)){
                    for(ClientHandler client: New_Clients){
                        int last_id = New_Clients.lastIndexOf(client.Client_Address);
                        if(!(New_Clients.indexOf(client) == last_id)){
                            System.out.println("FOUND CLIENT CONNECTION");
                            Client_Mgr client_conn = new Client_Mgr(client, New_Clients.get(last_id));
                            Client_Conn = new Thread(client_conn);
                            CLIENTS_THREADS.execute(Client_Conn);
                            Logs.add("MAKING NEW CLIENT CONNECTION");
                        }
                    }
                }
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static void NEW_BLOCK(Block block){
        try{
            for(String Node: IPs){
                if (!Node.matches(My_IP())){
                    Socket socket = new Socket(Node, 10000);

                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                    objectOutputStream.writeObject("New_Mined_Block");
                    objectOutputStream.writeObject(null);
                    objectOutputStream.writeObject(block);

                    socket.close();
                }
                return;
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public static Socket Find_Client(String Client_Address){
        for(String Server: Networking.IPs){
            try{
                Socket socket = new Socket(Server, 10000);
                System.out.println("Connected");
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());


                objectOutputStream.writeObject("Whos_Client");

                Object res = (Object) objectInputStream.readObject();

                if(res.getClass() == Boolean.class){
                    Networking.Logs.add(Client_Address + "Client Not Found at: "+ My_IP());
                }
                if(res.getClass() == Socket.class){
                    Socket socket1 = (Socket) res;
                    return socket1;
                }


            }catch (Exception ex){
                System.out.println(ex);
            }
        }
        return null;
    }

    public static int Agree_ADD_Chain() {
        int agreed = 0;
        try {
            for (String Node : IPs) {
                if (!Node.matches(My_IP())) {
                    Socket socket = new Socket(Node, 10000);

                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                    objectOutputStream.writeObject(Blockchain.PENDING_ACCEPTED_BLOCKCHAIN);

                    Boolean Accepted = (Boolean) objectInputStream.readObject();

                    if (Accepted) {
                        agreed += 1;
                    }

                    socket.close();
                }
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return agreed;
    }

    public static int Verify_Chain(Block block){
        int Nodes_Containing = 0;
        try{
            for(String IP: IPs) {
                Socket socket = new Socket(IP, 10000);
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                objectOutputStream.writeObject("Contains_Block");
                objectOutputStream.writeObject(null);
                objectOutputStream.writeObject(block);

                Boolean isContained = (Boolean) objectInputStream.readObject();

                if(isContained){
                    Nodes_Containing += 1;
                }

            }
        }catch (Exception ex){
            Networking.Logs.add("ERROR VERIFY CHAIN: "+ ex);
            System.out.println(ex);
        }
        return Nodes_Containing;
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
