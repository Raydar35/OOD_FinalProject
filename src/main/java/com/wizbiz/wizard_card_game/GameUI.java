package com.wizbiz.wizard_card_game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameUI extends Application {

    private final GameController gc = GameController.getInstance();

    private Label playerHpLabel = new Label("Player HP: ");
    private Label enemyHpLabel  = new Label("Enemy HP: ");
    private TextArea actionLogArea = new TextArea();

    private HBox playerHandPane = new HBox(6);
    private Button endTurnButton = new Button("End Turn");

    @Override
    public void start(Stage stage) {
        gc.setUI(this);

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        HBox statusRow = new HBox(10, playerHpLabel, enemyHpLabel);
        statusRow.setPadding(new Insets(6));

        playerHandPane.setPadding(new Insets(6));
        playerHandPane.setStyle("-fx-border-color: gray; -fx-border-radius: 4; -fx-padding:6;");

        actionLogArea.setEditable(false);
        actionLogArea.setPrefHeight(200);

        HBox controls = new HBox(10);
        controls.getChildren().addAll(
                endTurnButton
        );

        root.getChildren().addAll(
                statusRow,
                new Label("Player Hand:"),
                playerHandPane,
                controls,
                new Label("Action Log:"),
                actionLogArea
        );

        // Button handlers
        endTurnButton.setOnAction(e -> {
            gc.endTurn();
            // controller will call changeState -> enter() and update UI
        });

        gc.startGame();
        refreshUI();

        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setTitle("Wizard Card Game - Prototype");
        stage.show();
    }

    // Adds a card in hand as a button
    public void addCardToHand(SpellCard card) {
        Button b = new Button(card.getName());
        b.setOnAction(e -> {
            gc.castSpell(card.getName());
            // UI updates handled by controller
        });
        playerHandPane.getChildren().add(b);
    }

    public void refreshHandDisplay() {
        playerHandPane.getChildren().clear();
        Player p = gc.getPlayer();
        for (SpellCard c : p.getHand()) {
            addCardToHand(c);
        }
    }

    public void updateLog(String logText) {
        actionLogArea.setText(logText);
    }

    public void refreshUI() {
        Player p = gc.getPlayer();
        Enemy  e = gc.getEnemy();

        playerHpLabel.setText("Player HP: " + p.getHp() + "  Mana: " + p.getMp());
        enemyHpLabel.setText("Enemy HP: " + e.getHp() + "  Mana: " + e.getMp());

        refreshHandDisplay();
        updateLog(gc.getActionLog());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
