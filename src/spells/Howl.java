package spells;

import CCServer.GameBoardForServer;
import CCServer.HeroClass;


public class Howl implements SpellInterface{
    private byte CD = 3; //cooldown of the spell
    private byte currentCD = 0;

    private GameBoardForServer gameBoardForServer;

    @Override
    public SpellInterface copySpell() {
        return new Howl();
    }

    @Override
    public void cast(GameBoardForServer gameBoardForServer, boolean radHero, byte[] targetCoords, HeroClass caster) throws Exception {
        if (currentCD >0){
            throw new Exception();
        }
        currentCD =CD;
        this.gameBoardForServer = gameBoardForServer;
        if(radHero){
            for (HeroClass hero:
                 gameBoardForServer.radiantHeroes) {
                hero.setEffects(new MoonBlessed());
            }
        }
    }

    @Override
    public void onTurnChange() {
        if(currentCD >0)
            currentCD--;
    }
}

class MoonBlessed implements EffectInterface{

    private byte bonusHP = 1;
    private byte bonusDMG = 1;

    private byte time = 2;
    private HeroClass hero;
    private byte remainTime;

    @Override
    public void giveEffect(HeroClass hero) {
        this.hero = hero;
        remainTime = time;
        hero.bonusDMG+=bonusDMG;
        hero.bonusHP+=bonusHP;
    }

    @Override
    public void onTurnChange() {
        remainTime--;
        if(remainTime==0)
            removeEffect();
    }

    @Override
    public void removeEffect() {
        hero.bonusDMG-=bonusDMG;
        hero.bonusHP-=bonusHP;
        hero.effects.remove(this);
        this.equals(null);
    }
}
