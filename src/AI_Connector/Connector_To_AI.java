package AI_Connector;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

// we are connecting to port 5000
public class Connector_To_AI  {
    private static int port_To_AI = 5000;

    private static ServerSocket serverSocket;

    public static void main(String[] args){

        try {
            serverSocket = new ServerSocket(port_To_AI);
            System.out.println(serverSocket.toString());
        } catch (IOException e) {
            System.out.println( "Could not start server. " + e.getMessage());
        }

        run();
    }

    private static void run(){
        System.out.println("Server is running . . .");
        while(true) {
            try {
                System.out.println("waiting for new AI connection");
                Socket socket = serverSocket.accept();
                new AI_Player(socket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
