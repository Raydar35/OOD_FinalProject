package com.wizbiz.wizard_card_game;

// STATE PATTERN - defines battle phases (player/enemy turns)
public interface BattleState {
    void enter();
    void nextState();
    default void castSpell(String spellName) {}
}
