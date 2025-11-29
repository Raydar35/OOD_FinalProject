package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.StunEffect;

/**
 * Thunderbolt - Moderate damage with stun effect
 * Cost: 3 mana
 * Effect: 18 damage + Stun for 1 turn (prevents mana gain)
 */
public class Thunderbolt extends Spell {
    public Thunderbolt() {
        this.name = "Thunderbolt";
        this.manaCost = 3;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(18);
        target.addEffect(new StunEffect(1));
    }
}