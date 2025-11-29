package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.BurnEffect;

/**
 * Meteor - Extremely high damage spell with burn effect
 * Cost: 5 mana
 * Effect: 35 damage + Burn for 2 turns
 */
public class Meteor extends Spell {
    public Meteor() {
        this.name = "Meteor";
        this.manaCost = 5;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(35);
        target.addEffect(new BurnEffect(2));
    }
}