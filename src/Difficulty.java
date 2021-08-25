import java.util.Random;

public class Difficulty {

    int Blockid_Curr = 0;

    public int difficulty(){
        int Difficulty = 0;
        if(Blockchain.BlockChain.size() <= 10000 & Blockchain.BlockChain.size() >= 1){
            Difficulty += 1;
        }
        if(Blockchain.BlockChain.size() <= 100000 & Blockchain.BlockChain.size() >= 10000){
            Difficulty += Blockchain.BlockChain.size() ^ 15 / 36;
        }
        if(Blockchain.BlockChain.size() >= 100000 & Blockchain.BlockChain.size() <= 200000){
            Difficulty += Blockchain.BlockChain.size() ^ 10 / 25;
        }
        if(Blockchain.BlockChain.size() >= 200000 & Blockchain.BlockChain.size() <= 300000){
            Difficulty += Blockchain.BlockChain.size() ^ 8 / 36;
        }
        if(Blockchain.BlockChain.size() >= 300000 & Blockchain.BlockChain.size() <= 400000){
            Difficulty += Blockchain.BlockChain.size() ^ 8 / 40;
        }
        if(Blockchain.BlockChain.size() >= 400000 & Blockchain.BlockChain.size() <= 500000){
            Difficulty += Blockchain.BlockChain.size() ^ 8 / 40;
        }
        if(Blockchain.BlockChain.size() >= 500000 & Blockchain.BlockChain.size() <= 600000){
            Difficulty += Blockchain.BlockChain.size() ^ 8 / 40;
        }
        if(Blockchain.BlockChain.size() >= 600000 & Blockchain.BlockChain.size() <= 700000){
            Difficulty += Blockchain.BlockChain.size() ^ 8 / 40;
        }
        if(Blockchain.BlockChain.size() >= 700000 & Blockchain.BlockChain.size() <= 800000){
            Difficulty += Blockchain.BlockChain.size() ^ 8 / 40;
        }
        if(Blockchain.BlockChain.size() >= 800000 & Blockchain.BlockChain.size() <= 900000){
            Difficulty += Blockchain.BlockChain.size() *3 /9;
        }
        if(Blockchain.BlockChain.size() >= 900000 & Blockchain.BlockChain.size() <= 1000000){
            Difficulty += Blockchain.BlockChain.size() *3 /9;
        }
        if(Blockchain.BlockChain.size() >= 1000000 & Blockchain.BlockChain.size() <= 1100000){
            Difficulty += Blockchain.BlockChain.size() *3 /24560;
        }
        if(Blockchain.BlockChain.size() >= 1100000 & Blockchain.BlockChain.size() <= 2100000){
            Difficulty += Blockchain.BlockChain.size() *3 /24560;
        }
        if(Blockchain.BlockChain.size() >= 2100000 & Blockchain.BlockChain.size() <= 3100000){
            Difficulty += Blockchain.BlockChain.size() *3 /7;
        }
        if(Blockchain.BlockChain.size() >= 3100000 & Blockchain.BlockChain.size() <= 4100000){
            Difficulty += Blockchain.BlockChain.size() *3 /8;
        }
        if(Blockchain.BlockChain.size() >= 4100000 & Blockchain.BlockChain.size() <= 5100000){
            Difficulty += Blockchain.BlockChain.size() *3 /9;
        }
        if(Blockchain.BlockChain.size() >= 5100000 & Blockchain.BlockChain.size() <= 10100000){
            Difficulty += Blockchain.BlockChain.size() *3 /10;
        }
        if(Blockchain.BlockChain.size() >= 10100000 & Blockchain.BlockChain.size() <= 30100000){
            Difficulty += Blockchain.BlockChain.size() *3 /11;
        }
        if(Blockchain.BlockChain.size() >= 30100000 & Blockchain.BlockChain.size() <= 100100000){
            Difficulty += Blockchain.BlockChain.size() *2 /19;
        }
        return Difficulty;

    }

    public void Blockid_current(){
        Blockid_Curr = Blockchain.BlockChain.size();
        return;
    }
}
