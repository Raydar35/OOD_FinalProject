package com.wizbiz.wizard_card_game.commands;

import com.wizbiz.wizard_card_game.Actor;
import com.wizbiz.wizard_card_game.GameController;

// COMMAND - draw cards action
public class DrawCardCommand implements Command {
    private final GameController gc;
    private final Actor actor;
    private final int count;

    public DrawCardCommand(Actor actor, int count) {
        this.gc = GameController.getInstance();
        this.actor = actor;
        this.count = count;
    }

    @Override
    public void execute() {
        gc.drawForActor(actor, count);
    }

    @Override
    public String getDescription() {
        return "Draw " + count + " card(s)";
    }
}
