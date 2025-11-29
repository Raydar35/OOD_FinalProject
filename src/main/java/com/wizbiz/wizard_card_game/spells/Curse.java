package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.WeakenEffect;

/**
 * Curse - Low damage but weakens enemy for several turns
 * Cost: 2 mana
 * Effect: 8 damage + Weaken for 3 turns (reduces enemy effectiveness)
 */
public class Curse extends Spell {
    public Curse() {
        this.name = "Curse";
        this.manaCost = 2;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(8);
        target.addEffect(new WeakenEffect(3));
    }
}