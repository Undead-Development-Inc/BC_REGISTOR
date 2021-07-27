import java.sql.*;

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
}
