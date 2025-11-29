package com.wizbiz.wizard_card_game.statuseffects;
import com.wizbiz.wizard_card_game.Actor;

/**
 * RegenEffect - Heals the target each turn
 * Applied by: Regeneration spell
 */
public class RegenEffect implements StatusEffect {
    private int turnsLeft;
    private int healPerTurn;

    public RegenEffect(int turns, int healPerTurn) {
        this.turnsLeft = turns;
        this.healPerTurn = healPerTurn;
    }

    @Override
    public void onTurnStart(Actor target) {
        target.heal(healPerTurn);
        turnsLeft--;
    }

    @Override
    public boolean isExpired() {
        return turnsLeft <= 0;
    }

    @Override
    public void refresh(StatusEffect other) {
        if (other instanceof RegenEffect) {
            RegenEffect otherRegen = (RegenEffect) other;
            this.turnsLeft = otherRegen.turnsLeft;
            this.healPerTurn = otherRegen.healPerTurn;
        }
    }
}