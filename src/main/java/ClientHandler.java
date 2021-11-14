import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    

    
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private String clientUsername;

    public ClientHandler(Socket socket) {

        try {

            this.socket = socket;
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.output= new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = input.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER: " + clientUsername + " has entered the chat!");

        } catch (IOException e) {

            closeEverything(socket, input, output);
        }
    }

    @Override
    public void run() {

        String messageFromClient;
       
        while (socket.isConnected()) {

            try {
               
                messageFromClient = input.readLine();
                broadcastMessage(messageFromClient);
                
            } catch (IOException e) {
                
                closeEverything(socket, input, output);
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {

        for (ClientHandler clientHandler : clientHandlers) {
            
            try {
                
                if (!clientHandler.clientUsername.equals(clientUsername)) {

                    clientHandler.output.write(messageToSend);
                    clientHandler.output.newLine();
                    clientHandler.output.flush();
                }
            } catch (IOException e) {

                closeEverything(socket, input, output);
            }
        }
    }

    public void removeClientHandler() {

        clientHandlers.remove(this);
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        
        removeClientHandler();
        
        try {

            if (bufferedReader != null) {

                bufferedReader.close();
            }
            if (bufferedWriter != null) {

                bufferedWriter.close();
            }
            if (socket != null) {

                socket.close();
            }
            
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
