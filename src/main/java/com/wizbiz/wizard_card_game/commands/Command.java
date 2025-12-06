package com.wizbiz.wizard_card_game.commands;

// COMMAND PATTERN - wraps actions as objects
public interface Command {
    void execute();
    String getDescription();
}
