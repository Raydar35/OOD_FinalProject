package com.wizbiz.wizard_card_game;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.util.Duration;
import com.wizbiz.wizard_card_game.commands.EndTurnCommand;

import java.io.InputStream;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// Main UI for the Wizard Card Game - handles visuals and player interactions
// Implements Observer Pattern for automatic updates
public class GameUI extends Application implements GameObserver {

    private static final double WINDOW_WIDTH = 900;
    private static final double WINDOW_HEIGHT = 750;
    private static final double PADDING_LARGE = 30;
    private static final double PADDING_MEDIUM = 20;
    private static final double PADDING_SMALL = 15;
    private static final double SPACING_LARGE = 25;
    private static final double SPACING_MEDIUM = 15;
    private static final double SPACING_SMALL = 10;

    private final GameController gc = GameController.getInstance();

    private Label playerHpLabel = new Label("100");
    private Label playerMpLabel = new Label("0");
    private Label enemyHpLabel = new Label("100");
    private Label enemyMpLabel = new Label("0");

    private ProgressBar playerHpBar = new ProgressBar(1.0);
    private ProgressBar playerMpBar = new ProgressBar(0.0);
    private ProgressBar enemyHpBar = new ProgressBar(1.0);
    private ProgressBar enemyMpBar = new ProgressBar(0.0);

    private TextArea logArea = new TextArea();
    private HBox handPane = new HBox(SPACING_MEDIUM);
    private Button endTurnBtn = new Button("END TURN");

    private Pane animationPane;
    private Stage primaryStage;

    private PlayerCustomization playerCustomization;
    private EnemyCustomization enemyCustomization;

    private ImageView previewIcon;
    private ImageView previewHatIcon;
    private ImageView previewStaffIcon;
    private Circle previewCircle;

