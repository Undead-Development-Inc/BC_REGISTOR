import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.ECGenParameterSpec;
import java.security.*;
import java.sql.*;
import java.util.ArrayList;

public class DataBase {

    public static void Get_Masters(){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "SELECT * FROM "+ Settings.DBTABLE_REG;

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(Query);

            while (rs.next()){
                String IP = (String) rs.getString("IP");
                System.out.println("GOT IP FROM DB: "+ IP);
                if (!Networking.IPs.contains(IP)){
                    Networking.IPs.add(IP);
                }
            }
            st.close();

        }catch (Exception ex){
            System.out.println("Error in DB_GET_MASTERS: "+ ex);
        }
        return;
    }

    public static ArrayList<byte[]> Get_CKEY(){
        ArrayList<byte[]> T_CKEYS = new ArrayList<>();
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "SELECT * FROM "+ Settings.DBREG_CKEY;

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(Query);

            while (rs.next()){
                byte[] CKEY = rs.getBytes("CKEY");
                System.out.println("GOT CKEY FROM DB: "+ CKEY);
                if(!T_CKEYS.contains(CKEY)){
                    T_CKEYS.add(CKEY);
                }
            }
            st.close();

        }catch (Exception ex){
            System.out.println("Error in DB_GET_MASTERS: "+ ex);
        }
        return T_CKEYS;
    }

    public static Boolean FIND_DNSNODE(String SIP){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "SELECT * FROM "+ Settings.DBREG_REG;

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(Query);

            while (rs.next()){
                String IP = (String) rs.getString("IP");
                if (IP.matches(SIP)){
                    return true;
                }
            }
            st.close();

        }catch (Exception ex){
            System.out.println("Error in DB_GET_MASTERS: "+ ex);
        }
        return false;
    }

    public static Boolean FIND_Masters(String SIP){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "SELECT * FROM "+ Settings.DBTABLE_REG;

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(Query);

            while (rs.next()){
                String IP = (String) rs.getString("IP");
                if (IP.matches(SIP)){
                    return true;
                }
            }
            st.close();

        }catch (Exception ex){
            System.out.println("Error in DB_GET_MASTERS: "+ ex);
        }
        return false;
    }

    public static void ADD_Master(String IP, String Ver){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "insert into "+ Settings.DBTABLE_REG + " (IP, Ver)" + "values (?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(Query);

            preparedStatement.setString(1, IP);
            preparedStatement.setString(2, Ver);

            preparedStatement.execute();

            conn.close();

        }catch (Exception ex){
            System.out.println("Error in DB_ADD_Master: "+ ex);
        }
        return;
    }


    public static void ADD_AUTH_USER(String Hash){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "insert into "+ Settings.DBREG_AUTHUSERS + " (Hash)" + "values (?)";

            PreparedStatement preparedStatement = conn.prepareStatement(Query);

            preparedStatement.setObject(1, Hash);

            preparedStatement.execute();

            conn.close();

        }catch (Exception ex){
            System.out.println("Error in DB_ADDUSER: "+ ex);
        }
        return;
    }

    public static Boolean FIND_AUTHUSER(PublicKey publicKey, PrivateKey privateKey, String Password){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "SELECT * FROM "+ Settings.DBREG_AUTHUSERS;

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(Query);

            while (rs.next()){
                String Hash = (String) rs.getString("Hash");


                if(StringUtil.applySha512(publicKey+privateKey.toString()+Password).matches(Hash)){
                    System.out.println("Found User!!");
                    return true;
                }
            }
            st.close();

        }catch (Exception ex){
            System.out.println("Error in DB_FIND_USER: "+ ex);
        }
        return false;
    }

    public static void ADD_LOG(String Log){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "insert into "+ Settings.DBREG_LOGs + " (Log)" + "values (?)";

            PreparedStatement preparedStatement = conn.prepareStatement(Query);

            preparedStatement.setString(1, Log);


            preparedStatement.execute();

            conn.close();

        }catch (Exception ex){
            System.out.println("Error in DB_ADD_Master: "+ ex);
        }
        return;
    }


    public static Boolean FIND_RECORD_BLOCK(Block block){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "SELECT * FROM "+ Settings.DBTABLE_BC;

            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(Query);

            while (rs.next()){
                String Hash = (String) rs.getString("BlockHash");
                if (Hash.matches(block.getBlockHash())){
                    st.close();
                    return true;
                }
            }
            st.close();

        }catch (Exception ex){
            System.out.println("Error in DB_GET_MASTERS: "+ ex);
        }
        return false;
    }

    public static void ADD_BC_RECORD(Block Block){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "insert into "+ Settings.DBTABLE_BC + " (BlockID, BlockHash, PrevHash, Difficulty, MinedBy)" + "values (?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = conn.prepareStatement(Query);

            preparedStatement.setInt(1, Blockchain.BlockChain.lastIndexOf(Block));
            preparedStatement.setString(2, Block.getBlockHash());
            preparedStatement.setString(3, Block.getPreviousHash());
            preparedStatement.setInt(4, Block.diff);
            preparedStatement.setString(5, Block.miner.toString());

            preparedStatement.execute();

            conn.close();

        }catch (Exception ex){
            System.out.println("Error in DB_ADD_Master: "+ ex);
        }
        return;
    }

    public static void ADD_REGISTOR(){
        try{
            Class.forName(Settings.DBDRIVER);
            Connection conn = DriverManager.getConnection(Settings.DBURL, Settings.DBUSER, Settings.DBPASS);

            String Query = "insert into "+ Settings.DBREG_REG + " (IP)" + "values (?)";

            PreparedStatement preparedStatement = conn.prepareStatement(Query);

            preparedStatement.setString(1, Networking.My_IP());


            preparedStatement.execute();

            conn.close();

        }catch (Exception ex){
            System.out.println("Error in DB_ADD_Master: "+ ex);
        }
        return;
    }
}
