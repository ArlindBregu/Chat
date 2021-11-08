import java.net.Socket;
import java.util.List;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    
    BufferedReader inputDalServer;
    BufferedWriter outputVersoServer;
    Socket socket;
    String username;
    List<String> clientConnessi;

    public ChatClient(Socket socket, String username){

        try {
            this.socket = socket;
            this.username = username;
            this.inputDalServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.outputVersoServer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (Exception e) {
            
            closeEverything(socket, inputDalServer, outputVersoServer);
        }
    }

    public void closeEverything(Socket socket, BufferedReader inputDalServer, BufferedWriter outputVersoServer){

        try {
            
            if(inputDalServer != null){

                inputDalServer.close();
            }
            if(outputVersoServer != null){

                outputVersoServer.close();
            }
            if(socket != null){

                socket.close();
            }

        } catch (IOException e) {
            
            e.printStackTrace();
        }

    }

}
