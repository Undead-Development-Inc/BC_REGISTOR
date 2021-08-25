import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.testng.annotations.AfterClass;



public class Chain_Verification {
    public static int Diff = new Difficulty().difficulty();
    public static String prefixString = new String(new char[Diff]).replace('\0', '0');

    public void givenBlockchain_whenNewBlockAdded_thenSuccess(Block block) throws  ClassNotFoundException, IOException {
        try {

            Block newBlock = new Block(block.get_DATA(), block
                    .getBlockHash(), new Date().getTime(), block.miner);
            for(Transaction transaction : block.get_DATA()){
                transaction.BlockHash = block.getBlockHash();
            }
            newBlock.mineBlock(Diff);
            newBlock.clear_used_transactions(newBlock.getBlockHash());
            System.out.println("Hashing");
            assertTrue(newBlock.getBlockHash()
                    .substring(0, Diff)
                    .equals(prefixString));


            Blockchain.BlockChain.add(newBlock);

            System.out.println("BLOCK: " + Blockchain.BlockChain.lastIndexOf(block) + " Previous Hash: " + block.getPreviousHash());

            return;
        }catch (Exception ex){

        }
    }


    public void givenBlockchain_whenValidated_thenSuccess() {
        try {
            boolean flag = true;
            for (int i = 0; i < Blockchain.BlockChain.size(); i++) {
                flag = Blockchain.BlockChain.get(i).getBlockHash().matches(Blockchain.BlockChain.get(i).calculateBlockHash());
                flag = Blockchain.BlockChain.get(i).getPreviousHash().matches(Blockchain.BlockChain.get(i -1).calculateBlockHash());
                flag= Blockchain.BlockChain.get(i).Calculate_MerkleRoot().matches(Blockchain.BlockChain.get(i).Merkleroot);

                if (!flag)
                    System.out.println("Removing Block Due to FLAG: "+ Blockchain.BlockChain.get(i));
                Blockchain.BlockChain.remove(i);
                break;
            }
            assertTrue(flag);
        }catch (Exception ex){

        }
    }




    /////TEST FUNCTIONS!!!!/////
    public void TEST_givenBlockchain_whenNewBlockAdded_thenSuccess(Block block) throws  ClassNotFoundException, IOException {
        try {

            Block newBlock = new Block(block.get_DATA(), block
                    .getBlockHash(), new Date().getTime(), block.miner);
            for(Transaction transaction : block.get_DATA()){
                transaction.BlockHash = block.getBlockHash();
            }
            newBlock.mineBlock(Diff);
            newBlock.clear_used_transactions(newBlock.getBlockHash());
            System.out.println("Hashing");
            assertTrue(newBlock.getBlockHash()
                    .substring(0, Diff)
                    .equals(prefixString));


            Blockchain.TEST_BlockCain.add(newBlock);
            System.out.print(Settings.RED_BOLD + "TEST FUNCTION" );
            System.out.println("BLOCK: " + Blockchain.TEST_BlockCain.lastIndexOf(block) + " Previous Hash: " + block.getPreviousHash());

            return;
        }catch (Exception ex){

        }
    }
    public void TEST_givenBlockchain_whenValidated_thenSuccess() {
        try {
            boolean flag = true;
            for (int i = 0; i < Blockchain.TEST_BlockCain.size(); i++) {
                flag = Blockchain.TEST_BlockCain.get(i).getBlockHash().matches(Blockchain.TEST_BlockCain.get(i).calculateBlockHash());
                flag = Blockchain.TEST_BlockCain.get(i).getPreviousHash().matches(Blockchain.TEST_BlockCain.get(i -1).calculateBlockHash());
                flag= Blockchain.TEST_BlockCain.get(i).Calculate_MerkleRoot().matches(Blockchain.TEST_BlockCain.get(i).Merkleroot);


                if (!flag)
                    System.out.println("TEST FUNCTION"+"Removing Block Due to FLAG: "+ Blockchain.TEST_BlockCain.get(i));
                Blockchain.TEST_BlockCain.remove(i);
                break;
            }
            assertTrue(flag);
        }catch (Exception ex){

        }
    }


}
