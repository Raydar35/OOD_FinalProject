package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;

/**
 * Heal - Restores health to the caster
 * Cost: 2 mana
 * Effect: Restore 20 HP to caster
 */
public class Heal extends Spell {
    public Heal() {
        this.name = "Heal";
        this.manaCost = 2;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        // Heal targets the caster, not the enemy
        caster.heal(20);
    }
}