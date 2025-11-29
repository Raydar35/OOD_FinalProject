package com.wizbiz.wizard_card_game;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Deck - Contains all spell cards for the game
 * Both player and enemy draw from this shared deck
 */
public class Deck {
    private List<SpellCard> cards = new ArrayList<>();

    public Deck() {
        // Add variety of spells to the deck
        cards.addAll(SpellCardFactory.createMultiple("Fireball", 8));
        cards.addAll(SpellCardFactory.createMultiple("Ice Blast", 6));
        cards.addAll(SpellCardFactory.createMultiple("Lightning", 4));
        cards.addAll(SpellCardFactory.createMultiple("Heal", 6));
        cards.addAll(SpellCardFactory.createMultiple("Poison Cloud", 5));
        cards.addAll(SpellCardFactory.createMultiple("Drain", 4));
        cards.addAll(SpellCardFactory.createMultiple("Shield", 5));
        cards.addAll(SpellCardFactory.createMultiple("Meteor", 3));
        cards.addAll(SpellCardFactory.createMultiple("Regeneration", 4));
        cards.addAll(SpellCardFactory.createMultiple("Thunderbolt", 4));
        cards.addAll(SpellCardFactory.createMultiple("Curse", 5));

        // Shuffle the deck for variety
        Collections.shuffle(cards);
    }

    public DeckIterator iterator() {
        return new DeckIteratorImpl(cards);
    }

    private class DeckIteratorImpl implements DeckIterator {
        private int index = 0;
        private List<SpellCard> internal;

        public DeckIteratorImpl(List<SpellCard> cards) {
            this.internal = cards;
        }

        public boolean hasNext() {
            return index < internal.size();
        }

        public SpellCard next() {
            return internal.get(index++);
        }
    }
}