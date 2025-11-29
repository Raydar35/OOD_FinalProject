package com.wizbiz.wizard_card_game;

/**
 * Enemy - Represents the AI opponent
 * Now includes visual customization based on player's opposite
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

    public EnemyCustomization getCustomization() {
        return customization;
    }

    public void setCustomization(EnemyCustomization customization) {
        this.customization = customization;
    }

    public String getName() {
        return customization != null ? customization.getEnemyName() : "Dark Wizard";
    }
}