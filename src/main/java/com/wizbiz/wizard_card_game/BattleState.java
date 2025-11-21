package com.wizbiz.wizard_card_game;

public interface BattleState {
    void enter();
    void nextState();
    default void castSpell(String spellName) {}
}
