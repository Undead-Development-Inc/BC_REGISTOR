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
import java.util.Objects;

public class Networking {

    public static ArrayList<String> IPs = new ArrayList<>();
    public static ArrayList<String> INACTIVE_IPS = new ArrayList<>();
    public static ArrayList<Thread> Active_Threads = new ArrayList<>();
    public static ArrayList<String> Logs = new ArrayList<>();
    public static Package_Blocks package_blocks;
    public static ArrayList<String> IPDNS_SYNC_NODES = new ArrayList<>();
    public static ArrayList<Object> Obj_2_Push = new ArrayList<>();
    Logger loger = new Logger();

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

    public static void Network_Accept() { ///GETS CONNECTION AND VERIFIES AND ADDS MASTERS IF VALID!
        while (true) {
            try {
                System.out.println("Waiting For Connection!!!");
                Logger.Logme("Network_Accept Waiting for Connection");
                ServerSocket serverSocket = new ServerSocket(20);
                Socket socket = serverSocket.accept();
                Logger.Logme("Network_Accept Got Connection From: " + socket.getInetAddress());
                System.out.println("[LOG] Got Connection From " + socket.getInetAddress());
                System.out.println("[LOG] Checking Server Ver on " + socket.getInetAddress());

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                String SVER = (String) ois.readObject();

                if (SVER.matches(Curr_Ver())) {
                    Logger.Logme("Network_Accept Accepted Connection");
                    Logs.add("Network ACCEPTED Connection from: " + socket.getInetAddress());
                    if (!DataBase.FIND_Masters(socket.getInetAddress().toString())) {
                        DataBase.ADD_Master(socket.getInetAddress().toString(), SVER);
                        oos.writeObject(IPs);
                    }
                } else {
                    Logger.Logme("Network_Accept DENIED Connection");
                    Logs.add("Network DENIED Connection from: " + socket.getInetAddress());
                    System.out.println("ERROR ON SERVER: VER DOSE NOT MATCH HOST");
                    oos.writeObject(0);
                }

            }catch(Exception ex){
                Logger.Logme(ex.toString());
                System.out.println("Error in Network_Accept: " + ex);
            }
        }
    }




