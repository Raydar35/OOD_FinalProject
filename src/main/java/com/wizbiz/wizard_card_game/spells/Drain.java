package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;

/**
 * Drain - Damages enemy and heals caster
 * Cost: 3 mana
 * Effect: Deal 12 damage, heal caster for 12
 */
public class Drain extends Spell {
    public Drain() {
        this.name = "Drain";
        this.manaCost = 3;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(12);
        caster.heal(12);
    }
}