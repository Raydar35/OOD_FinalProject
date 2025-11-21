package com.wizbiz.wizard_card_game.statuseffects;
import com.wizbiz.wizard_card_game.Actor;

public class BurnEffect implements StatusEffect {
    private int turnsLeft;

    public BurnEffect(int turns) {
        this.turnsLeft = turns;
    }

    @Override
    public void onTurnStart(Actor target) {
        target.takeDamage(3);
        turnsLeft--;
    }

    @Override
    public boolean isExpired() {
        return turnsLeft <= 0;
    }

    @Override
    public void refresh(StatusEffect other) {
        if (other instanceof BurnEffect) {
            this.turnsLeft = ((BurnEffect) other).turnsLeft;
        }
    }
}
