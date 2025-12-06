package com.wizbiz.wizard_card_game.commands;

import com.wizbiz.wizard_card_game.BattleState;

// COMMAND - end turn action
public class EndTurnCommand implements Command {
    private final BattleState currentState;

    public EndTurnCommand(BattleState currentState) {
        this.currentState = currentState;
    }

    @Override
    public void execute() {
        currentState.nextState();
    }

    @Override
    public String getDescription() {
        return "End Turn";
    }
}
