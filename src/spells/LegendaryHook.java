package spells;

import CCServer.GameBoardForServer;
import CCServer.HeroClass;
import CCServer.ServerCell;

public class LegendaryHook implements SpellInterface {
    private byte CD = 3;
    private byte currentCD = 0;

    private byte dmg = 3;

    @Override
    public SpellInterface copySpell() {
        return new LegendaryHook();
    }

    @Override
    public void onTurnChange() {
        if(currentCD >0)
            currentCD--;
    }

    @Override
    public void cast(GameBoardForServer gameBoardForServer, boolean radHero, byte[] targetCoords, HeroClass caster) throws Exception {
        HeroClass hookedHero = null;
        for (ServerCell cell:
             gameBoardForServer.gameBoard) {
            if(cell.position.equals(targetCoords)){
                hookedHero = cell.heroOnCell;

                ServerCell casterCell = gameBoardForServer.cellOnBoardByHero(caster);

                for (byte[] neighbourCoords:
                     casterCell.neighbourCells) {
                    if(gameBoardForServer.cellOnBoardByCoords(neighbourCoords).heroOnCell==null){
                        hookedHero.changePosition(neighbourCoords,true);
                        break;
                    }
                }
                break;
            }
        }
        if(hookedHero!=null){
            hookedHero.takenDamage+=dmg;
        }

    }
}
