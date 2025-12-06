package com.wizbiz.wizard_card_game;
import com.wizbiz.wizard_card_game.spells.*;

import java.util.ArrayList;
import java.util.List;

// FACTORY METHOD - creates spell cards
public class SpellCardFactory {

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

    public static List<SpellCard> createMultiple(String name, int copies) {
        List<SpellCard> list = new ArrayList<>();
        for (int i = 0; i < copies; i++) {
            list.add(create(name));
        }
        return list;
    }
}