package com.wizbiz.wizard_card_game;
import com.wizbiz.wizard_card_game.spells.*;

import java.util.ArrayList;
import java.util.List;

public class SpellCardFactory {
    public static SpellCard create(String name) {
        switch (name) {
            case "Fireball":
                return new SpellCard(new Fireball());
            default:
                return null;
        }
    }

    // optional: multiple copies
    public static List<SpellCard> createMultiple(String name, int copies) {
        List<SpellCard> list = new ArrayList<>();
        for (int i = 0; i < copies; i++) {
            list.add(create(name));
        }
        return list;
    }
}
