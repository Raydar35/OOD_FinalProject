package com.wizbiz.wizard_card_game;

import com.wizbiz.wizard_card_game.commands.CastSpellCommand;
import com.wizbiz.wizard_card_game.commands.DrawCardCommand;

// STATE + COMMAND patterns - player's turn actions
public class PlayerTurnState implements BattleState {
    private final GameController gc = GameController.getInstance();

    @Override
    public void enter() {
        gc.logAction("Player's turn starts.");
        gc.getPlayer().startTurnEffects();
        gc.executeCommand(new DrawCardCommand(gc.getPlayer(), 1));
    }

    @Override
    public void castSpell(String name) {
        gc.executeCommand(new CastSpellCommand(gc.getPlayer(), gc.getEnemy(), name));
        nextState();
    }

    @Override
    public void nextState() {
        gc.changeState(new EnemyTurnState());
    }
}
