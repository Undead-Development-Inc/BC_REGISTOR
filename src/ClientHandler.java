import com.mysql.cj.log.Log;

import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler implements Runnable{
    public String Client_Address;
    private Socket client;
    public ObjectOutputStream objectOutputStream;
    public ObjectInputStream objectInputStream;

    public ClientHandler(Socket clientSocket) {
        try {
            this.client = clientSocket;
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());
            objectInputStream = new ObjectInputStream(client.getInputStream());
            System.out.println("Creating Streams for Client: "+ this.client);
            run();
        }catch (Exception ex){
            System.out.println(ex);
            Networking.Logs.add(ex.getMessage());
        }
    }

    @Override
    public void run() {
        try{
            System.out.println("Running Client Methods");
            Client_Address = (String) objectInputStream.readObject();
            System.out.println("GOT ADDRESS: "+ Client_Address);
            objectOutputStream.writeObject("WAIT");
            client.setKeepAlive(true);
        }catch (Exception ex){
            System.out.println(ex);
            Networking.Logs.add(Networking.My_IP() + ex);
        }
    }
}
