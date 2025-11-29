package com.wizbiz.wizard_card_game;

/**
 * PlayerCustomization - Stores player's visual appearance choices
 * Includes face style and wizard outfit components
 */
public class PlayerCustomization {
    // Face options (only 3 now)
    private String faceType;  // "face1", "face2", or "face3"

    // Outfit components
    private String hatType;    // e.g., "pointy_hat", "wide_brim_hat", "crown"
    private String robeColor;  // e.g., "blue", "red", "purple", "green"
    private String staffType;  // e.g., "wooden_staff", "crystal_staff", "bone_staff"

    // Player name
    private String playerName;

    // Default constructor with basic appearance
    public PlayerCustomization() {
        this.faceType = "face1";
        this.hatType = "pointy_hat";
        this.robeColor = "blue";
        this.staffType = "wooden_staff";
        this.playerName = "Wizard";
    }

    // Constructor with custom values
    public PlayerCustomization(String faceType, String hatType, String robeColor,
                               String staffType, String playerName) {
        this.faceType = faceType;
        this.hatType = hatType;
        this.robeColor = robeColor;
        this.staffType = staffType;
        this.playerName = playerName;
    }

    // Getters
    public String getFaceType() { return faceType; }
    public String getHatType() { return hatType; }
    public String getRobeColor() { return robeColor; }
    public String getStaffType() { return staffType; }
    public String getPlayerName() { return playerName; }

    // Setters
    public void setFaceType(String faceType) { this.faceType = faceType; }
    public void setHatType(String hatType) { this.hatType = hatType; }
    public void setRobeColor(String robeColor) { this.robeColor = robeColor; }
    public void setStaffType(String staffType) { this.staffType = staffType; }
    public void setPlayerName(String playerName) { this.playerName = playerName; }

    /**
     * Get the file path for the face image
     * @return Path to face image resource
     */
    public String getFaceImagePath() {
        return "/images/faces/" + faceType + ".png";
    }

    /**
     * Get the file path for the hat image
     * @return Path to hat image resource
     */
    public String getHatImagePath() {
        return "/images/hats/" + hatType + ".png";
    }

    /**
     * Get the file path for the robe image
     * @return Path to robe image resource
     */
    public String getRobeImagePath() {
        return "/images/robes/" + robeColor + "_robe.png";
    }

    /**
     * Get the file path for the staff image
     * @return Path to staff image resource
     */
    public String getStaffImagePath() {
        return "/images/staffs/" + staffType + ".png";
    }

    /**
     * Get composite avatar image path (if using pre-composed images)
     * @return Path to full avatar image
     */
    public String getCompositeAvatarPath() {
        return "/images/avatars/" + faceType + "_" + hatType + "_" +
                robeColor + "_" + staffType + ".png";
    }
}