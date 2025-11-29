package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.ShieldEffect;

/**
 * Shield - Provides damage absorption
 * Cost: 2 mana
 * Effect: Absorbs up to 15 points of damage
 */
public class Shield extends Spell {
    public Shield() {
        this.name = "Shield";
        this.manaCost = 2;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        // Shield targets the caster for protection
        // Provides 15 shield points (absorbs 15 damage total)
        caster.addEffect(new ShieldEffect(15));
    }
}