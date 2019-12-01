package CCServer;

import java.util.ArrayList;
import java.util.Arrays;

public class GameBoardForServer{
    public ArrayList<HeroClass> heroLibrary;

    public ArrayList<ServerCell> gameBoard = new ArrayList<>();
    public ArrayList<HeroClass> radiantHeroes = new ArrayList<HeroClass>();
    public ArrayList<HeroClass> dareHeroes = new ArrayList<HeroClass>();

    public void InitializeGameBoard(ArrayList<HeroClass> heroLibray){
        this.heroLibrary = heroLibray;
        for(byte i = 1; i<=11;i++){
            for(byte j = 1; j<=11-Math.abs(6-i);j++){
                gameBoard.add(new ServerCell().ServerCell(i,j,this));
            }
        }
        for (ServerCell cell : gameBoard) {
            cell.setNeighbours();
        }
    }

    void AddHero(HeroClass newHero, boolean isRad){
        if (isRad) {
            //CellOnBoard(newHero.)
            radiantHeroes.add (newHero);
        } else {
            dareHeroes.add (newHero);
        }
    }

    void action(String[] values) throws Exception{
        Exception e = new Exception();
        byte[] heroCoords = Command.getCoordinates(values[0]);
        byte actionID= Byte.parseByte(values[1]);
        ServerCell activeCell = null;
        activeCell = cellOnBoardByCoords(heroCoords);
        if(activeCell != null){
            HeroClass hero = activeCell.heroOnCell;
            ServerCell targetCell;
            byte[] targetCoords;
            switch (actionID){
                case 1://MOVE
                    activeCell.heroOnCell.changePosition(Command.getCoordinates(values[2]), false);
                    break;
                case 2://ATTACK
                    targetCoords = Command.getCoordinates(values[2]);
                    targetCell = cellOnBoardByCoords(targetCoords);
                    if(targetCell!=null){
                        //the cell must be able for attack so I check it here;
                        byte centerTargetDistance = (byte) ((byte)6 - targetCell.position[1]);
                        byte centerActiveDistance = (byte) ((byte)6 - activeCell.position[1]);
                        byte xDistance = (byte) (Math.abs(targetCell.position[0]-activeCell.position[0]));
                        if(centerActiveDistance*centerTargetDistance<0){
                            xDistance+=(centerActiveDistance>=centerTargetDistance)?centerTargetDistance:centerTargetDistance;
                        }
                        if(xDistance<=hero.attackRange){//if the cell is able for attack;
                            targetCell.setAbleToBeAttacked(hero.damage, hero.attackSplashRadius==1);
                        }else{//the target cell is out of attack range;
                            throw e;
                        }

                    }
                    break;
                case 3://SHIFT COIN
                    targetCoords = Command.getCoordinates(values[2]);
                    targetCell = cellOnBoardByCoords(targetCoords);
                    if (targetCell.heroOnCell != null & activeCell.heroOnCell!=null) {
                        if(String.valueOf(activeCell.heroOnCell.code).length()>1
                        & String.valueOf(targetCell.heroOnCell.code).length()<3){
                            int coinSide = activeCell.heroOnCell.code % 10;
                            activeCell.heroOnCell.code /= 10;
                            targetCell.heroOnCell.code *= 10;
                            targetCell.heroOnCell.code+=coinSide;
                            activeCell.heroOnCell.reinitialization();
                            targetCell.heroOnCell.reinitialization();
                        }else{
                            throw e;
                        }

                    }else{
                        throw e;
                    }
                    break;
                case 4://CAST
                        activeCell.heroOnCell.cast(Command.getCoordinates(values[2]));
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + actionID);
            }
        }else throw e;
    }

    /// <summary>
    /// Cells are on board by coords.
    /// </summary>
    /// <returns>The ServerCell that is  on board by getting coordinates.</returns>
    /// <param name="circle">Circle.</param>
    /// <param name="num">Number.</param>
    ///
    public ServerCell cellOnBoardByCoords(byte[] position) throws Exception {
        for (ServerCell cell:
             gameBoard) {
            if(Arrays.equals(cell.position, position)){
                return cell;
            }
        }
        throw new Exception();
    }

    /// <summary>
    /// Cells are on board by heroClass.
    /// </summary>
    /// <returns>The ServerCell that is  on board by hero that is staying on the cell.</returns>
    /// <param name="circle">Circle.</param>
    /// <param name="num">Number.</param>
    ///
    public ServerCell cellOnBoardByHero(HeroClass heroClass) throws Exception {
        for (ServerCell cell:
                gameBoard) {
            if(cell.heroOnCell.equals(heroClass)){
                return cell;
            }
        }
        throw new Exception();
    }

    /// <summary>
    /// check if the hero or summon is able to come on the target cell
    /// </summary>
    /// <returns>true if smb is able to stay on the target cell, falls if not</returns>
    /// <param name="circle">Circle.</param>
    /// <param name="num">Number.</param>
    ///
    public boolean checkMoveAble(ServerCell activeCell, ServerCell targetCell) throws Exception {
        activeCell.setAbleToMove(activeCell.heroOnCell.moveRange);
        wait(50);
        return targetCell.ableToMove;
    }

    public void clearAll(){
        for (ServerCell cell:gameBoard)
            cell.removeAll();
    }


}



