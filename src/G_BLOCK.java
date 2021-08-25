import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class G_BLOCK {


    public void SETUP_GBLOCK() throws Exception {
        ArrayList<Transaction> G_BLOCK_TRANSACTION = new ArrayList<>();
        Block G_Block = new Block(G_BLOCK_TRANSACTION, "", new Date().getTime(), null);
        G_Block.calculateBlockHash();
        G_Block.diff = 1;
        System.out.println("The G-Block Hash is: "+ G_Block.getBlockHash());
        Blockchain.BlockChain.add(G_Block);
        if(Blockchain.BlockChain.contains(G_Block)){
            System.out.println(Settings.PURPLE + Settings.BLACK_BACKGROUND + "G_BLOCK ADDED TO CHAIN!!");
            return;
        }else{
            System.out.println("ERROR: G_BLOCK NOT ADDED TO CHAIN!!!");
            new ErrorMgr().errormgr(12);
        }
    }
}
