package com.wizbiz.wizard_card_game.statuseffects;
import com.wizbiz.wizard_card_game.Actor;

public class PoisonEffect implements StatusEffect {
    private int turnsLeft;
    private int damagePerTurn;

    public PoisonEffect(int turns, int damagePerTurn) {
        this.turnsLeft = turns;
        this.damagePerTurn = damagePerTurn;
    }

    @Override
    public void onTurnStart(Actor target) {
        target.takeDamage(damagePerTurn);
        turnsLeft--;
    }

    @Override
    public boolean isExpired() {
        return turnsLeft <= 0;
    }

    @Override
    public void refresh(StatusEffect other) {
        if (other instanceof PoisonEffect) {
            PoisonEffect newPoison = (PoisonEffect) other;
            this.damagePerTurn += newPoison.damagePerTurn;
            this.turnsLeft = newPoison.turnsLeft;
        }
    }
}