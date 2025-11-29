package com.wizbiz.wizard_card_game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * GameUI - Enhanced with player customization and auto-generated enemy
 */
public class GameUI extends Application {

    private final GameController gc = GameController.getInstance();
    private PlayerCustomization playerCustomization;
    private EnemyCustomization enemyCustomization;

    private Label playerHpLabel = new Label("Player HP: ");
    private Label playerMpLabel = new Label("Player Mana: ");
    private Label enemyHpLabel  = new Label("Enemy HP: ");
    private Label enemyMpLabel  = new Label("Enemy Mana: ");

    private ImageView playerAvatarView;
    private ImageView enemyAvatarView;

    private TextArea actionLogArea = new TextArea();
    private HBox playerHandPane = new HBox(6);
    private Button endTurnButton = new Button("End Turn");

    @Override
    public void start(Stage stage) {
        // Show customization screen first
        CustomizationScreen customScreen = new CustomizationScreen(stage);
        customScreen.show(() -> {
            playerCustomization = customScreen.getCustomization();
            startGameWithCustomization(stage);
        });
    }

    private void startGameWithCustomization(Stage stage) {
        // Generate enemy customization based on player (opposite colors)
        enemyCustomization = new EnemyCustomization(playerCustomization);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.setStyle("-fx-background-color: #1a1a2e;");

        // Title with player name
        Label titleLabel = new Label("⚔️ Wizard Battle ⚔️");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; " +
                "-fx-text-fill: gold;");
        titleLabel.setAlignment(Pos.CENTER);

        // Battle area with avatars
        HBox battleArea = createBattleArea();

        // Player hand display
        VBox handSection = new VBox(5);
        Label handLabel = new Label("Your Hand:");
        handLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        playerHandPane.setPadding(new Insets(6));
        playerHandPane.setStyle("-fx-border-color: #4a90e2; -fx-border-radius: 4; " +
                "-fx-padding: 6; -fx-background-color: #2a2a4a; " +
                "-fx-background-radius: 4;");
        handSection.getChildren().addAll(handLabel, playerHandPane);

        // Action log
        Label logLabel = new Label("Battle Log:");
        logLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        actionLogArea.setEditable(false);
        actionLogArea.setPrefHeight(150);
        actionLogArea.setStyle("-fx-control-inner-background: #1a1a2e; " +
                "-fx-text-fill: white;");

        // Controls
        HBox controls = new HBox(10);
        controls.setAlignment(Pos.CENTER);
        endTurnButton.setStyle("-fx-font-size: 14px; -fx-padding: 8 20; " +
                "-fx-background-color: #e74c3c; -fx-text-fill: white;");
        controls.getChildren().add(endTurnButton);

        root.getChildren().addAll(
                titleLabel,
                battleArea,
                handSection,
                controls,
                logLabel,
                actionLogArea
        );

        // Button handler
        endTurnButton.setOnAction(e -> gc.endTurn());

        // Register this UI with GameController so it can call refreshUI() and updateLog()
        gc.setUI(this);

        // Start game with customized player AND enemy
        Player customPlayer = new Player(playerCustomization);
        Enemy customEnemy = new Enemy(enemyCustomization);
        gc.startGameWithCustomizations(customPlayer, customEnemy);
        refreshUI();

