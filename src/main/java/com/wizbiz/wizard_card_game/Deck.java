package com.wizbiz.wizard_card_game;

import com.wizbiz.wizard_card_game.spells.Fireball;
import com.wizbiz.wizard_card_game.spells.Spell;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<SpellCard> cards = new ArrayList<>();

    public Deck() {
        cards.addAll(SpellCardFactory.createMultiple("Fireball", 20));
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
