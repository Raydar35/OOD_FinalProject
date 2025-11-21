package com.wizbiz.wizard_card_game;

import java.util.Random;

public class EnemyAI {
    private static final String[] SPELL_POOL = {
            "Fireball"
    };

    public static String chooseBestSpell() {
        Random rand = new Random();
        return SPELL_POOL[rand.nextInt(SPELL_POOL.length)];
    }
}
