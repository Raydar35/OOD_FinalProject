package com.wizbiz.wizard_card_game;


/**
 * Player class - Represents the user's wizard
 * Extends Character with player-specific behavior and customization
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

    /**
     * Add bonus starting MP to the player (for win streak rewards)
     */
    public void addStartingMp(int bonus) {
        this.manaPoints += bonus;
    }
}