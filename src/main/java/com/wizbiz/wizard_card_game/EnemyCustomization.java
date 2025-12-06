package com.wizbiz.wizard_card_game;

import java.util.Random;

/**
 * EnemyCustomization - Generates enemy appearance based on player's choices
 * Uses opposite colors and random face (different from player)
 */
public class EnemyCustomization {

    private String faceType;
    private String hatType;
    private String robeColor;
    private String staffType;
    private String enemyName;

    // Evil wizard names for random selection
    private static final String[] EVIL_NAMES = {
            "Malachar", "Vexor", "Shadowmane", "Dreadmoor", "Nightshade",
            "Morgath", "Grimveil", "Darkflame", "Ravenclaw", "Thornhex",
            "Blackthorn", "Venomspire", "Skullcrusher", "Doomweaver", "Bloodmoon"
    };

    /**
     * Generate enemy customization based on player's choices
     * Uses opposite colors and random face (different from player)
     */
    public EnemyCustomization(PlayerCustomization playerCustom) {
        Random rand = new Random();

        // Random face (different from player if possible)
        String[] faces = {"RuggedWarrior", "WiseElder", "YoungProdigy"};
        do {
            faceType = faces[rand.nextInt(faces.length)];
        } while (faceType.equals(playerCustom.getFaceType()) && faces.length > 1);

        // Opposite hat type
        hatType = getOppositeHat(playerCustom.getHatType());

        // Opposite robe color
        robeColor = getOppositeColor(playerCustom.getRobeColor());

        // Opposite staff type
        staffType = getOppositeStaff(playerCustom.getStaffType());

        // Random evil name
        enemyName = EVIL_NAMES[rand.nextInt(EVIL_NAMES.length)];
    }

    /**
     * Get opposite hat type (only 4 available hats)
     */
    private String getOppositeHat(String playerHat) {
        switch (playerHat) {
            case "pointy_hat":
                return "wide_brim_hat";
            case "wide_brim_hat":
                return "pointy_hat";
            case "hood":
                return "top_hat";
            case "top_hat":
                return "hood";
            default:
                return "hood";
        }
    }

    /**
     * Get opposite robe color
     */
    private String getOppositeColor(String playerColor) {
        switch (playerColor) {
            case "blue":
                return "red";
            case "red":
                return "blue";
            case "purple":
                return "green";
            case "green":
                return "purple";
            case "black":
                return "white";
            case "white":
                return "black";
            default:
                return "black";
        }
    }

    /**
     * Get opposite staff type
     */
    private String getOppositeStaff(String playerStaff) {
        switch (playerStaff) {
            case "wooden_staff":
                return "bone_staff";
            case "bone_staff":
                return "wooden_staff";
            case "crystal_staff":
                return "gold_staff";
            case "gold_staff":
                return "crystal_staff";
            default:
                return "bone_staff";
        }
    }

    // Getters for all enemy customization options
    public String getHatType() { return hatType; }
    public String getRobeColor() { return robeColor; }
    public String getEnemyName() { return enemyName; }

    /**
     * Get image paths (same as PlayerCustomization)
     */
    public String getFaceImagePath() {
        return "/images/faces/" + faceType + getFaceExtension(faceType);
    }

    /**
     * Returns the file extension for face images.
     * All face images are currently .jpeg files.
     */
    private String getFaceExtension(String name) {
        return switch (name) {
            case "RuggedWarrior", "WiseElder", "YoungProdigy" -> ".jpeg";
            default -> ".png"; // fallback
        };
    }

    /**
     * Get the file path for the hat image.
     */
    public String getHatImagePath() {
        if (hatType == null || hatType.isEmpty()) return "/images/hats/TopHat.jpg";
        if (hatType.contains(".")) return "/images/hats/" + hatType;
        return switch (hatType) {
            case "pointy_hat" -> "/images/hats/PointyHat.jpg";
            case "wide_brim_hat" -> "/images/hats/WideBrim.jpg";
            case "hood" -> "/images/hats/hood.jpg";
            case "top_hat" -> "/images/hats/TopHat.jpg";
            default -> "/images/hats/TopHat.jpg";
        };
    }

    /**
     * Get the file path for the staff image.
     */
    public String getStaffImagePath() {
        if (staffType == null || staffType.isEmpty()) return "/images/staff/WoodStaff.png";

        // Map internal tokens to the exact resource filenames
        return switch (staffType) {
            case "wooden_staff" -> "/images/staff/WoodStaff.png";
            case "crystal_staff" -> "/images/staff/CrystalStaff.png";
            case "bone_staff" -> "/images/staff/BoneStaff.png";
            case "gold_staff" -> "/images/staff/GoldStaff.png";
            default -> "/images/staff/WoodStaff.png";
        };
    }
}