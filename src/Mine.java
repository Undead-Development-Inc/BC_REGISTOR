import jdk.jshell.spi.ExecutionControlProvider;


import java.security.PublicKey;
import java.util.Date;

public class Mine {

    public void mine(PublicKey miner) {
        try {

            Block block1 = new Block(Blockchain.Mine_Transactions, Blockchain.BlockChain.get(Blockchain.BlockChain.size() -1).getBlockHash(), new Date().getTime(), miner);
            new Chain_Verification().givenBlockchain_whenNewBlockAdded_thenSuccess(block1);
            new Chain_Verification().givenBlockchain_whenValidated_thenSuccess();

            System.out.println(block1);

        }catch (Exception ex){

        }
    }

    public void TEST_mine() {
        try {
            Wallet wallet_test_miner = new Wallet();
            wallet_test_miner = new Wallet();
            System.out.println("WALLET KEY: "+ wallet_test_miner.publicKey);


            Block block1 = new Block(Blockchain.Test_Mine_Transactions, Blockchain.TEST_BlockCain.get(Blockchain.TEST_BlockCain.size() -1).getBlockHash(), new Date().getTime(), wallet_test_miner.publicKey);
            System.out.println("Starting to Mine Block: "+ block1);
            new Chain_Verification().TEST_givenBlockchain_whenNewBlockAdded_thenSuccess(block1);
            new Chain_Verification().TEST_givenBlockchain_whenValidated_thenSuccess();

            System.out.println(block1);

        }catch (Exception ex){

        }
    }




}
