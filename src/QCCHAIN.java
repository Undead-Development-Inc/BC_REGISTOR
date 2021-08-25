public class QCCHAIN {
    public static String Ver = StringUtil.HASH();

    public static void main(String[] args) throws Exception {
        StringUtil.HASH();

        new TEST_SEQ().Test_Chain();
        Thread thread = new Thread(Networking::APINETWORK);
        System.out.println("GENESES BLOCK HAS IS: "+ Blockchain.BlockChain.get(0).getBlockHash());
        thread.start();



//        new menu().Menu();

    }
}
