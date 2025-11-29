package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.RegenEffect;

/**
 * Regeneration - Provides healing over time
 * Cost: 3 mana
 * Effect: Heal 5 HP per turn for 4 turns
 */
public class Regeneration extends Spell {
    public Regeneration() {
        this.name = "Regeneration";
        this.manaCost = 3;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        // Regeneration targets the caster for healing
        caster.addEffect(new RegenEffect(4, 5));
    }
}