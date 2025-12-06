package com.wizbiz.wizard_card_game;

import com.wizbiz.wizard_card_game.commands.CastSpellCommand;
import com.wizbiz.wizard_card_game.commands.DrawCardCommand;

// STATE + COMMAND patterns - enemy AI turn
public class EnemyTurnState implements BattleState {
    private final GameController gc = GameController.getInstance();

    @Override
    public void enter() {
        gc.logAction("=== Enemy's turn begins ===");
        gc.getEnemy().startTurnEffects();
        gc.executeCommand(new DrawCardCommand(gc.getEnemy(), 1));

        String chosen = EnemyAI.chooseBestSpell(gc.getEnemy());
        if (chosen != null) {
            gc.executeCommand(new CastSpellCommand(gc.getEnemy(), gc.getPlayer(), chosen));
        } else {
            gc.logAction("Enemy has no playable cards!");
        }
        nextState();
    }

    @Override
    public void nextState() {
        gc.changeState(new PlayerTurnState());
    }
}