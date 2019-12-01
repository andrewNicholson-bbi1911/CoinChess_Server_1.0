package spells;

import CCServer.HeroClass;

public interface EffectInterface {

    public void giveEffect(HeroClass hero);
    public void onTurnChange();

    public void removeEffect();

}
