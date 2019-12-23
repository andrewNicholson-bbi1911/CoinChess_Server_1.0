package CCServer;

import java.util.ArrayList;

public class ServerCell{
    public ArrayList<byte[]> neighbourCells = new ArrayList<>();
    public byte[] position = new byte[2];
    public boolean active = false;
    public boolean ableToMove = false;
    public boolean ableToCast= false;

    public HeroClass heroOnCell = null;

    private GameBoardForServer gameBoardForServer;

    public ServerCell ServerCell(byte raw, byte column, GameBoardForServer gameBoardForServer){
        position [0] = raw;
        position [1] = column;
        this.gameBoardForServer = gameBoardForServer;
        return this;
    }

    public void poseHero(HeroClass hero){
        heroOnCell = hero;
    }

    public void setNeighbours(){

        if (position[0] < 6) //for lines 1-5;
        {
            if (position[0] == 1)
            {
                if (position[1] == 1)
                {
                    neighbourCells.add(new byte[] { position[0],(byte)( position[1] + 1 )});
                }
                else if (position[1] - 5 == position[0])
                {
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] - 1 )});
                }
                else
                {
                    neighbourCells.add(new byte[] { position[0], (byte)(position[1] - 1) });
                    neighbourCells.add(new byte[] { position[0], (byte)(position[1] + 1 )});
                }
                neighbourCells.add(new byte[] { (byte)(position[0]+1), position[1]});
                neighbourCells.add(new byte[] { (byte)(position[0]+1),(byte) (position[1] + 1) });

            }
            else
            {

                if (position[1] == 1)
                {
                    neighbourCells.add(new byte[] {(byte)( position[0]-1), position[1]});
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] + 1) });
                }else if (position[1]-5==position[0]){
                    neighbourCells.add(new byte[] {(byte)( position[0] - 1), (byte)(position[1] - 1) });
                    neighbourCells.add(new byte[] { position[0], (byte)(position[1] - 1) });
                }
                else{
                    neighbourCells.add(new byte[] { (byte)(position[0] - 1),(byte)( position[1] - 1 )});
                    neighbourCells.add(new byte[] { (byte) (position[0] - 1), position[1] });
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] - 1) });
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] + 1) });
                }
                neighbourCells.add(new byte[] { (byte)(position[0] + 1), position[1] });
                neighbourCells.add(new byte[] { (byte)(position[0] + 1),(byte) (position[1] + 1) });
            }
        }
        else if (position[0]>6)//for lines 7-11;
        {
            if (position[0] == 11)//for last line only
            {
                neighbourCells.add(new byte[] { (byte)(position[0] - 1), position[1] });
                neighbourCells.add(new byte[] { (byte)(position[0] - 1),(byte) (position[1] + 1) });
                if (position[1] == 1)
                {
                    neighbourCells.add(new byte[] { position[0], (byte)(position[1] + 1) });
                }
                else if (position[1] - 5 == position[0])
                {
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] - 1) });
                }
                else
                {
                    neighbourCells.add(new byte[] { position[0], (byte)(position[1] - 1 )});
                    neighbourCells.add(new byte[] { position[0], (byte)(position[1] + 1 )});
                }


            }
            else
            {

                if (position[1] == 1)//if its the first cell on a line
                {
                    neighbourCells.add(new byte[] { (byte)(position[0] - 1), position[1] });
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] + 1) });
                }
                else if (position[1] - 5 == position[0])//if it's the last cell on a line
                {
                    neighbourCells.add(new byte[] { (byte)(position[0] - 1), (byte)(position[1] - 1) });
                    neighbourCells.add(new byte[] { position[0], (byte)(position[1] - 1) });
                }
                else //if it's not
                {
                    // adding neighbours from the Previous line
                    neighbourCells.add(new byte[] { (byte)(position[0] - 1),(byte) (position[1] - 1) });
                    neighbourCells.add(new byte[] { (byte)(position[0] - 1), position[1] });
                    //adding neighbours from the SAME line
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] - 1) });
                    neighbourCells.add(new byte[] { position[0],(byte) (position[1] + 1) });
                }
                //adding neighbours from the NEXT line
                neighbourCells.add(new byte[] { (byte)(position[0] + 1), position[1] });
                neighbourCells.add(new byte[] { (byte)(position[0] + 1), (byte)(position[1] + 1) });
            }
        }
        else //for line 6
        {

            if (position[1] == 1)
            {
                neighbourCells.add(new byte[] { (byte)(position[0] - 1), position[1] });
                neighbourCells.add(new byte[] { position[0] , (byte)(position[1] + 1 )});
                neighbourCells.add(new byte[] { (byte)(position[0] + 1), position[1] });
            }
            else if(position[1] == 11) {
                neighbourCells.add(new byte[] { (byte)(position[0] - 1),(byte)( position[1] -1)});
                neighbourCells.add(new byte[] { position[0],(byte) (position[1] - 1) });
                neighbourCells.add(new byte[] { (byte)(position[0] + 1), (byte)(position[1] - 1) });
            }
            else
            {
                neighbourCells.add(new byte[] { (byte)(position[0] - 1), position[1] });
                neighbourCells.add(new byte[] { (byte)(position[0] - 1), (byte)(position[1] - 1) });
                neighbourCells.add(new byte[] { position[0], (byte)(position[1] - 1) });
                neighbourCells.add(new byte[] { position[0], (byte)(position[1] + 1) });
                neighbourCells.add(new byte[] { (byte)(position[0] + 1), position[1] });
                neighbourCells.add(new byte[] { (byte)(position[0] + 1), (byte)(position[1] - 1) });
            }


        }

    }

    public void setAbleToBeAttacked(int damage,boolean splashable){
        if(heroOnCell!=null){
            heroOnCell.takenDamage+=damage;
        }
        if(splashable){
            for (ServerCell cell:
                 gameBoardForServer.gameBoard) {
                for (byte[] coords:
                     neighbourCells) {
                    if(cell.position.equals(coords)){
                        cell.setAbleToBeAttacked(damage/2, false);
                        break;
                    }
                }
            }
        }

    }

    public void setAbleToMove(byte range){
        removeAll();
        if(heroOnCell!=null & range>=0 & !ableToMove) {
            ableToMove = true;
            for (byte[] neighboursCoords:
                 neighbourCells) {
                for (ServerCell cell:
                     gameBoardForServer.gameBoard) {
                    if(cell.position.equals(neighboursCoords)){
                        if(cell.heroOnCell==null)
                            cell.setAbleToMove((byte)(range-1));
                        break;
                    }
                }

            }

        }

    }

    public void setAbleToCast(){
        removeAll();
        ableToCast = true;
    }

    public void removeAll() {
        active = false;
        ableToMove = false;
        ableToCast = false;
    }
}
