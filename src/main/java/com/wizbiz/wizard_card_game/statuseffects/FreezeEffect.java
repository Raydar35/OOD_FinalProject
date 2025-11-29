package com.wizbiz.wizard_card_game.statuseffects;
import com.wizbiz.wizard_card_game.Actor;

/**
 * FreezeEffect - Reduces mana by 1 each turn (prevents normal mana gain)
 * Applied by: Ice Blast spell
 */
public class FreezeEffect implements StatusEffect {
    private int turnsLeft;

    public FreezeEffect(int turns) {
        this.turnsLeft = turns;
    }

    @Override
    public void onTurnStart(Actor target) {
        // Freeze reduces mana gain effect - drain 1 mana if available
        if (target.getMp() > 0) {
            target.spendMp(1);
        }
        turnsLeft--;
    }

    @Override
    public boolean isExpired() {
        return turnsLeft <= 0;
    }

    @Override
    public void refresh(StatusEffect other) {
        if (other instanceof FreezeEffect) {
            this.turnsLeft = ((FreezeEffect) other).turnsLeft;
        }
    }
}