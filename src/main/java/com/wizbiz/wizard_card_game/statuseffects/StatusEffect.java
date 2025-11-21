package com.wizbiz.wizard_card_game.statuseffects;
import com.wizbiz.wizard_card_game.Actor;

public interface StatusEffect {
    void onTurnStart(Actor target);
    boolean isExpired();
    default void refresh(StatusEffect other) {}
}
