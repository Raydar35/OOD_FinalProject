package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;

public abstract class Spell {
    protected String name;
    protected int manaCost;

    protected abstract void applyEffect(Actor caster, Actor target);

    public final void cast(Actor caster, Actor target) {
        if (!caster.hasMp(manaCost)) return;

        caster.spendMp(manaCost);
        // optional animation here
        applyEffect(caster, target);
    }

    public String getName() { return name; }
}
