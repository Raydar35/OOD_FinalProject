package com.wizbiz.wizard_card_game;

public class PlayerTurnState implements BattleState {
    private final GameController gc = GameController.getInstance();

    @Override
    public void enter() {
        gc.logAction("Player's turn starts.");
        gc.getPlayer().startTurnEffects(); // Apply start-of-turn effects
        gc.drawForActor(gc.getPlayer(), 1); // Player draws 1 card at start of turn
    }

    @Override
    public void castSpell(String name) {
        gc.playCard(gc.getPlayer(), name, gc.getEnemy());
        nextState();
    }

    @Override
    public void nextState() {
        gc.changeState(new EnemyTurnState());
    }
}
