import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ErrorMgr {

    public void errormgr(int error) throws Exception {

        if(error == 1){
            throw new Exception("ERROR NO-BLOCK IN LOCAL-CHAIN");
        }
        if(error == 2){
            throw new Exception("ERROR NO-BLOCK IN DB");
        }
        if(error == 3){
            throw new Exception("ERROR BLOCK MISSING IN LOCAL DB & CLOUD");
        }
        if(error == 4){
            throw new Exception("Transaction missing from local DB");
        }
        if(error == 5){
            throw new Exception("Transaction Not in Cloud");
        }
        if(error == 6){
            throw new Exception("Transaction NOT-LINKED");
        }
        if(error == 7){
            throw new Exception("Transaction NOT IN BLOCK---MIS/SYNC");
        }
        if(error == 8){
            throw new Exception("False Data--Transaction");
        }
        if(error == 9){
            throw new Exception("False Data --Block");
        }
        if(error == 10){
            throw new Exception("Block ----MIS SYNC");
        }
        if(error == 11){
            throw new Exception("BlockChain is Empty");
        }
        if(error == 12){
            throw new Exception("G-BLOCK NOT ADDED ERROR!!!!");
        }

    }
}