    private boolean gameEnded = false;
    private int winStreak = 0;
    private int currentDifficulty = 1;
    private int lastHandSize = -1;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setResizable(true);
        showCustomizationScreen();
    }

    private void showCustomizationScreen() {
        winStreak = 0;
        currentDifficulty = 1;

        StackPane root = new StackPane();
        root.setBackground(createDeepPurpleBackground());

        VBox customizationBox = new VBox(SPACING_MEDIUM);
        customizationBox.setAlignment(Pos.CENTER);
        customizationBox.setPadding(new Insets(PADDING_MEDIUM, PADDING_LARGE, PADDING_MEDIUM, PADDING_LARGE));
        customizationBox.setMaxHeight(WINDOW_HEIGHT);

        VBox titleBox = createTitleSection("WIZARD CREATION CHAMBER", "Forge Your Destiny", 34, 14);
        VBox customPanel = new VBox(SPACING_MEDIUM);
        customPanel.setPadding(new Insets(SPACING_MEDIUM));
        customPanel.setAlignment(Pos.CENTER);
        customPanel.setMaxWidth(650);
        customPanel.setStyle(createPanelStyle());

        VBox previewSection = createPreviewSection();
        Rectangle separator1 = createSeparator();
        VBox nameBox = createEnhancedInputBox("Wizard Name", "Enter your name");
        TextField nameField = (TextField) ((HBox) nameBox.getChildren().get(1)).getChildren().get(0);
        Rectangle separator2 = createSeparator();
        GridPane selectionsGrid = new GridPane();
        selectionsGrid.setHgap(SPACING_MEDIUM);
        selectionsGrid.setVgap(SPACING_SMALL);
        selectionsGrid.setAlignment(Pos.CENTER);

        VBox faceBox = createEnhancedComboBox("Face", new String[]{"Rugged Warrior", "Wise Elder", "Young Prodigy"});
        ComboBox<String> faceCombo = (ComboBox<String>) ((HBox) faceBox.getChildren().get(1)).getChildren().get(0);

        VBox hatBox = createEnhancedComboBox("Headwear", new String[]{"Pointy Hat", "Wide Brim", "Hood", "Top Hat"});
        ComboBox<String> hatCombo = (ComboBox<String>) ((HBox) hatBox.getChildren().get(1)).getChildren().get(0);

        VBox robeBox = createEnhancedComboBox("Robe Color", new String[]{"Azure Blue", "Crimson Red", "Royal Purple", "Emerald Green", "Midnight Black", "Pure White"});
        ComboBox<String> robeCombo = (ComboBox<String>) ((HBox) robeBox.getChildren().get(1)).getChildren().get(0);

        VBox staffBox = createEnhancedComboBox("Staff", new String[]{"Oak Wood", "Crystal", "Bone", "Gold"});
        ComboBox<String> staffCombo = (ComboBox<String>) ((HBox) staffBox.getChildren().get(1)).getChildren().get(0);

        selectionsGrid.add(faceBox, 0, 0);
        selectionsGrid.add(hatBox, 1, 0);
        selectionsGrid.add(robeBox, 0, 1);
        selectionsGrid.add(staffBox, 1, 1);

        faceCombo.setOnAction(e -> updatePreviewAnimation(faceCombo.getValue(), hatCombo.getValue(), staffCombo.getValue()));
        hatCombo.setOnAction(e -> updatePreviewAnimation(faceCombo.getValue(), hatCombo.getValue(), staffCombo.getValue()));
        robeCombo.setOnAction(e -> {
            updatePreviewColor(robeCombo.getValue());
            updatePreviewAnimation(faceCombo.getValue(), hatCombo.getValue(), staffCombo.getValue());
        });
        staffCombo.setOnAction(e -> updatePreviewAnimation(faceCombo.getValue(), hatCombo.getValue(), staffCombo.getValue()));

        Rectangle separator3 = createSeparator();
        Button startBtn = createEnhancedButton();

        startBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) name = "Wizard";
            playerCustomization = new PlayerCustomization(parseFace(faceCombo.getValue()), parseHat(hatCombo.getValue()),
                parseRobe(robeCombo.getValue()), parseStaff(staffCombo.getValue()), name);
            enemyCustomization = new EnemyCustomization(playerCustomization);
            startBattle();
        });

        Label footerText = new Label("Prepare yourself for epic magical duels");
        footerText.setFont(Font.font("Georgia", FontPosture.ITALIC, 12));
        footerText.setTextFill(Color.web("#9370DB"));
        footerText.setOpacity(0.7);

        customPanel.getChildren().addAll(
                previewSection,
                separator1,
                nameBox,
                separator2,
                selectionsGrid,
                separator3,
                startBtn,
                footerText
        );

        updatePreviewAnimation(faceCombo.getValue(), hatCombo.getValue(), staffCombo.getValue());

        customizationBox.getChildren().addAll(titleBox, customPanel);
        root.getChildren().add(customizationBox);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Wizard Character Creation");
        primaryStage.show();
        playFadeIn(root);
    }

    private void startBattle() {
        startBattle(false);
    }

    private void startBattle(boolean isContinuation) {
        gameEnded = false;
        lastHandSize = -1;
        if (isContinuation) enemyCustomization = new EnemyCustomization(playerCustomization);

        StackPane root = new StackPane();
        root.setBackground(createDeepPurpleBackground());

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(PADDING_SMALL));

        animationPane = new Pane();
        animationPane.setMouseTransparent(true);

        VBox topSection = createTopSection();
        VBox centerSection = createCenterSection();
        VBox bottomSection = createBottomSection();

        mainLayout.setTop(topSection);
        mainLayout.setCenter(centerSection);
        mainLayout.setBottom(bottomSection);

        root.getChildren().addAll(mainLayout, animationPane);

        endTurnBtn.setOnAction(e -> {
            if (!gameEnded) {
                gc.executeCommand(new EndTurnCommand(gc.getCurrentState()));
                playTurnTransitionAnimation();
            }
        });

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);

        String difficultyText = currentDifficulty > 1 ? " [Difficulty " + currentDifficulty + "]" : "";
        primaryStage.setTitle(playerCustomization.getPlayerName() + " vs " + enemyCustomization.getEnemyName() + difficultyText);

        gc.setUI(this);
        gc.addObserver(this);

        Player customPlayer = new Player(playerCustomization);
        Enemy customEnemy = new Enemy(enemyCustomization);

        // Both wizards start with mana equal to current round (1 mana in round 1, 2 in round 2, etc.)
        customPlayer.addStartingMp(currentDifficulty);
        customEnemy.addMp(currentDifficulty);

        // Enemy gets HP bonus for higher difficulties
        if (currentDifficulty > 1) {
            int hpBonus = (currentDifficulty - 1) * 20;
            customEnemy.addHp(hpBonus);
        }

        gc.startGameWithCustomizations(customPlayer, customEnemy);
        refreshUI();

        playFadeIn(root);
    }

    /**
     * Creates the top section of the battle screen.
     * Contains the title and the battle arena with both characters.
     */
    private VBox createTopSection() {
        VBox top = new VBox(SPACING_SMALL);
        top.setAlignment(Pos.CENTER);
        top.setPadding(new Insets(PADDING_SMALL, 0, 0, 0));
        VBox titleBox = createTitleSection("ARENA OF MYSTIC COMBAT", "Battle for Magical Supremacy", 28, 12);
        HBox arena = createBattleArena();
        top.getChildren().addAll(titleBox, arena);
        return top;
    }

    private HBox createBattleArena() {
        HBox arena = new HBox(30);
        arena.setAlignment(Pos.CENTER);
        arena.setPadding(new Insets(PADDING_SMALL));
        arena.setStyle(createPanelStyle());

        VBox playerBox = createCharacterBox(true);

        // VS section
        VBox vsBox = new VBox(5);
        vsBox.setAlignment(Pos.CENTER);

        Label vsLabel = new Label("VS");
        vsLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        vsLabel.setTextFill(Color.web("#FFD700"));
        vsLabel.setStyle("-fx-alignment: center;");
        vsLabel.setEffect(createGlowEffect(Color.web("#FFA500"), 15, 0.6));

        // Pulsing animation instead of rotating
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), vsLabel);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.1);
        pulse.setToY(1.1);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        vsBox.getChildren().add(vsLabel);

        VBox enemyBox = createCharacterBox(false);

        arena.getChildren().addAll(playerBox, vsBox, enemyBox);
        return arena;
    }

    private VBox createCharacterBox(boolean isPlayer) {
        VBox box = new VBox(SPACING_SMALL);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(PADDING_SMALL));
        box.setPrefWidth(240);

        // Get robe color for this character
        String robeColor = isPlayer ? playerCustomization.getRobeColor() : enemyCustomization.getRobeColor();
        String[] colors = getRobeColorGradient(robeColor);
        String borderColor = colors[0];

        // Use semi-transparent version of robe color for box background
        String bgColor1 = convertToRGBA(colors[0]);
        String bgColor2 = convertToRGBA(colors[2]);

        box.setStyle(
                "-fx-background-color: linear-gradient(to bottom, " + bgColor1 + ", " + bgColor2 + ");" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, " + borderColor + ", 15, 0.3, 0, 0);"
        );

        // Character icon with decorative ring - NOW SHOWS ACTUAL FACE IMAGES
        StackPane iconPane = new StackPane();

        Circle outerRing = new Circle(45);
        outerRing.setFill(Color.TRANSPARENT);
        outerRing.setStroke(Color.web(borderColor));
        outerRing.setStrokeWidth(2);
        outerRing.setOpacity(0.5);

        // Use player's robe color for character icon background
        Circle iconCircle = new Circle(36);
        iconCircle.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(colors[0])),
                new Stop(0.5, Color.web(colors[1])),
                new Stop(1, Color.web(colors[2]))
        ));
        iconCircle.setEffect(createGlowEffect(Color.web(borderColor), 20, 0.5));

        // Try to load actual face image, fallback to emoji if fails
        boolean imageLoaded = false;

        try {
            String facePath = isPlayer ?
                    playerCustomization.getFaceImagePath() :
                    enemyCustomization.getFaceImagePath();

            InputStream in = getClass().getResourceAsStream(facePath);
            if (in != null) {
                Image faceImg = new Image(in);

                // Create ImageView for face and bind its size to the iconCircle diameter so it adapts
                ImageView faceImageView = new ImageView(faceImg);
                faceImageView.fitWidthProperty().bind(iconCircle.radiusProperty().multiply(2));
                faceImageView.fitHeightProperty().bind(iconCircle.radiusProperty().multiply(2));
                faceImageView.setPreserveRatio(true);
                faceImageView.setSmooth(true);  // Smooth rendering

                // Create a circular clip whose radius is bound to the iconCircle radius so clipping adapts
                Circle imageClip = new Circle();
                imageClip.radiusProperty().bind(iconCircle.radiusProperty());
                // Keep clip centered on the actual image bounds
                faceImageView.boundsInLocalProperty().addListener((obs, oldB, newB) -> {
                    double w = newB.getWidth();
                    double h = newB.getHeight();
                    imageClip.setCenterX(w / 2.0);
                    imageClip.setCenterY(h / 2.0);
                });
                faceImageView.setClip(imageClip);

                imageLoaded = true;

                // Floating animation for the image
                TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(2.5), faceImageView);
                floatAnim.setByY(-4);
                floatAnim.setCycleCount(Animation.INDEFINITE);
                floatAnim.setAutoReverse(true);
                floatAnim.play();

                // Add outer ring, background circle and image so the image appears inside the circular frame
                iconPane.getChildren().addAll(outerRing, iconCircle, faceImageView);

                // Hat overlay with dynamic positioning
                String hatType = isPlayer ? playerCustomization.getHatType() : enemyCustomization.getHatType();
                double yOffset = "pointy_hat".equals(hatType) ? -1.15 :
                                "hood".equals(hatType) ? -0.35 : -0.7;
                ImageView hatView = addHatOverlay(iconPane, iconCircle, 1.2, yOffset);
                String hatPath = isPlayer ? playerCustomization.getHatImagePath() : enemyCustomization.getHatImagePath();
                setHatImage(hatView, hatPath);

                // Hood: position after rim but before face
                if ("hood".equals(hatType)) {
                    int faceIndex = iconPane.getChildren().indexOf(faceImageView);
                    iconPane.getChildren().remove(hatView);
                    iconPane.getChildren().add(faceIndex, hatView);
                }

                // Staff overlay
                ImageView staffView = addStaffOverlay(iconPane, iconCircle, 0.8, -0.4, 0.0);
                String staffPath = isPlayer ? playerCustomization.getStaffImagePath() : enemyCustomization.getStaffImagePath();
                setStaffImage(staffView, staffPath);
            }
        } catch (Exception e) {
        }

        // If image didn't load, use text fallback
        if (!imageLoaded) {
            Label fallbackIcon = new Label("W");
            fallbackIcon.setFont(Font.font("Georgia", FontWeight.BOLD, 44));
            fallbackIcon.setTextFill(Color.WHITE);

            // Floating animation for fallback
            TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(2.5), fallbackIcon);
            floatAnim.setByY(-4);
            floatAnim.setCycleCount(Animation.INDEFINITE);
            floatAnim.setAutoReverse(true);
            floatAnim.play();

            iconPane.getChildren().addAll(outerRing, iconCircle, fallbackIcon);

            // Hat overlay (fallback case) with dynamic positioning
            String hatType = isPlayer ? playerCustomization.getHatType() : enemyCustomization.getHatType();
            double yOffset = "pointy_hat".equals(hatType) ? -1.15 :
                            "hood".equals(hatType) ? -0.35 : -0.7;
            ImageView hatView = addHatOverlay(iconPane, iconCircle, 1.2, yOffset);
            String hatPath = isPlayer ? playerCustomization.getHatImagePath() : enemyCustomization.getHatImagePath();
            setHatImage(hatView, hatPath);

            if ("hood".equals(hatType)) {
                int fallbackIndex = iconPane.getChildren().indexOf(fallbackIcon);
                iconPane.getChildren().remove(hatView);
                iconPane.getChildren().add(fallbackIndex, hatView);
            }

            // Staff overlay (fallback case)
            ImageView staffView = addStaffOverlay(iconPane, iconCircle, 0.8, -0.4, 0.0);
            String staffPath = isPlayer ? playerCustomization.getStaffImagePath() : enemyCustomization.getStaffImagePath();
            setStaffImage(staffView, staffPath);
        }

        String displayName = isPlayer ?
                playerCustomization.getPlayerName() :
                enemyCustomization.getEnemyName();

        Label nameLabel = new Label(displayName.toUpperCase());
        nameLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        nameLabel.setTextFill(Color.web("#FFD700"));
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-alignment: center;");
        nameLabel.setMaxWidth(220);
        nameLabel.setEffect(createGlowEffect(Color.web(borderColor), 10, 0.4));

        VBox hpBox = createStatBar(
                isPlayer ? playerHpLabel : enemyHpLabel,
                isPlayer ? playerHpBar : enemyHpBar,
                "HP", "#DC143C"
        );

        VBox mpBox = createStatBar(
                isPlayer ? playerMpLabel : enemyMpLabel,
                isPlayer ? playerMpBar : enemyMpBar,
                "MANA", "#4169E1"
        );

        box.getChildren().addAll(iconPane, nameLabel, hpBox, mpBox);
        return box;
    }

    private VBox createStatBar(Label valueLabel, ProgressBar bar, String name, String color) {
        VBox statBox = new VBox(5);
        statBox.setAlignment(Pos.CENTER);

        Label statLabel = new Label(name);
        statLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        statLabel.setTextFill(Color.web("#FFD700"));

        valueLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        valueLabel.setTextFill(Color.WHITE);

        bar.setPrefWidth(170);
        bar.setPrefHeight(18);
        bar.setStyle(
                "-fx-accent: " + color + ";" +
                        "-fx-control-inner-background: rgba(26, 26, 46, 0.8);" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;"
        );

        HBox labelBox = new HBox(8);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().addAll(statLabel, valueLabel);

        statBox.getChildren().addAll(labelBox, bar);
        return statBox;
    }

    private VBox createCenterSection() {
        VBox center = new VBox(SPACING_SMALL);
        center.setPadding(new Insets(PADDING_SMALL, 0, PADDING_SMALL, 0));
        center.setAlignment(Pos.CENTER);

        Label handLabel = new Label("YOUR SPELLBOOK");
        handLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        handLabel.setTextFill(Color.web("#FFD700"));
        handLabel.setEffect(createGlowEffect(Color.web("#FFA500"), 15, 0.5));

        // Enhanced hand container
        handPane.setAlignment(Pos.CENTER);
        handPane.setPadding(new Insets(PADDING_SMALL));
        handPane.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(42, 26, 74, 0.8), rgba(28, 17, 51, 0.8));" +
                        "-fx-border-color: linear-gradient(to right, #FFD700, #FFA500, #FFD700);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.3), 20, 0.2, 0, 0);"
        );

        center.getChildren().addAll(handLabel, handPane);
        return center;
    }

    private VBox createBottomSection() {
        VBox bottom = new VBox(SPACING_SMALL);
        bottom.setPadding(new Insets(0, PADDING_SMALL, PADDING_SMALL, PADDING_SMALL));

        Label logLabel = new Label("BATTLE LOG");
        logLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        logLabel.setTextFill(Color.web("#FFD700"));
        logLabel.setEffect(createGlowEffect(Color.web("#FFA500"), 10, 0.4));

        logArea.setEditable(false);
        logArea.setPrefHeight(80);
        logArea.setWrapText(true);
        logArea.setStyle(
                "-fx-control-inner-background: rgba(26, 26, 46, 0.9);" +
                        "-fx-text-fill: #00FF00;" +
                        "-fx-font-family: 'Courier New';" +
                        "-fx-font-size: 12px;" +
                        "-fx-border-color: linear-gradient(to right, #FFD700, #FFA500, #FFD700);" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );

        // Enhanced end turn button
        endTurnBtn.setPrefWidth(240);
        endTurnBtn.setPrefHeight(45);
        endTurnBtn.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        endTurnBtn.setStyle(createEndTurnButtonStyle());

        endTurnBtn.setOnMouseEntered(e -> {
            if (!gameEnded) {
                endTurnBtn.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #FF1744, #C62828);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 15;" +
                                "-fx-border-color: #FFD700;" +
                                "-fx-border-width: 4;" +
                                "-fx-border-radius: 15;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(255, 23, 68, 0.7), 20, 0.6, 0, 0);"
                );
            }
        });

        endTurnBtn.setOnMouseExited(e -> {
            if (!gameEnded) {
                endTurnBtn.setStyle(createEndTurnButtonStyle());
            }
        });

        HBox btnBox = new HBox(endTurnBtn);
        btnBox.setAlignment(Pos.CENTER);

        bottom.getChildren().addAll(logLabel, logArea, btnBox);
        return bottom;
    }

    private void showVictoryScreen() {
        winStreak++;
        currentDifficulty++;

        StackPane root = new StackPane();
        root.setBackground(createVictoryBackground());

        VBox victoryBox = new VBox(SPACING_LARGE);
        victoryBox.setAlignment(Pos.CENTER);
        victoryBox.setPadding(new Insets(PADDING_LARGE));

        // Victory title
        VBox titleBox = createTitleSection(
                "VICTORY!",
                "You have proven your magical prowess!",
                40,
                16
        );

        // Adjust title glow to green
        Label title = (Label) titleBox.getChildren().get(0);
        title.setEffect(createGlowEffect(Color.web("#32CD32"), 40, 1.0));

        // Victory panel
        VBox victoryPanel = new VBox(SPACING_MEDIUM);
        victoryPanel.setPadding(new Insets(PADDING_LARGE));
        victoryPanel.setAlignment(Pos.CENTER);
        victoryPanel.setMaxWidth(600);
        victoryPanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(42, 74, 42, 0.9), rgba(28, 51, 28, 0.9));" +
                        "-fx-border-color: linear-gradient(to right, #FFD700, #32CD32, #FFD700);" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(50, 205, 50, 0.6), 30, 0.5, 0, 0);"
        );

        // Winner icon
        Label winnerIcon = new Label("V");
        winnerIcon.setFont(Font.font("Georgia", FontWeight.BOLD, 75));
        winnerIcon.setTextFill(Color.web("#FFD700"));
        winnerIcon.setEffect(createGlowEffect(Color.web("#32CD32"), 30, 0.8));

        ScaleTransition iconPulse = new ScaleTransition(Duration.seconds(2), winnerIcon);
        iconPulse.setFromX(1.0);
        iconPulse.setFromY(1.0);
        iconPulse.setToX(1.1);
        iconPulse.setToY(1.1);
        iconPulse.setCycleCount(Animation.INDEFINITE);
        iconPulse.setAutoReverse(true);
        iconPulse.play();

        // Winner name
        Label winnerName = new Label(playerCustomization.getPlayerName().toUpperCase());
        winnerName.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        winnerName.setTextFill(Color.web("#FFD700"));

        // Victory message
        Label victoryMessage = new Label("Defeated " + enemyCustomization.getEnemyName());
        victoryMessage.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        victoryMessage.setTextFill(Color.web("#90EE90"));

        Rectangle separator1 = createSeparator();

        // Win Streak Display
        VBox streakBox = new VBox(SPACING_SMALL);
        streakBox.setAlignment(Pos.CENTER);

        Label streakLabel = new Label("WIN STREAK: " + winStreak);
        streakLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        streakLabel.setTextFill(Color.web("#FFD700"));
        streakLabel.setEffect(createGlowEffect(Color.web("#FFA500"), 15, 0.6));

        if (winStreak > 1) {
            Label streakSubtext = new Label("Next opponent will be Level " + currentDifficulty + " difficulty");
            streakSubtext.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
            streakSubtext.setTextFill(Color.web("#FFFFFF"));

            Label mpBonusText = new Label("You'll start with +" + winStreak + " bonus mana");
            mpBonusText.setFont(Font.font("Georgia", FontPosture.ITALIC, 13));
            mpBonusText.setTextFill(Color.web("#87CEEB"));

            streakBox.getChildren().addAll(streakLabel, streakSubtext, mpBonusText);
        } else {
            Label streakSubtext = new Label("Next opponent will be Level " + currentDifficulty + " difficulty");
            streakSubtext.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
            streakSubtext.setTextFill(Color.web("#FFFFFF"));

            Label mpBonusText = new Label("You'll start with +1 bonus mana");
            mpBonusText.setFont(Font.font("Georgia", FontPosture.ITALIC, 13));
            mpBonusText.setTextFill(Color.web("#87CEEB"));

            streakBox.getChildren().addAll(streakLabel, streakSubtext, mpBonusText);
        }

        Rectangle separator2 = createSeparator();

        // Stats
        VBox statsBox = new VBox(SPACING_SMALL);
        statsBox.setAlignment(Pos.CENTER);

        Label hpRemaining = new Label("HP Remaining: " + gc.getPlayer().getHp());
        hpRemaining.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        hpRemaining.setTextFill(Color.WHITE);

        Label mpRemaining = new Label("Mana Remaining: " + gc.getPlayer().getMp());
        mpRemaining.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        mpRemaining.setTextFill(Color.WHITE);

        Label difficultyLabel = new Label("Difficulty Level: " + (currentDifficulty - 1));
        difficultyLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        difficultyLabel.setTextFill(Color.web("#FFD700"));

        statsBox.getChildren().addAll(hpRemaining, mpRemaining, difficultyLabel);

        Rectangle separator3 = createSeparator();

        // Buttons - three options
        Button continueBtn = createActionButton("CONTINUE YOUR JOURNEY", "#32CD32", "#228B22");
        continueBtn.setOnAction(e -> {
            startBattle(true);
        });

        Button newWizardBtn = createActionButton("CREATE NEW WIZARD", "#4169E1", "#1E3A8A");
        newWizardBtn.setOnAction(e -> showCustomizationScreen());

        Button exitBtn = createActionButton("EXIT GAME", "#8B0000", "#5C0000");
        exitBtn.setOnAction(e -> primaryStage.close());

        victoryPanel.getChildren().addAll(
                winnerIcon,
                winnerName,
                victoryMessage,
                separator1,
                streakBox,
                separator2,
                statsBox,
                separator3,
                continueBtn,
                newWizardBtn,
                exitBtn
        );

        victoryBox.getChildren().addAll(titleBox, victoryPanel);
        root.getChildren().add(victoryBox);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("VICTORY!");

        playFadeIn(root);
    }

    private void showDefeatScreen() {
        StackPane root = new StackPane();
        root.setBackground(createDefeatBackground());

        VBox defeatBox = new VBox(SPACING_LARGE);
        defeatBox.setAlignment(Pos.CENTER);
        defeatBox.setPadding(new Insets(PADDING_LARGE));

        // Defeat title
        VBox titleBox = createTitleSection(
                "DEFEAT",
                "Your magical journey ends here...",
                40,
                16
        );

        // Adjust title glow to red
        Label title = (Label) titleBox.getChildren().get(0);
        title.setTextFill(Color.web("#DC143C"));
        title.setEffect(createGlowEffect(Color.web("#8B0000"), 40, 1.0));

        Label subtitle = (Label) titleBox.getChildren().get(1);
        subtitle.setTextFill(Color.web("#FF6B6B"));

        // Defeat panel
        VBox defeatPanel = new VBox(SPACING_MEDIUM);
        defeatPanel.setPadding(new Insets(PADDING_LARGE));
        defeatPanel.setAlignment(Pos.CENTER);
        defeatPanel.setMaxWidth(600);
        defeatPanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(74, 42, 42, 0.9), rgba(51, 28, 28, 0.9));" +
                        "-fx-border-color: linear-gradient(to right, #DC143C, #8B0000, #DC143C);" +
                        "-fx-border-width: 4;" +
                        "-fx-border-radius: 25;" +
                        "-fx-background-radius: 25;" +
                        "-fx-effect: dropshadow(gaussian, rgba(220, 20, 60, 0.6), 30, 0.5, 0, 0);"
        );

        // Defeat icon
        Label defeatIcon = new Label("X");
        defeatIcon.setFont(Font.font("Georgia", FontWeight.BOLD, 75));
        defeatIcon.setTextFill(Color.web("#DC143C"));
        defeatIcon.setEffect(createGlowEffect(Color.web("#8B0000"), 30, 0.8));

        ScaleTransition iconPulse = new ScaleTransition(Duration.seconds(2), defeatIcon);
        iconPulse.setFromX(1.0);
        iconPulse.setFromY(1.0);
        iconPulse.setToX(1.1);
        iconPulse.setToY(1.1);
        iconPulse.setCycleCount(Animation.INDEFINITE);
        iconPulse.setAutoReverse(true);
        iconPulse.play();

        // Defeated name
        Label defeatedName = new Label(playerCustomization.getPlayerName().toUpperCase());
        defeatedName.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        defeatedName.setTextFill(Color.web("#DC143C"));

        // Defeat message
        Label defeatMessage = new Label("Vanquished by " + enemyCustomization.getEnemyName());
        defeatMessage.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        defeatMessage.setTextFill(Color.web("#FF6B6B"));

        Rectangle separator1 = createSeparator();

        // Win Streak Achievement (if any)
        VBox achievementBox = new VBox(SPACING_SMALL);
        achievementBox.setAlignment(Pos.CENTER);

        if (winStreak > 0) {
            Label streakAchieved = new Label("WIN STREAK ACHIEVED: " + winStreak);
            streakAchieved.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
            streakAchieved.setTextFill(Color.web("#FFD700"));

            String rankMessage = getStreakRank(winStreak);
            Label rankLabel = new Label(rankMessage);
            rankLabel.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
            rankLabel.setTextFill(Color.web("#FFD700"));

            Label difficultyReached = new Label("Reached Difficulty Level " + (currentDifficulty - 1));
            difficultyReached.setFont(Font.font("Georgia", 13));
            difficultyReached.setTextFill(Color.WHITE);

            achievementBox.getChildren().addAll(streakAchieved, rankLabel, difficultyReached);
        } else {
            Label noStreak = new Label("Defeated on first battle");
            noStreak.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
            noStreak.setTextFill(Color.web("#FF6B6B"));
            achievementBox.getChildren().add(noStreak);
        }

        Rectangle separator2 = createSeparator();

        // Motivational quote
        Label quote = new Label("\"Even the greatest wizards must fall before they can rise again\"");
        quote.setFont(Font.font("Georgia", FontPosture.ITALIC, 15));
        quote.setTextFill(Color.web("#FFD700"));
        quote.setWrapText(true);
        quote.setStyle("-fx-alignment: center;");
        quote.setMaxWidth(500);

        Rectangle separator3 = createSeparator();

        // Buttons - two options
        Button returnBtn = createActionButton("CREATE NEW WIZARD", "#DC143C", "#8B0000");
        returnBtn.setOnAction(e -> showCustomizationScreen());

        Button exitBtn = createActionButton("EXIT GAME", "#4B0000", "#2B0000");
        exitBtn.setOnAction(e -> primaryStage.close());

        defeatPanel.getChildren().addAll(
                defeatIcon,
                defeatedName,
                defeatMessage,
                separator1,
                achievementBox,
                separator2,
                quote,
                separator3,
                returnBtn,
                exitBtn
        );

        defeatBox.getChildren().addAll(titleBox, defeatPanel);
        root.getChildren().add(defeatBox);

        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Defeat");

        playFadeIn(root);
    }

    private String getStreakRank(int streak) {
        if (streak >= 10) return "LEGENDARY WIZARD!";
        if (streak >= 7) return "MASTER WIZARD!";
        if (streak >= 5) return "EXPERT WIZARD!";
        if (streak >= 3) return "SKILLED WIZARD!";
        if (streak >= 1) return "APPRENTICE WIZARD!";
        return "";
    }

    // Helper methods for styling

    private Background createDeepPurpleBackground() {
        return new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0a0612")),
                        new Stop(0.3, Color.web("#1a0f2e")),
                        new Stop(0.6, Color.web("#2d1b4e")),
                        new Stop(1, Color.web("#1e0f3d"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        ));
    }

    private Background createVictoryBackground() {
        return new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0a1e0a")),
                        new Stop(0.3, Color.web("#1a4d1a")),
                        new Stop(0.6, Color.web("#2d6e2d")),
                        new Stop(1, Color.web("#1e3d1e"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        ));
    }

    private Background createDefeatBackground() {
        return new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#1e0a0a")),
                        new Stop(0.3, Color.web("#4d1a1a")),
                        new Stop(0.6, Color.web("#6e2d2d")),
                        new Stop(1, Color.web("#3d1e1e"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        ));
    }

    private String createPanelStyle() {
        return "-fx-background-color: linear-gradient(to bottom, rgba(42, 26, 74, 0.9), rgba(28, 17, 51, 0.9));" +
                "-fx-border-color: linear-gradient(to right, #FFD700, #FFA500, #FFD700);" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 20;" +
                "-fx-background-radius: 20;" +
                "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.4), 25, 0.3, 0, 0);";
    }

    private String createEndTurnButtonStyle() {
        return "-fx-background-color: linear-gradient(to bottom, #DC143C, #8B0000);" +
                "-fx-text-fill: white;" +
                "-fx-background-radius: 15;" +
                "-fx-border-color: #FFD700;" +
                "-fx-border-width: 3;" +
                "-fx-border-radius: 15;" +
                "-fx-cursor: hand;" +
                "-fx-effect: dropshadow(gaussian, rgba(220, 20, 60, 0.5), 15, 0.4, 0, 0);";
    }

    private DropShadow createGlowEffect(Color color, double radius, double spread) {
        DropShadow glow = new DropShadow();
        glow.setColor(color);
        glow.setRadius(radius);
        glow.setSpread(spread);
        return glow;
    }

    private VBox createTitleSection(String mainText, String subText, int mainSize, int subSize) {
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);

        Label title = new Label(mainText);
        title.setFont(Font.font("Georgia", FontWeight.BOLD, mainSize));
        title.setTextFill(Color.web("#FFD700"));
        title.setEffect(createGlowEffect(Color.web("#FFA500"), 25, 0.8));

        // Pulsing animation
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), title);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        Label subtitle = new Label(subText);
        subtitle.setFont(Font.font("Georgia", FontPosture.ITALIC, subSize));
        subtitle.setTextFill(Color.web("#C4A47C"));
        subtitle.setOpacity(0.8);

        // Subtitle fade
        FadeTransition subtitleFade = new FadeTransition(Duration.seconds(3), subtitle);
        subtitleFade.setFromValue(0.6);
        subtitleFade.setToValue(1.0);
        subtitleFade.setCycleCount(Animation.INDEFINITE);
        subtitleFade.setAutoReverse(true);
        subtitleFade.play();

        titleBox.getChildren().addAll(title, subtitle);
        return titleBox;
    }

    private VBox createPreviewSection() {
        VBox box = new VBox(SPACING_SMALL);
        box.setAlignment(Pos.CENTER);

        // Preview circle frame
        previewCircle = new Circle(70);
        previewCircle.setStroke(Color.GOLD);
        previewCircle.setStrokeWidth(3);
        previewCircle.setFill(Color.rgb(50, 50, 80));

        // Add magical glow effect to face circle
        DropShadow circleGlow = new DropShadow();
        circleGlow.setColor(Color.GOLD);
        circleGlow.setRadius(15);
        circleGlow.setSpread(0.6);
        previewCircle.setEffect(circleGlow);

        // ImageView for displaying the actual character face (clipped to circle)
        previewIcon = new ImageView();
        // bind preview image size to the preview circle diameter so it adapts automatically
        previewIcon.fitWidthProperty().bind(previewCircle.radiusProperty().multiply(2));
        previewIcon.fitHeightProperty().bind(previewCircle.radiusProperty().multiply(2));
        previewIcon.setPreserveRatio(true);
        previewIcon.setSmooth(true);

        // Circular clip for the preview image bound to the previewCircle radius
        Circle previewClip = new Circle();
        previewClip.radiusProperty().bind(previewCircle.radiusProperty());
        previewIcon.boundsInLocalProperty().addListener((obs, oldB, newB) -> {
            double w = newB.getWidth();
            double h = newB.getHeight();
            previewClip.setCenterX(w / 2.0);
            previewClip.setCenterY(h / 2.0);
        });
        previewIcon.setClip(previewClip);

        // Stack the circle background and the clipped image so the image appears inside the circle
        StackPane previewStack = new StackPane(previewCircle, previewIcon);

        // Add hat overlay for preview (adjusted to fit all face types)
        // Pointy hat needs extra upward offset
        double hatYOffset = -0.7; // Default for most hats
        previewHatIcon = addHatOverlay(previewStack, previewCircle, 1.2, hatYOffset);

        // Gold box frame for staff (matching the style of the circle around the face)
        // Box size matches staff size (0.8 scale * 2 = 1.6 * radius, plus small padding)
        Rectangle staffBox = new Rectangle();
        staffBox.widthProperty().bind(previewCircle.radiusProperty().multiply(1.7)); // Slightly larger than staff for padding
        staffBox.heightProperty().bind(previewCircle.radiusProperty().multiply(1.7)); // Square box to frame staff nicely
        staffBox.setArcWidth(15); // Rounded corners
        staffBox.setArcHeight(15); // Rounded corners
        staffBox.setStroke(Color.GOLD);
        staffBox.setStrokeWidth(3);
        staffBox.setFill(Color.TRANSPARENT);

        // Add magical glow effect to staff box
        DropShadow staffGlow = new DropShadow();
        staffGlow.setColor(Color.GOLD);
        staffGlow.setRadius(15);
        staffGlow.setSpread(0.6);
        staffBox.setEffect(staffGlow);

        StackPane.setAlignment(staffBox, Pos.CENTER_RIGHT);
        // Position box at same location as staff
        staffBox.translateXProperty().bind(previewCircle.radiusProperty().multiply(-0.4));
        staffBox.translateYProperty().bind(previewCircle.radiusProperty().multiply(0.0));
        previewStack.getChildren().add(staffBox);

        // Add staff overlay for preview (right next to circle, outside but close)
        previewStaffIcon = addStaffOverlay(previewStack, previewCircle, 0.8, -0.4, 0.0);

        Label text = new Label("Character Preview");
        text.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        text.setTextFill(Color.web("#FFD700"));

        box.getChildren().addAll(previewStack, text);

        return box;
    }

    private Rectangle createSeparator() {
        Rectangle separator = new Rectangle(340, 2);
        separator.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.web("#FFD700", 0.5)),
                new Stop(1, Color.TRANSPARENT)
        ));
        return separator;
    }

    private Button createEnhancedButton() {
        String text = "ENTER THE ARENA";
        double width = 280;
        double height = 48;
        Button btn = new Button(text);
        btn.setPrefWidth(width);
        btn.setPrefHeight(height);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 17));
        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FFD700, #FFA500, #FF8C00);" +
                        "-fx-text-fill: #1a0f2e;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.6), 15, 0.5, 0, 0);"
        );

        // Pulsing effect
        ScaleTransition btnPulse = new ScaleTransition(Duration.seconds(1.5), btn);
        btnPulse.setFromX(1.0);
        btnPulse.setFromY(1.0);
        btnPulse.setToX(1.03);
        btnPulse.setToY(1.03);
        btnPulse.setCycleCount(Animation.INDEFINITE);
        btnPulse.setAutoReverse(true);
        btnPulse.play();

        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #FFE55C, #FFB84D, #FFA500);" +
                            "-fx-text-fill: #1a0f2e;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 4;" +
                            "-fx-border-radius: 15;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.9), 25, 0.7, 0, 0);"
            );
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, #FFD700, #FFA500, #FF8C00);" +
                            "-fx-text-fill: #1a0f2e;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-color: white;" +
                            "-fx-border-width: 3;" +
                            "-fx-border-radius: 15;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.6), 15, 0.5, 0, 0);"
            );
        });

        return btn;
    }

    private Button createActionButton(String text, String color1, String color2) {
        Button btn = new Button(text);
        btn.setPrefWidth(300);
        btn.setPrefHeight(46);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        btn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + ");" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, " + color1 + ", 15, 0.5, 0, 0);"
        );

        btn.setOnMouseEntered(e -> {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + ");" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-color: #FFD700;" +
                            "-fx-border-width: 4;" +
                            "-fx-border-radius: 15;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, " + color1 + ", 25, 0.7, 0, 0);"
            );
            btn.setScaleX(1.05);
            btn.setScaleY(1.05);
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle(
                    "-fx-background-color: linear-gradient(to bottom, " + color1 + ", " + color2 + ");" +
                            "-fx-text-fill: white;" +
                            "-fx-background-radius: 15;" +
                            "-fx-border-color: #FFD700;" +
                            "-fx-border-width: 3;" +
                            "-fx-border-radius: 15;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, " + color1 + ", 15, 0.5, 0, 0);"
            );
            btn.setScaleX(1.0);
            btn.setScaleY(1.0);
        });

        return btn;
    }

    private VBox createEnhancedInputBox(String labelText, String placeholder) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#FFD700"));

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefWidth(340);
        field.setPrefHeight(36);
        field.setFont(Font.font("Georgia", 13));
        field.setStyle(
                "-fx-background-color: rgba(26, 26, 46, 0.95);" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: white;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-padding: 8;"
        );

        field.setOnMouseEntered(e -> {
            field.setStyle(
                    "-fx-background-color: rgba(36, 36, 56, 0.95);" +
                            "-fx-text-fill: white;" +
                            "-fx-prompt-text-fill: white;" +
                            "-fx-border-color: #FFA500;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 8;" +
                            "-fx-effect: dropshadow(gaussian, rgba(255, 165, 0, 0.4), 10, 0.3, 0, 0);"
            );
        });

        field.setOnMouseExited(e -> {
            field.setStyle(
                    "-fx-background-color: rgba(26, 26, 46, 0.95);" +
                            "-fx-text-fill: white;" +
                            "-fx-prompt-text-fill: white;" +
                            "-fx-border-color: #FFD700;" +
                            "-fx-border-width: 2;" +
                            "-fx-border-radius: 10;" +
                            "-fx-background-radius: 10;" +
                            "-fx-padding: 8;"
            );
        });

        HBox fieldBox = new HBox(field);
        fieldBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(label, fieldBox);
        return box;
    }

    private VBox createEnhancedComboBox(String labelText, String[] items) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        label.setTextFill(Color.web("#FFD700"));

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(items);
        combo.setValue(items[0]);
        combo.setPrefWidth(200);
        combo.setPrefHeight(34);
        combo.setStyle(
                "-fx-background-color: rgba(26, 26, 46, 0.95);" +
                        "-fx-font-family: Georgia;" +
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );

        // Style the dropdown list items to have white text
        combo.setButtonCell(new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: transparent;");
                }
            }
        });

        combo.setCellFactory(lv -> new javafx.scene.control.ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.WHITE);
                    setStyle("-fx-background-color: rgba(26, 26, 46, 0.95); -fx-padding: 5px;");
                }
            }
        });

        HBox comboBox = new HBox(combo);
        comboBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(label, comboBox);
        return box;
    }

    private void updatePreviewAnimation(String faceSelection, String hatSelection, String staffSelection) {
        // Parse the selections to get the actual values
        String face = parseFace(faceSelection);
        String hat = parseHat(hatSelection);
        String staff = parseStaff(staffSelection);

        // Create a temporary customization object with current selections
        PlayerCustomization tempCustomization = new PlayerCustomization(face, hat, "blue", staff, "Preview");

        String path = tempCustomization.getFaceImagePath();

        try {
            InputStream in = getClass().getResourceAsStream(path);
            Image img = new Image(Objects.requireNonNull(in,
                    "Image not found at: " + path));

            // Apply to preview ImageView
            if (previewIcon != null) previewIcon.setImage(img);

            // Update preview hat position and image (DYNAMIC positioning)
            if (previewHatIcon != null) {
                // Dynamic offset based on hat type
                double yOffset = "pointy_hat".equals(hat) ? -1.15 :
                                "hood".equals(hat) ? -0.35 : -0.7;

                // Unbind and rebind with new offset to update position dynamically
                previewHatIcon.translateYProperty().unbind();
                previewHatIcon.translateYProperty().bind(previewCircle.radiusProperty().multiply(yOffset));
                setHatImage(previewHatIcon, tempCustomization.getHatImagePath());

                // Hood: between circle and face
                if ("hood".equals(hat)) {
                    previewHatIcon.toBack();
                    previewCircle.toBack();
                } else {
                    previewHatIcon.toFront();
                }
            }

            // Update preview staff image
            if (previewStaffIcon != null) {
                setStaffImage(previewStaffIcon, tempCustomization.getStaffImagePath());
            }

        } catch (Exception e) {
        }
    }

    private String[] getRobeColorGradient(String robe) {
        if (robe == null) robe = "blue";

        if (robe.contains("blue") || robe.contains("Blue")) {
            return new String[]{"#4169E1", "#2E5CB8", "#1E3A8A"};
        } else if (robe.contains("red") || robe.contains("Red")) {
            return new String[]{"#DC143C", "#B22222", "#8B0000"};
        } else if (robe.contains("purple") || robe.contains("Purple")) {
            return new String[]{"#9370DB", "#7B68EE", "#6A5ACD"};
        } else if (robe.contains("green") || robe.contains("Green")) {
            return new String[]{"#32CD32", "#228B22", "#006400"};
        } else if (robe.contains("black") || robe.contains("Black")) {
            return new String[]{"#4A4A4A", "#2F2F2F", "#1A1A1A"};
        } else if (robe.contains("white") || robe.contains("White")) {
            return new String[]{"#F0F0F0", "#D3D3D3", "#A9A9A9"};
        } else {
            return new String[]{"#4169E1", "#2E5CB8", "#1E3A8A"};
        }
    }

    private String convertToRGBA(String hex) {
        double alpha = 0.15;
        Color color = Color.web(hex);
        int r = (int) (color.getRed() * 255);
        int g = (int) (color.getGreen() * 255);
        int b = (int) (color.getBlue() * 255);
        return String.format("rgba(%d, %d, %d, %.2f)", r, g, b, alpha);
    }

    private void updatePreviewColor(String robe) {
        String[] colors = getRobeColorGradient(robe);
        previewCircle.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(colors[0])), new Stop(0.5, Color.web(colors[1])), new Stop(1, Color.web(colors[2]))));
        previewCircle.setEffect(createGlowEffect(Color.web(colors[0]), 20, 0.5));
    }

    private String parseFace(String value) {
        if (value.contains("Rugged") || value.contains("Warrior")) return "RuggedWarrior";
        if (value.contains("Elder") || value.contains("Wise")) return "WiseElder";
        if (value.contains("Young") || value.contains("Prodigy")) return "YoungProdigy";
        return "WiseElder";  // default
    }

    private String parseHat(String value) {
        if (value.contains("Pointy")) return "pointy_hat";
        if (value.contains("Wide")) return "wide_brim_hat";
        if (value.contains("Crown")) return "crown";
        if (value.contains("Hood")) return "hood";
        return "top_hat";
    }

    private String parseRobe(String value) {
        if (value.contains("Blue") || value.contains("Azure")) return "blue";
        if (value.contains("Red") || value.contains("Crimson")) return "red";
        if (value.contains("Purple") || value.contains("Royal")) return "purple";
        if (value.contains("Green") || value.contains("Emerald")) return "green";
        if (value.contains("Black") || value.contains("Midnight")) return "black";
        return "white";
    }

    private String parseStaff(String value) {
        if (value.contains("Wood") || value.contains("Oak")) return "wooden_staff";
        if (value.contains("Crystal")) return "crystal_staff";
        if (value.contains("Bone")) return "bone_staff";
        return "gold_staff";
    }

    // Card management

    public void addCardToHand(SpellCard card) {
        VBox cardBox = createCardUI(card);
        handPane.getChildren().add(cardBox);

        // Faster, smoother card appearance animation
        cardBox.setScaleX(0.7);
        cardBox.setScaleY(0.7);
        cardBox.setOpacity(0);

        ScaleTransition appear = new ScaleTransition(Duration.millis(150), cardBox);
        appear.setToX(1);
        appear.setToY(1);

        FadeTransition fade = new FadeTransition(Duration.millis(150), cardBox);
        fade.setToValue(1);

        ParallelTransition animation = new ParallelTransition(appear, fade);
        animation.play();
    }

    private VBox createCardUI(SpellCard card) {
        VBox cardBox = new VBox(8);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(10));
        cardBox.setPrefWidth(135);
        cardBox.setPrefHeight(190);

        String cardColor = getCardColor(card.getName());
        cardBox.setStyle(
                "-fx-background-color: linear-gradient(to bottom, " + cardColor + ", #1a1a2e);" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        StackPane imagePane = new StackPane();
        Circle imageBg = new Circle(32);
        imageBg.setFill(Color.web("#1a1a2e"));
        imageBg.setStroke(Color.web(cardColor));
        imageBg.setStrokeWidth(2);

        Label imageLabel = new Label(getSpellIcon(card.getName()));
        imageLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 42));
        imageLabel.setTextFill(Color.WHITE);

        imagePane.getChildren().addAll(imageBg, imageLabel);

        Label nameLabel = new Label(card.getName());
        nameLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 12));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-alignment: center;");

        HBox manaBox = new HBox(5);
        manaBox.setAlignment(Pos.CENTER);

        Label manaText = new Label("MANA:");
        manaText.setFont(Font.font("Georgia", FontWeight.BOLD, 11));
        manaText.setTextFill(Color.CYAN);

        Label manaLabel = new Label(String.valueOf(card.getSpell().getManaCost()));
        manaLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        manaLabel.setTextFill(Color.CYAN);

        manaBox.getChildren().addAll(manaText, manaLabel);

        String description = getSpellDescription(card.getName());
        Tooltip tooltip = new Tooltip(description);
        tooltip.setFont(Font.font("Georgia", 12));
        tooltip.setStyle(
                "-fx-background-color: rgba(42, 26, 74, 0.95);" +
                        "-fx-text-fill: white;" +
                        "-fx-border-color: gold;" +
                        "-fx-border-width: 2;" +
                        "-fx-padding: 10;"
        );
        Tooltip.install(cardBox, tooltip);

        cardBox.getChildren().addAll(imagePane, nameLabel, manaBox);

        DropShadow glow = new DropShadow();
        glow.setColor(Color.GOLD);
        glow.setRadius(20);
        glow.setSpread(0.5);

        // Smooth hover animation
        ScaleTransition hoverGrow = new ScaleTransition(Duration.millis(100), cardBox);
        hoverGrow.setToX(1.05);
        hoverGrow.setToY(1.05);

        ScaleTransition hoverShrink = new ScaleTransition(Duration.millis(100), cardBox);
        hoverShrink.setToX(1.0);
        hoverShrink.setToY(1.0);

        cardBox.setOnMouseEntered(e -> {
            if (!gameEnded) {
                cardBox.setEffect(glow);
                hoverGrow.playFromStart();
            }
        });

        cardBox.setOnMouseExited(e -> {
            cardBox.setEffect(null);
            hoverShrink.playFromStart();
        });

        cardBox.setOnMouseClicked(e -> {
            if (!gameEnded && gc.getPlayer().hasMp(card.getSpell().getManaCost())) {
                playSpellCastAnimation(card.getName());
                gc.castSpell(card.getName());
            }
        });

        return cardBox;
    }

    private String getCardColor(String spellName) {
        switch (spellName) {
            case "Fireball":
            case "Meteor":
                return "#FF4500";
            case "Ice Blast":
            case "Shield":
                return "#4169E1";
            case "Lightning":
            case "Thunderbolt":
                return "#FFD700";
            case "Heal":
            case "Regeneration":
                return "#32CD32";
            case "Poison Cloud":
            case "Curse":
                return "#9370DB";
            case "Drain":
                return "#8B008B";
            default:
                return "#4169E1";
        }
    }

    private String getSpellIcon(String spellName) {
        switch (spellName) {
            case "Fireball":
                return "F";
            case "Ice Blast":
                return "I";
            case "Lightning":
                return "L";
            case "Heal":
                return "H";
            case "Poison Cloud":
                return "P";
            case "Drain":
                return "D";
            case "Shield":
                return "S";
            case "Meteor":
                return "M";
            case "Regeneration":
                return "R";
            case "Thunderbolt":
                return "T";
            case "Curse":
                return "C";
            default:
                return "?";
        }
    }

    private String getSpellDescription(String spellName) {
        switch (spellName) {
            case "Fireball":
                return "FIREBALL\n\nDeal 10 damage and inflict Burn.\n\nBurn: 3 damage per turn for 3 turns.\n\n\"A classic spell of destruction!\"";
            case "Ice Blast":
                return "ICE BLAST\n\nDeal 15 damage and inflict Freeze.\n\nFreeze: Reduces enemy mana by 1 per turn for 2 turns.\n\n\"Chill your enemies to the bone!\"";
            case "Lightning":
                return "LIGHTNING\n\nDeal 25 pure damage.\n\nNo status effects.\n\n\"Raw elemental power!\"";
            case "Heal":
                return "HEAL\n\nRestore 20 HP instantly.\n\n\"The power of restoration!\"";
            case "Poison Cloud":
                return "POISON CLOUD\n\nDeal 5 damage and inflict Poison.\n\nPoison: 4 damage per turn for 4 turns.\n\n\"Let them suffer slowly...\"";
            case "Drain":
                return "DRAIN\n\nDeal 12 damage and heal yourself for 12 HP.\n\n\"Steal their life force!\"";
            case "Shield":
                return "SHIELD\n\nAbsorb up to 15 damage.\n\nLasts until depleted.\n\n\"Protection from harm!\"";
            case "Meteor":
                return "METEOR\n\nDeal 35 massive damage and inflict Burn.\n\nBurn: 3 damage per turn for 2 turns.\n\n\"Ultimate destruction!\"";
            case "Regeneration":
                return "REGENERATION\n\nHeal 5 HP per turn for 4 turns.\n\nTotal: 20 HP over time.\n\n\"Sustained recovery!\"";
            case "Thunderbolt":
                return "THUNDERBOLT\n\nDeal 18 damage and inflict Stun.\n\nStun: Disrupts enemy mana for 1 turn.\n\n\"Strike with lightning!\"";
            case "Curse":
                return "CURSE\n\nDeal 8 damage and inflict Weaken.\n\nWeaken: Reduces effectiveness for 3 turns.\n\n\"Curse your foe!\"";
            default:
                return "A magical spell!";
        }
    }

    public void refreshHandDisplay() {
        Player p = gc.getPlayer();
        if (p != null && p.getHand() != null) {
            int currentHandSize = p.getHand().size();
            // Only refresh if hand size changed
            if (currentHandSize != lastHandSize) {
                handPane.getChildren().clear();
                int cardCount = Math.min(5, currentHandSize);
                for (int i = 0; i < cardCount; i++) {
                    addCardToHand(p.getHand().get(i));
                }
                lastHandSize = currentHandSize;
            }
        }
    }

    public void refreshUI() {
        Player p = gc.getPlayer();
        Enemy e = gc.getEnemy();

        if (p != null && e != null) {
            playerHpLabel.setText(String.valueOf(p.getHp()));
            playerHpBar.setProgress(p.getHp() / 100.0);

            enemyHpLabel.setText(String.valueOf(e.getHp()));
            enemyHpBar.setProgress(e.getHp() / 100.0);

            playerMpLabel.setText(String.valueOf(p.getMp()));
            playerMpBar.setProgress(Math.min(p.getMp() / 10.0, 1.0));

            enemyMpLabel.setText(String.valueOf(e.getMp()));
            enemyMpBar.setProgress(Math.min(e.getMp() / 10.0, 1.0));

            refreshHandDisplay();
            checkGameEnd();
        }
    }

    private void checkGameEnd() {
        if (gameEnded) return;

        Player p = gc.getPlayer();
        Enemy e = gc.getEnemy();

        if (p.getHp() <= 0) {
            gameEnded = true;
            showDefeatScreen();
        } else if (e.getHp() <= 0) {
            gameEnded = true;
            showVictoryScreen();
        }
    }

    public void updateLog(String logText) {
        logArea.setText(logText);
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    // Observer Pattern - auto-update when game changes
    @Override
    public void update() {
        refreshUI();
    }

    // Animations

    private void playFadeIn(StackPane root) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void playTurnTransitionAnimation() {
        Rectangle flash = new Rectangle();
        flash.setWidth(primaryStage.getScene().getWidth());
        flash.setHeight(primaryStage.getScene().getHeight());
        flash.setFill(Color.WHITE);
        flash.setOpacity(0);
        animationPane.getChildren().add(flash);

        FadeTransition flashIn = new FadeTransition(Duration.millis(100), flash);
        flashIn.setToValue(0.3);

        FadeTransition flashOut = new FadeTransition(Duration.millis(200), flash);
        flashOut.setToValue(0);

        SequentialTransition seq = new SequentialTransition(flashIn, flashOut);
        seq.setOnFinished(e -> animationPane.getChildren().remove(flash));
        seq.play();
    }

    private void playSpellCastAnimation(String spellName) {
        Circle effect = new Circle(25);
        effect.setFill(Color.web(getCardColor(spellName), 0.6));
        effect.setCenterX(primaryStage.getScene().getWidth() / 2);
        effect.setCenterY(primaryStage.getScene().getHeight() / 2);

        animationPane.getChildren().add(effect);

        ScaleTransition expand = new ScaleTransition(Duration.millis(250), effect);
        expand.setToX(2.5);
        expand.setToY(2.5);

        FadeTransition fade = new FadeTransition(Duration.millis(250), effect);
        fade.setToValue(0);

        ParallelTransition parallel = new ParallelTransition(expand, fade);
        parallel.setOnFinished(e -> animationPane.getChildren().remove(effect));
        parallel.play();
    }

    // Image overlay helpers

    private ImageView addOverlay(StackPane container, Circle iconCircle, double scale, Pos alignment, double xOffset, double yOffset) {
        ImageView view = new ImageView();
        view.setPreserveRatio(true);
        view.fitWidthProperty().bind(iconCircle.radiusProperty().multiply(2 * scale));
        StackPane.setAlignment(view, alignment);
        if (xOffset != 0) view.translateXProperty().bind(iconCircle.radiusProperty().multiply(xOffset));
        if (yOffset != 0) view.translateYProperty().bind(iconCircle.radiusProperty().multiply(yOffset));
        container.getChildren().add(view);
        return view;
    }

    private ImageView addHatOverlay(StackPane container, Circle iconCircle, double scale, double yOffset) {
        return addOverlay(container, iconCircle, scale, Pos.TOP_CENTER, 0, yOffset);
    }

    private ImageView addStaffOverlay(StackPane container, Circle iconCircle, double scale, double xOffset, double yOffset) {
        return addOverlay(container, iconCircle, scale, Pos.CENTER_RIGHT, xOffset, yOffset);
    }

    private boolean loadImage(ImageView view, String path) {
        if (view == null || path == null || path.isEmpty()) {
            if (view != null) view.setImage(null);
            return false;
        }
        try (InputStream in = getClass().getResourceAsStream(path)) {
            if (in == null) {
                view.setImage(null);
                return false;
            }
            view.setImage(new Image(in));
            return true;
        } catch (Exception e) {
            view.setImage(null);
            return false;
        }
    }

    private void setHatImage(ImageView hatView, String path) {
        loadImage(hatView, path);
    }

    private void setStaffImage(ImageView staffView, String path) {
        loadImage(staffView, path);
    }
}
