package com.wizbiz.wizard_card_game.spells;
import com.wizbiz.wizard_card_game.Actor;

public class Lightning extends Spell {
    public Lightning() {
        this.name = "Lightning";
        this.manaCost = 3;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(25);
    }
}