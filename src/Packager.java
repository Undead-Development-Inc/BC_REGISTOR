import java.util.ArrayList;

public class Packager {

    public String Latest_Hash = "";
    public int BlocksinChain = 0;
    public ArrayList<String> Recent_IPs = new ArrayList<>();
    public String Latest_Transaction_HASH = "";

    public Packager (String Req){
        if(Req.matches("Status")){
            Latest_Hash = Blockchain.LastBlockHash();
            BlocksinChain += Blockchain.BlockChain.size();
            for(String IP: Blockchain.Net_IPs_Recent){
                Recent_IPs.add(IP);
            }
            Latest_Transaction_HASH = Blockchain.BlockChain.get(BlocksinChain).transactions.get(Blockchain.BlockChain.get(BlocksinChain).transactions.size() -1).transhash;
            return;
        }
        return;
    }
}
