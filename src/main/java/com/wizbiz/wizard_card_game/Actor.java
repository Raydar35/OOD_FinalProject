package com.wizbiz.wizard_card_game;
import com.wizbiz.wizard_card_game.statuseffects.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Actor {
    protected int healthPoints = 100;
    protected int manaPoints = 0;

    protected List<SpellCard> hand = new ArrayList<>();
    protected List<StatusEffect> effects = new ArrayList<>();

    // MAXIMUM HAND SIZE
    private static final int MAX_HAND_SIZE = 5;

    public void drawCards(DeckIterator it, int count) {
        for (int i = 0; i < count && it.hasNext(); i++) {
            // Only draw if hand isn't full
            if (hand.size() < MAX_HAND_SIZE) {
                hand.add(it.next());
            } else {
                break; // Hand is full, stop drawing
            }
        }
    }

    public void startTurnEffects() {
        manaPoints += 1;
        for (Iterator<StatusEffect> it = effects.iterator(); it.hasNext();) {
            StatusEffect effect = it.next();
            effect.onTurnStart(this);
            if (effect.isExpired()) it.remove();
        }
    }

    public void addEffect(StatusEffect effect) {
        if (effect == null) return;
        for (StatusEffect existing : effects) {
            if (existing.getClass() == effect.getClass()) {
                existing.refresh(effect);
                return;
            }
        }
        effects.add(effect);
    }

    public void takeDamage(int dmg) { healthPoints -= dmg; }
    public void heal(int amt) { healthPoints += amt; }

    public boolean hasMp(int cost) { return manaPoints >= cost; }
    public void spendMp(int cost) { manaPoints -= cost; }

    public int getHp() { return healthPoints; }
    public int getMp() { return manaPoints; }
    public List<SpellCard> getHand() { return hand; }

    public int getMaxHandSize() { return MAX_HAND_SIZE; }
}