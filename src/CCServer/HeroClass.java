package CCServer;

import spells.EffectInterface;
import spells.SpellInterface;

import java.util.ArrayList;

public class HeroClass{

    private GameBoardForServer activeGameBoard;

    int code = 0;

    String heroName = "";

    int hp;

    int damage;

    int attackRange;

    int attackSplashRadius;

    byte moveRange;

    int spellID;
    SpellInterface spell;
    // optional stats

    public ArrayList<EffectInterface> effects = new ArrayList<>();

    public int takenDamage = 0;

    public byte bonusDMG = 0;
    public byte bonusHP = 0;

    boolean rad;

    public HeroClass(String[] info, GameBoardForServer gameBoardForServer){
        activeGameBoard = gameBoardForServer;
        this.code =  Integer.parseInt(info[0]);
        this.heroName = info[1];
        this.hp = Integer.parseInt(info[2]);
        this.damage = Integer.parseInt(info[3]);
        this.attackRange = Integer.parseInt(info[4]);
        this.attackSplashRadius = Integer.parseInt(info[5]);
        this.moveRange = Byte.parseByte(info[6]);
        this.spellID = Integer.parseInt(info[7]);

//        spell = CoinChessServer.spellLibrary.get(this.spellID-1).copySpell();
    }
    public HeroClass(HeroClass pattern, GameBoardForServer gameBoardForServer){
        activeGameBoard = gameBoardForServer;

        code = pattern.code;
        heroName = pattern.heroName;
        hp = pattern.hp;
        damage = pattern.damage;
        attackRange = pattern.attackRange;
        attackSplashRadius = pattern.attackSplashRadius;
        moveRange = pattern.moveRange;
        spellID = pattern.spellID;

        spell = CoinChessServer.spellLibrary.get(this.spellID-1).copySpell();
    }
    public HeroClass() {

    }

    public void reinitialization(){
        HeroClass pattern = null;
        for (HeroClass hero:
             HeroesAll.getHeroesBase()) {
            if(hero.code==code){
                pattern = hero;
                break;
            }
        }

        code = pattern.code;
        heroName = pattern.heroName;
        hp = pattern.hp;
        damage = pattern.damage;
        attackRange = pattern.attackRange;
        attackSplashRadius = pattern.attackSplashRadius;
        moveRange = pattern.moveRange;
        spellID = pattern.spellID;
    }

    public void setEffects(EffectInterface effect){
        effect.giveEffect(this);
    }


    public void changePosition(byte[] newCoords, boolean flying) throws Exception {

        ServerCell activeCell = activeGameBoard.cellOnBoardByHero(this);
        ServerCell targetCell = activeGameBoard.cellOnBoardByCoords(newCoords);

        if(activeGameBoard.checkMoveAble(activeCell, targetCell) | flying){
            if(targetCell.heroOnCell==null){
                activeCell.heroOnCell = null;
                targetCell.heroOnCell = this;
            }else throw new Exception();
        }else throw new Exception();

    }

    public void cast(byte[] targetCoords)  {
        try {
            spell.cast(activeGameBoard, rad, targetCoords, this);
        }catch(Exception e){
            //it happends when cooldown;
        }
        }
}
