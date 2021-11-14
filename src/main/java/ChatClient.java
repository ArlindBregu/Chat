import java.net.Socket;
import java.util.List;

import java.io.*;
import java.util.Scanner;

public class ChatClient {
    
    BufferedReader inputDalServer;
    BufferedWriter outputVersoServer;
    Socket socket;
    String username;
    List<String> clientConnessi;
    private Scanner scanner1;
    private static Scanner scanner2;

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

    public void sendMessage(){

        try {
            
            outputVersoServer.write(username);
            outputVersoServer.newLine();
            outputVersoServer.flush();
            scanner1 = new Scanner(System.in);

            while(socket.isConnected()){

                String message = scanner1.nextLine();
                outputVersoServer.write(username + ": " + message);
                outputVersoServer.newLine();
                outputVersoServer.flush();
            }

        } catch (Exception e) {
            
            closeEverything(socket, inputDalServer, outputVersoServer);
        }
    }

    public void listenForMessage() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                String msgFromGroupChat;

                while (socket.isConnected()) {

                    try {

                        msgFromGroupChat = inputDalServer.readLine();
                        System.out.println(msgFromGroupChat);

                    } catch (IOException e) {

                        closeEverything(socket, inputDalServer, outputVersoServer);
                    }
                }
            }
        }).start();
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

    public static void main(String[] args) throws IOException {

        scanner2 = new Scanner(System.in);
        System.out.print("Enter your username for the group chat: ");
        String username = scanner2.nextLine();
        Socket socket = new Socket("localhost", 1234);
        ChatClient client = new ChatClient(socket, username);
        client.listenForMessage();
        client.sendMessage();
    }

}
