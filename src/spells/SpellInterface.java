package spells;
import CCServer.GameBoardForServer;
import CCServer.HeroClass;

public interface SpellInterface {
    // to keep the clone of spell to prevent some wrongs
    public SpellInterface copySpell();

    // to reduce cooldown or something that happens after each turn
    public void onTurnChange();

    //actual cast for spell
    public void cast(GameBoardForServer gameBoardForServer, boolean radHero, byte[] targetCoords, HeroClass caster) throws Exception;
}
