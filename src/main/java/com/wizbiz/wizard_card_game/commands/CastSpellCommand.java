package com.wizbiz.wizard_card_game.commands;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.GameController;

// COMMAND - cast spell action
public class CastSpellCommand implements Command {
    private final GameController gc;
    private final Actor caster;
    private final Actor target;
    private final String spellName;

    public CastSpellCommand(Actor caster, Actor target, String spellName) {
        this.gc = GameController.getInstance();
        this.caster = caster;
        this.target = target;
        this.spellName = spellName;
    }

    @Override
    public void execute() {
        gc.playCard(caster, spellName, target);
    }

    @Override
    public String getDescription() {
        return "Cast " + spellName;
    }
}
