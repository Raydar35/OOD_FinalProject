package com.wizbiz.wizard_card_game.spells;
import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.statuseffects.FreezeEffect;

public class IceBlast extends Spell {
    public IceBlast() {
        this.name = "Ice Blast";
        this.manaCost = 2;
    }

    @Override
    protected void applyEffect(Actor caster, Actor target) {
        target.takeDamage(15);
        target.addEffect(new FreezeEffect(2));
    }
}