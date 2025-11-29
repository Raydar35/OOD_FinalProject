package com.wizbiz.wizard_card_game;

/**
 * EnemyTurnState - Handles the enemy's turn in the game
 * Part of the State Pattern
 */
public class EnemyTurnState implements BattleState {
    private final GameController gc = GameController.getInstance();

    @Override
    public void enter() {
        gc.logAction("=== Enemy's turn begins ===");
        gc.getEnemy().startTurnEffects(); // start-of-turn effects
        gc.drawForActor(gc.getEnemy(), 1); // Enemy draws 1 card at start of turn

        // Enemy AI chooses and plays
        String chosen = EnemyAI.chooseBestSpell(gc.getEnemy());

        if (chosen != null) {
            gc.playCard(gc.getEnemy(), chosen, gc.getPlayer());
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