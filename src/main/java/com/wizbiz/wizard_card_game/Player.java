package com.wizbiz.wizard_card_game;

/**
 * Player - Represents the human player
 * Now includes visual customization
 */
public class Player extends Actor {
    private PlayerCustomization customization;

    // Default constructor with default appearance
    public Player() {
        super();
        this.customization = new PlayerCustomization();
    }

    // Constructor with custom appearance
    public Player(PlayerCustomization customization) {
        super();
        this.customization = customization;
    }

    public PlayerCustomization getCustomization() {
        return customization;
    }

    public void setCustomization(PlayerCustomization customization) {
        this.customization = customization;
    }

    public String getName() {
        return customization.getPlayerName();
    }
}