package com.wizbiz.wizard_card_game;
import com.wizbiz.wizard_card_game.spells.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory Method Pattern - Creates spell cards
 * Centralized spell creation makes it easy to add new spells
 */
public class SpellCardFactory {

    /**
     * Creates a single spell card by name
     * @param name The name of the spell to create
     * @return SpellCard object or null if spell name not found
     */
    public static SpellCard create(String name) {
        switch (name) {
            case "Fireball":
                return new SpellCard(new Fireball());

            case "Ice Blast":
                return new SpellCard(new IceBlast());

            case "Lightning":
                return new SpellCard(new Lightning());

            case "Heal":
                return new SpellCard(new Heal());

            case "Poison Cloud":
                return new SpellCard(new PoisonCloud());

            case "Drain":
                return new SpellCard(new Drain());

            case "Shield":
                return new SpellCard(new Shield());

            case "Meteor":
                return new SpellCard(new Meteor());

            case "Regeneration":
                return new SpellCard(new Regeneration());

            case "Thunderbolt":
                return new SpellCard(new Thunderbolt());

            case "Curse":
                return new SpellCard(new Curse());

            default:
                return null;
        }
    }

    /**
     * Creates multiple copies of the same spell card
     * @param name The name of the spell
     * @param copies The number of copies to create
     * @return List of spell cards
     */
    public static List<SpellCard> createMultiple(String name, int copies) {
        List<SpellCard> list = new ArrayList<>();
        for (int i = 0; i < copies; i++) {
            list.add(create(name));
        }
        return list;
    }
}