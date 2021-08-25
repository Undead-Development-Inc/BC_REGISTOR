import java.util.ArrayList;
import java.util.Date;

public class main {
    public static void main(String[] agrs) throws Exception {
        StringUtil.HASH();
        Wallet blankWallet = new Wallet();
        blankWallet = new Wallet();

        ArrayList<Transaction> G_BLOCK_TRANSACTION = new ArrayList<>();
        Block G_Block = new Block(G_BLOCK_TRANSACTION, "", new Date().getTime(), null);
        G_Block.miner = blankWallet.publicKey;
        //G_Block.calculateBlockHash();
        G_Block.diff = 1;
        System.out.println("The G-Block Hash is: "+ G_Block.getBlockHash());
        Blockchain.BlockChain.add(G_Block);
        //new TEST_SEQ().Test_Chain();
        Networking.ADD_NET();
        DataBase.Get_Masters();
        Thread Network_CORE = new Thread(Networking::Network_Accept);
        Thread Network_GET = new Thread(Networking::Network_GET);
        Thread Network_PINGM = new Thread(Networking::Ping_Master);
        Thread Network_VerifyM = new Thread(Networking::VERIFY_MASTER_VER);
        Thread Remote_Command = new Thread(Networking::Remote_Command);
        Thread Network_API_CORE = new Thread(Networking::APINETWORK);
        Thread Network_PUSH_SERVER = new Thread(Networking::Network_UPDATE_Server);
        Thread Network_PUSH_UPDATE = new Thread(Networking::Network_Master_UPDATE);
        Thread Network_BC_DB_PUBLISH = new Thread(Networking::ADD_RECORD);
        Networking.Active_Threads.add(Network_CORE);
        Networking.Active_Threads.add(Network_GET);
        Networking.Active_Threads.add(Network_PINGM);
        Networking.Active_Threads.add(Network_VerifyM);
        Networking.Active_Threads.add(Remote_Command);
        Networking.Active_Threads.add(Network_API_CORE);
        Networking.Active_Threads.add(Network_PUSH_SERVER);
        Networking.Active_Threads.add(Network_PUSH_UPDATE);
        Networking.Active_Threads.add(Network_BC_DB_PUBLISH);

        for(Thread T: Networking.Active_Threads){
            System.out.println("Checking Thread: "+ T);
            if(!T.isAlive()){
                T.start();
                System.out.println("Starting Thread: "+ T);
            }
        }

    }
}
