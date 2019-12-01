package spells;

import CCServer.GameBoardForServer;
import CCServer.HeroClass;
import CCServer.ServerCell;

import java.util.Random;

public class CallBrother implements SpellInterface {
    private byte CD = 4;
    private byte currentCD = 2;


    @Override
    public SpellInterface copySpell() {
        return new CallBrother();
    }

    @Override
    public void onTurnChange() {
        if(currentCD >0)
            currentCD--;
    }

    @Override
    public void cast(GameBoardForServer gameBoardForServer, boolean radHero, byte[] targetCoords, HeroClass caster) throws Exception {
        if (currentCD >0){
            throw new Exception();
        }
        Random random = new Random();
        while(true) {
            int num =  random.nextInt(90);
            ServerCell cell = gameBoardForServer.gameBoard.get(num);
            if(cell.heroOnCell==null){
                cell.poseHero(new HeroClass(gameBoardForServer.heroLibrary.get(1), gameBoardForServer));
                break;
            }
        }
        currentCD = CD;

    }
}
