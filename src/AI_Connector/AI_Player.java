package AI_Connector;


import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.util.Scanner;

public class AI_Player extends Thread{

    private Socket playerSocket;
    private InputStream ips;

    private BufferedReader fromAiPlayer;
    private PrintWriter toAiPlayer;


    Scanner scan = new Scanner(System.in);


    AI_Player(Socket player) {
        System.out.println("some AI has connected");
        playerSocket = player;
        run();
    }

    public void run() {
        String Connected_str = "Connected";
        //initialization
        try {
            ips = playerSocket.getInputStream();

            toAiPlayer = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            fromAiPlayer = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            sendToClient("Go");
            String firstMessage = new String(MessageFromClient());
            if(Connected_str == firstMessage.intern())
                while (true) {
                    System.out.print("Server(you)> ");
                    String message = scan.nextLine();
                    sendToClient(message);
                    System.out.println("AI> " + MessageFromClient());
                }

        } catch (IOException e) {
            e.printStackTrace();
            leave();
        }
    }

    private String MessageFromClient(){
        try{
            int size;
            while(true) {
                try {
                    size = fromAiPlayer.read();
                    break;
                }catch(Exception ex){

                }
            }
            size = fromAiPlayer.read();
            //System.out.println("size of next message is: " + size);
            byte[] clientData = new byte[size];
            for(int i = 0; i< size; i++){
                clientData[i] = (byte) fromAiPlayer.read();
            }
            String AI_message = new String(clientData);
            return AI_message;

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public void sendToClient( String message ){
        try {
            //message = URLEncoder.encode(message, "UTF-8");
            toAiPlayer.print(message.getBytes().length);
            toAiPlayer.flush();
            toAiPlayer.println(message);
            toAiPlayer.flush();
            //System.out.println("sent to AI client: " + message + " | size = " + message.getBytes().length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void leave(){
        try {
            fromAiPlayer.close();
            toAiPlayer.close();
            ips.close();
            playerSocket.close();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            interrupt();
        }
    }

}

