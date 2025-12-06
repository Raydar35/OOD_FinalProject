package com.wizbiz.wizard_card_game;

/**
 * Enemy - Represents the AI opponent
 * Now includes visual customization based on player's opposite
 */
/**
 * Enemy class - Represents the opponent wizard
 * Extends Character with enemy-specific behavior and customization
 */
public class Enemy extends Actor {

    private EnemyCustomization customization;

    // Default constructor
    public Enemy() {
        super();
        this.customization = null; // Will be set later
    }

    // Constructor with customization
    public Enemy(EnemyCustomization customization) {
        super();
        this.customization = customization;
    }


    public String getName() {
        return customization != null ? customization.getEnemyName() : "Dark Wizard";
    }

    /**
     * Add bonus HP to the enemy (for difficulty scaling)
     */
    public void addHp(int bonus) {
        this.healthPoints += bonus;
    }

    /**
     * Add bonus MP to the enemy (for difficulty scaling)
     */
    public void addMp(int bonus) {
        this.manaPoints += bonus;
    }
}