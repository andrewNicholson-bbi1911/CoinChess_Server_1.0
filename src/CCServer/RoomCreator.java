package CCServer;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

public class RoomCreator extends Thread{

    public LinkedList<CoinChessPlayer> searchingPlayers;

    public static Command commandList = new Command();

    public RoomCreator(){
        searchingPlayers = new LinkedList<CoinChessPlayer>();
        run();
    }

    public RoomCreator(LinkedList<CoinChessPlayer> olds){
        searchingPlayers = new LinkedList<CoinChessPlayer>(olds);
        run();
    }

    public void AddPlayer(CoinChessPlayer player){
        searchingPlayers.add(player);
    }

    public void RemovePlayer(CoinChessPlayer player){
        searchingPlayers.remove(player);
    }

    public void run(){
        System.out.println("RoomCreator: new room creator thread");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                LinkedList<CoinChessPlayer> oldSearchingPlayers = new LinkedList<>(searchingPlayers);

                searchingPlayers = new LinkedList<CoinChessPlayer>();
                oldSearchingPlayers.sort(new Comparator<CoinChessPlayer>() {
                    @Override
                    public int compare(CoinChessPlayer o1, CoinChessPlayer o2) {
                        return o1.getID().compareTo(o2.getID());
                    }
                });
                System.out.println("nn");
                for(int i = 0; i<oldSearchingPlayers.size(); i+=2){
                    oldSearchingPlayers.get(i).SetInGame();
                }
            }
        },30340);
    }

}