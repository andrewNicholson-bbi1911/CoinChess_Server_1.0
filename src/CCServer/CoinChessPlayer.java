package CCServer;

import parcles.PlayerLogIn;

import java.io.*;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;

public class CoinChessPlayer extends Thread{

    public String nickName;
    public boolean messageExists = false;
    public String gameMessage;

    private Socket playerSocket;
    private InputStream ips;
    private LinkedList<CoinChessPlayer> clients;

    private BufferedReader fromPlayer;
    private PrintWriter toPlayer;

    private int id, lvl;

    private boolean inGame=false;
    private boolean inSearch=false;

    private ArrayList<HeroClass> heroLibrary;


    CoinChessPlayer(Socket player , LinkedList<CoinChessPlayer> clients) {
        playerSocket = player;
        this.clients = clients;
    }

    public void run() {
        heroLibrary = CoinChessServer.getHeroLibrary();
        //initialization
        try {
            ips = playerSocket.getInputStream();

            toPlayer = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            fromPlayer = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));

            sendToClient("Go");

            String gotMessage = fromPlayer.readLine();
            if( gotMessage.equals("C")) {
                clients.add(this);
                System.out.println("clients:" + clients.size());
                String[] info = fromPlayer.readLine().split(" m:p ");
                if(info.length==2){
                boolean needHash = info[1].startsWith(" /needHash/");

                String idAndLvl = PlayerLogIn.GetIPandLVL(info[0], info[1],needHash);
                    System.out.println(idAndLvl);
                    if(idAndLvl.equals("ER"))
                    {
                        sendToClient("ER");
                        leave();
                    }
                id = Integer.parseInt(idAndLvl.split(":")[0]);
                lvl = Integer.parseInt(idAndLvl.split(":")[1]);
                nickName = idAndLvl.split(":")[2];
                    sendToClient(idAndLvl);
                }else {
                    sendToClient("error");
                }

            }else {
                leave();
            }

            // permanent talk
            while (true){
                String _gotMessage = fromPlayer.readLine();
                messageExists= false;
                if(_gotMessage.equals("E"))
                    leave();
                else if(_gotMessage.equals("S")){
                    if(inGame | inSearch) continue;
                }
                else{
                    setGotMessage(_gotMessage);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            leave();
        }
    }

    private void setGotMessage(String message){
        messageExists = true;
        gameMessage = message;
    }



    public Integer getID(){
        return id;
    }

    public void sendToClient( String message ){
        try {
            message = URLEncoder.encode(message, "UTF-8");
            toPlayer.println(message);
            toPlayer.flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void SetInGame(){


    }

    public void leave(){
        clients.remove(clients.indexOf(this));
        System.out.println("clients:" + clients.size());
        try {
            fromPlayer.close();
            toPlayer.close();
            ips.close();
            playerSocket.close();
            this.stop();
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            interrupt();
        }
    }

}
