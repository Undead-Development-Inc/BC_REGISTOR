import com.mysql.cj.log.Log;

import java.util.ArrayList;

public class DATA_Procces implements Runnable{

    public static ArrayList<Block> SUS_BLOCKS = new ArrayList<>();
    public static ArrayList<String> SUS_NODES = new ArrayList<>();

    public static void Process_Package(Package_Blocks package_blocks, String IP){
        for(Block block: package_blocks.Newly_MinedBlocks){
            if(!Blockchain.BlockChain.contains(block)){
                if(!Blockchain.MBlocks_NV.contains(block)){
                    Blockchain.MBlocks_NV.add(block);
                    Networking.NEW_BLOCK(block);
                    Networking.Logs.add("GOT NEW MINED BLOCK: "+ block.getBlockHash());
                }
            }
        }

        for(Block block: Blockchain.MBlocks_NV){
            for(Transaction transaction: package_blocks.Newly_CreatedTransactions) {
                if (block.transactions.contains(transaction)){
                    Networking.Logs.add("DUPLICATE TRANSACTION: "+ transaction.transhash);
                }else {
                    Blockchain.Mine_Transactions.add(transaction);
                }
            }
        }

        for(Block block: package_blocks.blockchain){
            if(!Blockchain.BlockChain.contains(block)){
                System.out.println("FLAG BLOCK NOT EXISTING: "+ block.getBlockHash());
                Networking.Logs.add("FLAG BLOCK NOT EXISTING: "+ block.getBlockHash());
                SUS_BLOCKS.add(block);
                SUS_NODES.add(IP);
            }
        }

        for(Block block: SUS_BLOCKS){
            if(Networking.Verify_Chain(block) * 0.51 >= Networking.IPs.size()){
                Blockchain.BlockChain.add(block);
            }
        }
    }

    @Override
    public void run() {

    }
}
