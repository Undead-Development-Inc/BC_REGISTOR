import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client_Mgr implements Runnable{

    private ClientHandler client0;
    private ClientHandler client01;

    Thread Client0_Output = new Thread(this::Output_Client0);
    Thread Client1_Output = new Thread(this::Output_Client1);


    public Client_Mgr(ClientHandler client1, ClientHandler client2){
        client0 = client1;
        client01 = client2;
    }

    @Override
    public void run() {
        Client0_Output.start();
        Client1_Output.start();
        while (true){

        }
    }

    public void Output_Client0(){
        try {
            while (true) {
                String Input = Get_Client0_Input();

                if (!(Input == null)) {
                    client01.objectOutputStream.writeObject(Input);
                }
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public void Output_Client1(){
        try {
            while (true) {
                String Input = Get_Client1_Input();

                if (!(Input == null)) {
                    client0.objectOutputStream.writeObject(Input);
                }
            }
        }catch (Exception ex){
            System.out.println(ex);
        }
    }

    public String Get_Client0_Input(){
        try {
            String Input = (String) client0.objectInputStream.readObject();
            return Input;
        }catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }

    public String Get_Client1_Input(){
        try {
            String Input = (String) client01.objectInputStream.readObject();
            return Input;
        }catch (Exception ex){
            System.out.println(ex);
        }
        return null;
    }
}
