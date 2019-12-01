package CCServer;

import spells.EffectInterface;
import spells.SpellInterface;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;

public class CoinChessServer {

    private static ArrayList<HeroClass> heroLibrary;
    static ArrayList<SpellInterface> spellLibrary;

    private static GameBoardForServer gameBoardForServer = new GameBoardForServer();

    private static ServerSocket serverSocket;

    private static LinkedList<CoinChessPlayer> clientList = new LinkedList<>();


    public static void main(String[] args){
        System.out.println("gooo");
        HeroesAll heroesAll = new HeroesAll();
        heroesAll.getHeroes();
        while(!heroesAll.libraryDownloaded);
        heroLibrary = heroesAll.getHeroesBase();
        gameBoardForServer.InitializeGameBoard(heroLibrary);

        try {
            serverSocket = new ServerSocket(1001);
            System.out.println(serverSocket.toString());
        } catch (IOException e) {
            System.out.println( "Could not start server. " + e.getMessage());
        }
        System.out.println("Server is running . . .");

        run();
    }

    private static void run() {
        new RoomCreator();
        while(true) {
            try {
                Socket socket = serverSocket.accept();
                new CoinChessPlayer(socket, clientList).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static ArrayList<HeroClass> getHeroLibrary(){
        return heroLibrary;
    }
}