    public static void Network_GET() {  ///THIS IS THE MASTER PING SERVER
        while (true) {
            try {
                ServerSocket ss = new ServerSocket(10000);

                System.out.println("Waiting for Connection from a server");
                Socket socket = ss.accept();
                System.out.println("GOT connection from IP: "+ socket.getInetAddress());

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                if(socket.isConnected()){


                    String Master_Ver = (String) ois.readObject();
                    ArrayList<String> Temp_List = new ArrayList<>();

                    System.out.println("Current WW Ver: "+ Curr_Ver());
                    System.out.println("Current Requested Server Ver: "+ Master_Ver);
                    System.out.println("MATCHES: "+ Curr_Ver().matches(Master_Ver));

                    if (Master_Ver.equals(Curr_Ver())) {
                        System.out.println("VER MATCHED - CHECKING IF MASTER IN LIST");
                        if (!IPs.contains(socket.getInetAddress().toString())) {
                            IPs.add(socket.getInetAddress().toString());
                            System.out.println("MASTER VALIDATED: "+ socket.getInetAddress());
                        }else {
                            System.out.println("Master Not Added -- Already IN LIST");
                        }
                        for (String IP : IPs) {
                            if (!IP.matches(socket.getInetAddress().toString())) {
                                Temp_List.add(IP);
                                System.out.println("Added Master to temp list: "+ IP);
                            }
                        }
                        oos.write(1);
                    }
                    if(Temp_List.isEmpty()){
                        oos.writeObject(true);
                    }else {
                        oos.writeObject(false);
                        oos.writeObject(Temp_List);
                    }
                } else {
                    oos.write(0); //0 IS FALSE -NOT ADDED
                }

                oos.close();
                ois.close();
                socket.close();


            } catch (Exception ex) {
                Logger.Logme(ex.toString());
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


    //THIS VERIFYS MASTER VERSIONS
    public static void VERIFY_MASTER_VER(){
        ArrayList<String> current = new ArrayList<>(1);
        while(true){
            try{
                for(String mIP: IPs){
                    current.add(mIP);
                    System.out.println("Connecting to " + mIP);
                    Socket socket = new Socket(mIP, 10000);
                    current.remove(1);

                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());

                    oos.writeObject(QCCHAIN.Ver);

                    oos.close();
                    ois.close();
                    socket.close();
                }
            }catch (Exception ex){
                Logger.Logme(ex.toString());
                IPs.remove(current.get(0));
                System.out.println("Removing: "+ current.get(0));
                current.remove(0);
            }
        }
    }

    public static void Remote_Command(){    ///THIS IS A REMOTE SERVER NET FUNCTION TO GET COMMANDS AND RUN THEM
        System.out.println("Waiting For Connection on Remote Command");
        while(true){
            try{
                ServerSocket ss = new ServerSocket(40);
                Socket socket = ss.accept();

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                System.out.println("Got Connection from: "+ socket.getInetAddress());

                String Request = (String) objectInputStream.readObject();

                if(Request.matches("Curr_Ver")){
                    objectOutputStream.writeObject(Curr_Ver());
                }

                if(Request.matches("GET_LOGS")){
                    System.out.println("Got Request for Logs");
                    objectOutputStream.writeObject(Logs);
                }

                if(Request.matches("GET_CURR_THREADS")){
                    System.out.println("Request to Get Threads");
                    objectOutputStream.writeObject(Networking.Active_Threads);
                }

                if(Request.matches("UPDATE")){
                    System.out.println("Updating System");
                    Process p = Runtime.getRuntime().exec("reboot");
                }

                if(Request.matches("Stop_Connections")){
                    System.out.println("Stopping Threads");
                    Stop_Connections();
                }
                socket.close();
                
            }catch (Exception ex){
                Logger.Logme(ex.toString());
            }
        }
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

    public static void Network_UPDATE_Server(){
        System.out.println("Starting Update Master Server");
        while(true){
            try {
                ServerSocket serverSocket = new ServerSocket(Settings.Network_Master_UPDATE_SS_PORT);
                Socket socket = serverSocket.accept();

                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());


                if(ois.readObject().getClass() == Block.class){
                    //RECIVING A NEW BLOCK
                    Block newblock = (Block) ois.readObject();
                    if(!Blockchain.MBlocks_NV.contains(newblock)){
                        Blockchain.MBlocks_NV.add(newblock);
                        Logs.add("Added New Block: "+ newblock.getBlockHash());
                    }
                }

                if(ois.readObject().getClass() == Blockchain.Mine_Transactions.getClass()){
                    //FOR RECIVING ALIST OF TRANSACTIONS IN ARRAY
                    ArrayList<Transaction> New_Transactions = (ArrayList<Transaction>) ois.readObject();
                    for(Transaction transaction: New_Transactions){
                        if(!Blockchain.Mine_Transactions.contains(transaction)){
                            System.out.println("ADDED NEW TRANSACTION: "+ transaction);
                            Logs.add("Got New Transaction: "+ transaction.transhash);
                            Blockchain.Mine_Transactions.add(transaction);
                        }
                    }
                }

                if(ois.readObject().getClass() == Message_Package.class){
                    //RECIVING A MESSAGE PACKET
                    Message_Package message = (Message_Package) ois.readObject();
                    if(!Blockchain.Temp_Messages.contains(message)){
                        Blockchain.Temp_Messages.add(message);
                    }
                }


                socket.close();
                serverSocket.close();
            }catch (Exception ex){

            }
        }
    }

    public static void Network_Master_UPDATE(){
        while(true) {
            for (Object obj : Obj_2_Push) {
                for (String IP : IPs) {
                    try {
                        System.out.println("Attempting PUSH to: " + IP);
                        Logs.add("Attempting Push to: " + IP);
                        Socket socket = new Socket(IP, Settings.Network_Master_UPDATE_SS_PORT);
                        socket.setSoTimeout(10);
                        Logs.add("PUSH CONNECTION SUCCESSFULL");
                        System.out.println("Connected to: " + IP);

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                        oos.writeObject(obj);
                    } catch (Exception ex) {

                    }
                }
            }
        }
    }

    public static void NET_CMD(){
        while (true) {
            try {
                ServerSocket serverSocket = new ServerSocket(93);
                Socket socket = serverSocket.accept();

                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

                String Req = (String) objectInputStream.readObject();

                if(Req.matches("Start_NCORE")){
                    if(main.Network_CORE.isAlive()){
                        main.Network_CORE.stop();
                    }
                    main.Network_CORE.start();
                    objectOutputStream.writeObject(main.Network_CORE.isAlive());
                }
                if(Req.matches("RESET_THREADS")){
                    for(Thread thread: Networking.Active_Threads){
                        Logger.Logme("Killing Thread: "+ thread);
                        thread.stop();
                        thread.start();
                        Logger.Logme("Starting Thread");
                    }
                }
                if(Req.matches("RESET_PROCC")){
                    Process p = Runtime.getRuntime().exec("systemctl restart dnsnode");
                }
                if(Req.matches("RESTART")){
                    Process p = Runtime.getRuntime().exec("reboot");
                }
                socket.close();
            }catch (Exception ex){
                Logger.Logme(ex.toString());
            }
        }
        }


    public static void APINETWORK() {
        System.out.println("TRYING");
        while (true) {
            try {
                package_blocks = null;
                Pack_ME();
                System.out.println("WAITING FOR CONNECTION");
                ServerSocket serverSocket = new ServerSocket(Settings.INET_Trans_Port);
                Socket socket = serverSocket.accept();
                System.out.println("CONNECTED!!!");
                socket.setSoTimeout(10);

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

                String req = (String) objectInputStream.readObject();



                if(req.matches("PUSH_MBLOCK")){
                    Block New_Blocks = (Block) objectInputStream.readObject();
                    System.out.println("GOT NEW BLOCK: "+ New_Blocks);
                    Blockchain.MBlocks_NV.add(New_Blocks);
                    System.out.println("BLOCK-SIZE: "+ Blockchain.MBlocks_NV.size());
                    if(!Blockchain.MBlocks_NV.contains(New_Blocks)){
                        throw new Exception("BLOCK NOT FOUND!!!");
                    }
                    Blockchain.Add_To_Chain();//
//                    Blockchain.BlockChain.add(New_Blocks);
                    objectInputStream.close();
                    objectOutputStream.close();
                }

                if(req.matches("PUSH_N_TRANSACTIONS")){
                    ArrayList<Transaction> New_Transactions = (ArrayList<Transaction>) objectInputStream.readObject();
                    for(Transaction transaction: New_Transactions){
                        if(!Blockchain.Mine_Transactions.contains(transaction)){
                            System.out.println("ADDED NEW TRANSACTION: "+ transaction);
                            Blockchain.Mine_Transactions.add(transaction);
                        }
                    }
                }

                if(req.matches("Wallet")){
                    if(Blockchain.Net_Wallets.size() == 0){
                        Wallet newwallet = new Wallet();
                        newwallet = new Wallet();
                        Blockchain.Net_Wallets.add(newwallet);
                        objectOutputStream.writeObject(newwallet);
                        System.out.println("MADE AND SENT NEW WALLET");
                    }else {
                        objectOutputStream.writeObject(Blockchain.Net_Wallets.get(0));
                        System.out.println("SEND WALLET");
                    }
                }

                if(req.matches("Get_Balance")){
                    Wallet wallet = (Wallet) objectInputStream.readObject();
                    objectOutputStream.writeObject(wallet.Balance(wallet));
                    System.out.println("RETURNED BALLET BALANCE");
                }

                if(req.matches("Get_Difficulty")){
                    objectOutputStream.writeObject(new Difficulty().difficulty());
                }

                if(req.matches("GET_BC_UPDATE")){
                    objectOutputStream.writeObject(Blockchain.BlockChain);
                }

                if(req.matches("GET_NEW_TRANSACTIONS")){
                    objectOutputStream.writeObject(Blockchain.Mine_Transactions);
                }

                if(req.matches("GET_NEW_MINED_BLOCKS")){
                    objectOutputStream.writeObject(Blockchain.MBlocks_NV);
                }

                objectInputStream.close();
                objectOutputStream.close();
                socket.close();
                serverSocket.close();


            }catch (Exception ex) {
                Logger.Logme(ex.toString());
                System.out.println(Settings.RED + ex);
            }

        }

    }
}
