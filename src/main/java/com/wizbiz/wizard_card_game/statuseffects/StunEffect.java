package com.wizbiz.wizard_card_game.statuseffects;
import com.wizbiz.wizard_card_game.Actor;

/**
 * StunEffect - Prevents the target from gaining mana for a turn
 * Applied by: Thunderbolt spell
 */
public class StunEffect implements StatusEffect {
    private int turnsLeft;

    public StunEffect(int turns) {
        this.turnsLeft = turns;
    }

    @Override
    public void onTurnStart(Actor target) {
        // Stun prevents mana gain - drain all mana gained this turn
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
        if (other instanceof StunEffect) {
            this.turnsLeft = ((StunEffect) other).turnsLeft;
        }
    }
}