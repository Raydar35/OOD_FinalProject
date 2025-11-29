package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.PoisonEffect;

/**
 * Poison Cloud - Low initial damage but strong poison effect
 * Cost: 2 mana
 * Effect: 5 damage + Poison (4 damage per turn for 4 turns)
 */
public class PoisonCloud extends Spell {
    public PoisonCloud() {
        this.name = "Poison Cloud";
        this.manaCost = 2;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(5);
        target.addEffect(new PoisonEffect(4, 4));
    }
}