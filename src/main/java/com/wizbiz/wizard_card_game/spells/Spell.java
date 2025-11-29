package com.wizbiz.wizard_card_game.spells;

import com.wizbiz.wizard_card_game.Actor;

/**
 * Spell - Abstract base class for all spells
 * Uses Template Method pattern - defines spell casting algorithm
 * Subclasses implement specific effects
 */
public abstract class Spell {
    protected String name;
    protected int manaCost;

    /**
     * Abstract method - subclasses must implement their specific effect
     * @param caster The actor casting the spell
     * @param target The target of the spell
     */
    protected abstract void applyEffect(Actor caster, Actor target);

    /**
     * Template Method - defines the spell casting algorithm
     * Final to prevent subclasses from changing the casting process
     * @param caster The actor casting the spell
     * @param target The target of the spell
     */
    public final void cast(Actor caster, Actor target) {
        if (!caster.hasMp(manaCost)) return;

        caster.spendMp(manaCost);
        // Future: could add animation hook here
        applyEffect(caster, target);
    }

    public String getName() {
        return name;
    }

    public int getManaCost() {
        return manaCost;
    }
}
