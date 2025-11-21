package com.wizbiz.wizard_card_game;

import com.wizbiz.wizard_card_game.spells.*;

public class SpellCard {
    private Spell spell;

    public SpellCard(Spell spell) {
        this.spell = spell;
    }

    public Spell getSpell() { return spell; }
    public String getName() { return spell.getName(); }
}
