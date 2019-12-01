package CCServer;

import spells.EffectInterface;
import spells.SpellInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class CoChRoom extends Thread {

        private CoinChessPlayer radPlayer, darePlayer;
        private GameBoardForServer gameBoardForServer;
        private ArrayList<HeroClass> heroLibrary;
        private ArrayList<byte[]> spellLibrary;
        private String winner = "";
        private String reason = "";

        private byte ableActions = 3;

    private int[] coins = new int[]{16,16}; //0-rad; 1-dare

        private boolean radTurn = true;

        public CoChRoom(CoinChessPlayer RadPlayer, CoinChessPlayer DarePlayer){
                this.radPlayer = RadPlayer;
                this.darePlayer = DarePlayer;
                begin();
        }

        private void begin(){
            //start game
            radPlayer.sendToClient(Command.strartGame+":"+Command.radiant);
            darePlayer.sendToClient(Command.strartGame+":"+Command.dare);
            start();
        }


        public void run(){

            gameBoardForServer = new GameBoardForServer();
            gameBoardForServer.InitializeGameBoard(heroLibrary);
            heroLibrary = CoinChessServer.getHeroLibrary();

            draftProcess();

            mainGameProcess();

            SetResult();

            interrupt();
        }

        private void draftProcess(){
            boolean[] playerMissHisDraftingTurn = {false, false};
            //draft
            boolean isDrafting = true;
            while(isDrafting){try {
                sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
                if ((gameBoardForServer.radiantHeroes.size()==7 | coins[0]==0  | playerMissHisDraftingTurn[0]) &
                        (gameBoardForServer.dareHeroes.size()==7 | coins[1]==0 |  playerMissHisDraftingTurn[1]) ){
                    isDrafting = false;
                }else{
                    if(radTurn & !playerMissHisDraftingTurn[0] & gameBoardForServer.radiantHeroes.size()<7 & coins[0]!=0){//if it is RADIANT DRAFT TURN
                        if(radPlayer.messageExists){
                            if(!radPlayer.gameMessage.equals(Command.missTurn)){// if RADIANT player POSE a HERO
                                //forma that we take is (_gotMes): "P:id:coords"  coords has a prefab "1line;2cell"
                                String _gotMes = radPlayer.gameMessage;
                                if(_gotMes.startsWith(Command.pose)){//if it's a pose process
                                    if(_gotMes.split(":")[1].length()<=coins[0]) {//if it's enough coins
                                        try {
                                            byte[] coords = Command.getCoordinates(_gotMes.split(":")[2]);

                                            for (ServerCell cell:
                                                    gameBoardForServer.gameBoard) {
                                                if(Arrays.equals(cell.position, coords)){
                                                    if(Objects.equals(cell.heroOnCell, "null")) {
                                                        HeroClass __hero = new HeroClass();
                                                        for (HeroClass _hero:
                                                                heroLibrary) {
                                                            if(_hero.code==Integer.getInteger(_gotMes.split(":")[1])){
                                                                __hero=new HeroClass(_hero, gameBoardForServer);
                                                                __hero.rad=true;
                                                                cell.poseHero(__hero);
                                                                gameBoardForServer.radiantHeroes.add(__hero);
                                                                coins[0]-=_gotMes.split(":")[1].length();
                                                                break;
                                                            }
                                                        }
                                                        if(__hero.code==0){
                                                            radPlayer.sendToClient(Command.error+":"+Command.wrongID);
                                                            break;
                                                        }
                                                        gameBoardForServer.AddHero(__hero, true);

                                                        radPlayer.sendToClient(Command.radiant+":"+_gotMes);
                                                        darePlayer.sendToClient(Command.radiant+":"+_gotMes);
                                                        radTurn=false;

                                                    }else{
                                                        radPlayer.sendToClient(Command.error+":"+Command.busyCell);
                                                    }
                                                    break;
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            radPlayer.sendToClient(Command.error+":"+Command.wrongCoords);
                                            continue;
                                        }

                                    }else{
                                        radPlayer.sendToClient(Command.error+":"+Command.notEnoughCoins);
                                        continue;
                                    }
                                }else{
                                    radPlayer.sendToClient(Command.error+":"+Command.impossibleAction);
                                    continue;
                                }
                                radTurn = false;
                            }else{// if RADIANT player MISS TURN
                                playerMissHisDraftingTurn[0] = true;
                                radTurn = false;
                            }
                        }
                    }
                    else if(!radTurn & !playerMissHisDraftingTurn[1] & gameBoardForServer.dareHeroes.size()<7 & coins[0]!=0) {// if it is DARE DRAFTING TURN
                        if(darePlayer.messageExists){
                            if(!darePlayer.gameMessage.equals(Command.missTurn)){// if DARE player POSE a HERO
                                //forma that we take is (_gotMes): "P:id:coords"  coords has a prefab "1line;2cell"
                                String _gotMes = darePlayer.gameMessage;
                                if(_gotMes.startsWith(Command.pose)){//if it's a pose process
                                    if(_gotMes.split(":")[1].length()<=coins[0]) {//if it's enough coins
                                        try {
                                            byte[] coords = Command.getCoordinates(_gotMes.split(":")[2]);

                                            for (ServerCell cell:
                                                    gameBoardForServer.gameBoard) {
                                                if(Arrays.equals(cell.position, coords)){
                                                    if(Objects.equals(cell.heroOnCell, "null")) {
                                                        HeroClass __hero = new HeroClass();
                                                        for (HeroClass _hero:
                                                                heroLibrary) {
                                                            if(_hero.code==Byte.parseByte(_gotMes.split(":")[1])){
                                                                __hero=new HeroClass(_hero, gameBoardForServer);
                                                                cell.poseHero(__hero);
                                                                __hero.rad=false;
                                                                gameBoardForServer.dareHeroes.add(__hero);
                                                                coins[1]-=_gotMes.split(":")[1].length();
                                                                break;
                                                            }
                                                        }
                                                        if(__hero.code==0){
                                                            radPlayer.sendToClient(Command.error+":"+Command.wrongID);
                                                            break;
                                                        }
                                                        gameBoardForServer.AddHero(__hero, false);

                                                        radPlayer.sendToClient(Command.dare+":"+_gotMes);
                                                        darePlayer.sendToClient(Command.dare+":"+_gotMes);
                                                        radTurn=true;

                                                    }else{
                                                        darePlayer.sendToClient(Command.error+":"+Command.busyCell);
                                                    }
                                                    break;
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            darePlayer.sendToClient(Command.error+":"+Command.wrongCoords);
                                            continue;
                                        }

                                    }else{
                                        darePlayer.sendToClient(Command.error+":"+Command.notEnoughCoins);
                                        continue;
                                    }
                                }else{
                                    darePlayer.sendToClient(Command.error+":"+Command.impossibleAction);
                                    continue;
                                }
                                radTurn = true;
                            }else{// if DARE player MISS TURN
                                playerMissHisDraftingTurn[1] = true;
                                radTurn = true;
                            }
                        }
                    }
                }
            }
            radTurn = true;
            boolean isGamePlay = true;

        }

        private void mainGameProcess() {
            //we get message from client: CoordsOfHero:action:AimCoords[if exists]
            while (true) {
                if (gameBoardForServer.radiantHeroes.size() == 0 | winner.equals("D")) {//dare victory
                    winner = "D";
                    break;
                } else if (gameBoardForServer.dareHeroes.size() == 0 | winner.equals("R")) {//rad victory
                    winner = "R";
                    break;
                }
                try {
                    sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //the process of main game

                if (radTurn) {//radiant turn
                    for(byte i = 0; i<ableActions;i++){
                        while (!radPlayer.messageExists) {
                            try {
                                sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //now we exactly got message
                        String _message = radPlayer.gameMessage;
                        String[] values = _message.split(":");
                        if(_message.equals(Command.giveUpReason)) {
                            winner = "D";
                            break;
                        }else if(_message.equals(Command.missTurn)){
                            i = ableActions;
                            break;
                        }else{
                            try{
                                gameBoardForServer.action(values);

                            }catch(Exception e){
                                i--;
                                radPlayer.sendToClient(Command.error+":"+Command.wrongCoords);
                            }

                        }

                    }
                    endOfTurn();
                } else if (!radTurn) {//if its a dare turn
                    for(byte i = 0; i<ableActions;i++){
                        while (!darePlayer.messageExists) {
                            try {
                                sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        //now we exactly got message
                        String _message = darePlayer.gameMessage;
                        String[] values = _message.split(":");
                        if(_message.equals(Command.giveUpReason)) {
                            winner = "R";
                            break;
                        }else if(_message.equals(Command.missTurn)){
                            i = ableActions;
                            break;
                        }else{
                            try{
                                gameBoardForServer.action(values);
                            }catch(Exception e){
                                i--;
                                darePlayer.sendToClient(Command.error+":"+Command.wrongCoords);
                            }

                        }

                    }
                    endOfTurn();
                }
            }
        }

        private void endOfTurn(){
            radTurn = !radTurn;
            //effect changing
            for (HeroClass hero:
                  gameBoardForServer.radiantHeroes) {
                for (EffectInterface effect:
                     hero.effects) {
                    effect.onTurnChange();
                }
            }
            for (HeroClass hero:
                    gameBoardForServer.dareHeroes) {
                for (EffectInterface effect:
                        hero.effects) {
                    effect.onTurnChange();
                }
            }

            //spell CD changing
            for (HeroClass hero:
                    gameBoardForServer.radiantHeroes) {
                    hero.spell.onTurnChange();
            }
            for (HeroClass hero:
                    gameBoardForServer.dareHeroes) {
                    hero.spell.onTurnChange();
                }
        }


        public void giveUp(){
        }

        private void SetResult(){

            switch (winner){
                case "" :
                    radPlayer.sendToClient(Command.noConnectedReason);
                    darePlayer.sendToClient(Command.noConnectedReason);
                case "R" :
                    radPlayer.sendToClient(Command.endGame+":"+Command.win);
                    radPlayer.sendToClient(Command.endGame+":"+Command.lose);
                case "D" :
                    radPlayer.sendToClient(Command.endGame+":"+Command.lose);
                    radPlayer.sendToClient(Command.endGame+":"+Command.win);
            }

        }


}