        Scene scene = new Scene(root, 700, 650);
        stage.setScene(scene);
        stage.setTitle("Wizard Card Game - " + playerCustomization.getPlayerName());
        stage.show();
    }

    private HBox createBattleArea() {
        HBox battleArea = new HBox(40);
        battleArea.setAlignment(Pos.CENTER);
        battleArea.setPadding(new Insets(10));
        battleArea.setStyle("-fx-background-color: #2a1a4a; -fx-border-radius: 10; " +
                "-fx-background-radius: 10;");

        // Player side
        VBox playerSide = createCharacterDisplay(true);

        // VS label
        Label vsLabel = new Label("⚔️\nVS\n⚔️");
        vsLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: gold; " +
                "-fx-font-weight: bold;");
        vsLabel.setAlignment(Pos.CENTER);

        // Enemy side
        VBox enemySide = createCharacterDisplay(false);

        battleArea.getChildren().addAll(playerSide, vsLabel, enemySide);
        return battleArea;
    }

    private VBox createCharacterDisplay(boolean isPlayer) {
        VBox characterBox = new VBox(10);
        characterBox.setAlignment(Pos.CENTER);
        characterBox.setPadding(new Insets(10));
        characterBox.setStyle("-fx-border-color: " + (isPlayer ? "#4a90e2" : "#e74c3c") +
                "; -fx-border-width: 2; -fx-border-radius: 5; " +
                "-fx-background-color: #1a1a2e; -fx-background-radius: 5;");

        // Character name
        Label nameLabel = new Label(isPlayer ? playerCustomization.getPlayerName() : enemyCustomization.getEnemyName());
        nameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; " +
                "-fx-text-fill: " + (isPlayer ? "#4a90e2" : "#e74c3c"));

        // Avatar image
        ImageView avatarView = new ImageView();
        avatarView.setFitWidth(120);
        avatarView.setFitHeight(120);
        avatarView.setPreserveRatio(true);

        if (isPlayer) {
            playerAvatarView = avatarView;
            loadPlayerAvatar();
        } else {
            enemyAvatarView = avatarView;
            loadEnemyAvatar();
        }

        // HP and Mana labels
        Label hpLabel = isPlayer ? playerHpLabel : enemyHpLabel;
        Label mpLabel = isPlayer ? playerMpLabel : enemyMpLabel;

        hpLabel.setStyle("-fx-text-fill: #e74c3c; -fx-font-size: 14px;");
        mpLabel.setStyle("-fx-text-fill: #4a90e2; -fx-font-size: 14px;");

        characterBox.getChildren().addAll(nameLabel, avatarView, hpLabel, mpLabel);
        return characterBox;
    }

    private void loadPlayerAvatar() {
        try {
            // Try to load composite avatar
            String avatarPath = playerCustomization.getCompositeAvatarPath();
            Image avatar = new Image(getClass().getResourceAsStream(avatarPath));
            playerAvatarView.setImage(avatar);
        } catch (Exception e) {
            // Fallback: try to load just the face
            try {
                String facePath = playerCustomization.getFaceImagePath();
                Image face = new Image(getClass().getResourceAsStream(facePath));
                playerAvatarView.setImage(face);
            } catch (Exception ex) {
                // Use placeholder
                createPlaceholderAvatar(playerAvatarView, "#4a90e2");
            }
        }
    }

    private void loadEnemyAvatar() {
        try {
            // Load enemy avatar based on generated customization
            String avatarPath = enemyCustomization.getCompositeAvatarPath();
            Image enemyImg = new Image(getClass().getResourceAsStream(avatarPath));
            enemyAvatarView.setImage(enemyImg);
        } catch (Exception e) {
            // Try loading just the face
            try {
                String facePath = enemyCustomization.getFaceImagePath();
                Image face = new Image(getClass().getResourceAsStream(facePath));
                enemyAvatarView.setImage(face);
            } catch (Exception ex) {
                // Use placeholder
                createPlaceholderAvatar(enemyAvatarView, "#e74c3c");
            }
        }
    }

    private void createPlaceholderAvatar(ImageView imageView, String color) {
        // Create a simple colored circle as placeholder
        // (You could also use a default wizard silhouette image)
        imageView.setImage(null);
    }

    public void addCardToHand(SpellCard card) {
        Button b = new Button(card.getName());
        b.setStyle("-fx-font-size: 12px; -fx-padding: 8 12; " +
                "-fx-background-color: #4a90e2; -fx-text-fill: white; " +
                "-fx-background-radius: 5;");
        b.setOnAction(e -> gc.castSpell(card.getName()));
        playerHandPane.getChildren().add(b);
    }

    public void refreshHandDisplay() {
        playerHandPane.getChildren().clear();
        Player p = gc.getPlayer();
        if (p != null && p.getHand() != null) {
            for (SpellCard c : p.getHand()) {
                addCardToHand(c);
            }
        }
    }

    public void refreshUI() {
        Player p = gc.getPlayer();
        Enemy e = gc.getEnemy();

        if (p != null && e != null) {
            playerHpLabel.setText("HP: " + p.getHp());
            playerMpLabel.setText("Mana: " + p.getMp());
            enemyHpLabel.setText("HP: " + e.getHp());
            enemyMpLabel.setText("Mana: " + e.getMp());

            refreshHandDisplay();
            actionLogArea.setText(gc.getActionLog());
        }
    }

    public void updateLog(String logText) {
        actionLogArea.setText(logText);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

