package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.BurnEffect;

public class Fireball extends Spell {
    public Fireball() {
        this.name = "Fireball";
        this.manaCost = 1;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(10);
        target.addEffect(new BurnEffect(3));
    }
}
