package com.wizbiz.wizard_card_game;

import java.util.List;
import java.util.Random;

/**
 * EnemyAI - Determines which spell the enemy should cast
 * Currently uses simple random selection from available hand
 */
public class EnemyAI {

    /**
     * Chooses the best spell from the enemy's hand
     * Simple AI: randomly selects from available cards in hand
     *
     * @param enemy The enemy actor with cards in hand
     * @return The name of the spell to cast, or null if no valid spell
     */
    public static String chooseBestSpell(Enemy enemy) {
        List<SpellCard> hand = enemy.getHand();

        if (hand.isEmpty()) {
            return null;
        }

        Random rand = new Random();

        // Try to find a spell the enemy can afford
        // Attempt up to 10 times to find an affordable spell
        for (int attempt = 0; attempt < 10; attempt++) {
            SpellCard card = hand.get(rand.nextInt(hand.size()));

            // Check if enemy has enough mana for this spell
            if (enemy.hasMp(card.getSpell().getManaCost())) {
                return card.getName();
            }
        }

        // If no affordable spell found, return the cheapest spell in hand
        SpellCard cheapest = hand.get(0);
        for (SpellCard card : hand) {
            if (card.getSpell().getManaCost() < cheapest.getSpell().getManaCost()) {
                cheapest = card;
            }
        }

        return cheapest.getName();
    }
}
