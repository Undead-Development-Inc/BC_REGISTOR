import java.util.Date;
import java.util.concurrent.ExecutionException;

public class TEST_SEQ {
    public void Test_Chain(){
        Block block = new Block(null, "0", new Date().getTime(), null);
        block.diff = 0;
        Blockchain.TEST_BlockCain.add(block);
        CONTROL();


    }
    public void CONTROL(){
        for(int x = 0; x <= 1; x++){
            new TEST_FUNCTIONS().Random_Wallets();
            new TEST_FUNCTIONS().CREATE_RANDOM_TRANSACTION();
            new Mine().TEST_mine();
            TEST_Transaction();
            if(Test_Blocks()){
                System.out.println("TEST PASSED!!");
            }
            new TEST_FUNCTIONS().CLEAN_TEST();
            System.out.println("BLOCKCHAIN TEST SIZE: "+ Blockchain.TEST_BlockCain.size());
        }
        return;
    }
    public boolean Test_Blocks() {
        Boolean flag = false;
        try {
            for(Block block: Blockchain.TEST_BlockCain){
                int i = Blockchain.TEST_BlockCain.lastIndexOf(block);
                new TEST_FUNCTIONS().Modify_CHAIN(i);
            }
            for (Block block : Blockchain.TEST_BlockCain) {
                flag = !Blockchain.TEST_BlockCain.get(Blockchain.TEST_BlockCain.lastIndexOf(block)).getBlockHash().matches(Blockchain.TEST_BlockCain.get(Blockchain.TEST_BlockCain.lastIndexOf(block)).calculateBlockHash());//SHOULD RETURN TURE DUE TO MODIFIED BLOCK
                if(!flag){
                    System.out.println(Settings.RED_BOLD +"FAILED AT TESTING MODIFIED (HASH != LAST BLOCK) BLOCK: "+ block);
                    DATA.MODIFIED_BLOCKS_FAILED.add(block);
                    return flag;
                }
                flag = !Blockchain.TEST_BlockCain.get(Blockchain.TEST_BlockCain.lastIndexOf(block)).getPreviousHash().matches(Blockchain.TEST_BlockCain.get(Blockchain.TEST_BlockCain.lastIndexOf(block) -1).calculateBlockHash());//SHOULD RETURN TURE DUE TO MODIFIED BLOCK
                if(!flag){
                    System.out.println(Settings.RED_BOLD +"FAILED AT TESTING MODIFIED (PREV-BLK-HSH != LBH-CALC) BLOCK: "+ block);
                    DATA.MODIFIED_BLOCKS_FAILED.add(block);
                    return flag;
                }
                flag = !Blockchain.TEST_BlockCain.get(Blockchain.TEST_BlockCain.lastIndexOf(block)).Calculate_MerkleRoot().matches(Blockchain.TEST_BlockCain.get(Blockchain.TEST_BlockCain.lastIndexOf(block)).Merkleroot);//SHOULD RETURN TURE DUE TO MODIFIED BLOCK
                if(!flag){
                    System.out.println(Settings.RED_BOLD +"FAILED AT TESTING MODIFIED (LST_BLK_MERKLEROOT != LST_BLK_MKRT) BLOCK: "+ block);
                    DATA.MODIFIED_BLOCKS_FAILED.add(block);
                    return flag;
                }
                flag = !(block.transactions.size() == 0);

            }
            return flag;
        }catch (Exception ex){

        }
        return flag;
    }

    public boolean TEST_Transaction(){

        Boolean flag = false;
        try {
            new TEST_FUNCTIONS().Modify_Transactions();
            for (Transaction Transaction : DATA.TEST_ORIGINAL_TRANSACTIONS) {
                for (Transaction transaction_M : Blockchain.Test_Mine_Transactions) {
                    flag = !Transaction.transhash.matches(StringUtil.applySha256(transaction_M.from_address + transaction_M.Recpt_address + transaction_M.value + transaction_M.BlockHash));
                    if (flag) {
                        System.out.println(Settings.GREEN_BOLD + "Transaction Failed: " + transaction_M);
                        DATA.FAILED_TRANSACTIONS_TEST.add(transaction_M);
                    } else {
                        throw new Exception("TRANSACTION: " + transaction_M + " PASSED MATCHING EXISTING VALID TRANSACTION ----MUST NOT PASS");
                    }
                }
            }
        }catch (Exception ex){
            System.out.println("EXCEPTION: "+ ex);
        }
        if(DATA.FAILED_TRANSACTIONS_TEST.size() == Blockchain.Test_Mine_Transactions.size()){
            System.out.println("FAILED MODIFY AND MATCH TRANSACTIONS");
            Blockchain.Test_Mine_Transactions.clear();
            System.out.println("NEW BLOCKCHAIN TRANSACTION SIZE: "+ Blockchain.Test_Mine_Transactions);
            for(Transaction transaction: DATA.TEST_ORIGINAL_TRANSACTIONS){
                Blockchain.Test_Mine_Transactions.add(transaction);
                System.out.println(Settings.WHITE + "ADDED TRANSACTION BACK TO BLOCKCHAIN: "+ transaction);
            }
            return true;
        }else {
            return false;
        }
    }

}
