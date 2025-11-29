package com.wizbiz.wizard_card_game;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

/**
 * Professional Wizardly UI with Cohesive Design & Win/Loss Screens
 */
public class GameUI extends Application {

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
    private HBox handPane = new HBox(15);
    private Button endTurnBtn = new Button("END TURN");

    private Pane animationPane;
    private Pane starsPane;
    private Stage primaryStage;

    private PlayerCustomization playerCustomization;
    private EnemyCustomization enemyCustomization;

    // Preview elements
    private Label previewIcon;
    private Circle previewCircle;

    // Game state
    private boolean gameEnded = false;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        showCustomizationScreen();
    }

    private void showCustomizationScreen() {
        StackPane root = new StackPane();

        // Enhanced gradient background with more depth
        BackgroundFill bgFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0a0612")),
                        new Stop(0.3, Color.web("#1a0f2e")),
                        new Stop(0.6, Color.web("#2d1b4e")),
                        new Stop(1, Color.web("#1e0f3d"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        root.setBackground(new Background(bgFill));

        // Animated stars background
        Pane starsPane = createStarsEffect();

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        VBox customizationBox = new VBox(20);
        customizationBox.setAlignment(Pos.CENTER);
        customizationBox.setPadding(new Insets(40, 30, 40, 30));

        // Enhanced title with subtitle
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);

        Label title = new Label("✨ WIZARD CREATION CHAMBER ✨");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 40));
        title.setTextFill(Color.web("#FFD700"));

        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.web("#FFA500"));
        titleGlow.setRadius(30);
        titleGlow.setSpread(0.9);
        title.setEffect(titleGlow);

        Label subtitle = new Label("Forge Your Destiny");
        subtitle.setFont(Font.font("Georgia", FontWeight.NORMAL, 16));
        subtitle.setTextFill(Color.web("#C4A47C"));
        subtitle.setOpacity(0.8);

        // Title animations
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), title);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        FadeTransition subtitleFade = new FadeTransition(Duration.seconds(3), subtitle);
        subtitleFade.setFromValue(0.6);
        subtitleFade.setToValue(1.0);
        subtitleFade.setCycleCount(Animation.INDEFINITE);
        subtitleFade.setAutoReverse(true);
        subtitleFade.play();

        titleBox.getChildren().addAll(title, subtitle);

        // Main customization panel with enhanced styling
        VBox customPanel = new VBox(18);
        customPanel.setPadding(new Insets(30));
        customPanel.setAlignment(Pos.CENTER);
        customPanel.setMaxWidth(750);
        customPanel.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(42, 26, 74, 0.9), rgba(28, 17, 51, 0.9));" +
                        "-fx-border-color: linear-gradient(to right, #FFD700, #FFA500, #FFD700);" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.4), 25, 0.3, 0, 0);"
        );

        // Enhanced preview section with decorative elements
        VBox previewSection = new VBox(10);
        previewSection.setAlignment(Pos.CENTER);

        Label previewLabel = new Label("⚡ YOUR WIZARD ⚡");
        previewLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        previewLabel.setTextFill(Color.web("#FFD700"));

        StackPane previewPane = new StackPane();
        previewPane.setPrefSize(140, 140);

        // Outer decorative ring
        Circle outerRing = new Circle(65);
        outerRing.setFill(Color.TRANSPARENT);
        outerRing.setStroke(Color.web("#FFD700"));
        outerRing.setStrokeWidth(2);
        outerRing.setOpacity(0.5);

        // Rotating animation for outer ring
        RotateTransition ringRotate = new RotateTransition(Duration.seconds(8), outerRing);
        ringRotate.setByAngle(360);
        ringRotate.setCycleCount(Animation.INDEFINITE);
        ringRotate.play();

        // Inner preview circle with gradient
        previewCircle = new Circle(50);
        previewCircle.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#4169E1")),
                new Stop(0.5, Color.web("#2E5CB8")),
                new Stop(1, Color.web("#1E3A8A"))
        ));

        DropShadow circleGlow = new DropShadow();
        circleGlow.setColor(Color.web("#4169E1"));
        circleGlow.setRadius(20);
        circleGlow.setSpread(0.5);
        previewCircle.setEffect(circleGlow);

        previewIcon = new Label("🧙‍♂️");
        previewIcon.setFont(Font.font(60));

        // Floating animation for icon
        TranslateTransition float1 = new TranslateTransition(Duration.seconds(2), previewIcon);
        float1.setByY(-5);
        float1.setCycleCount(Animation.INDEFINITE);
        float1.setAutoReverse(true);
        float1.play();

        previewPane.getChildren().addAll(outerRing, previewCircle, previewIcon);
        previewSection.getChildren().addAll(previewLabel, previewPane);

        // Separator line
        Rectangle separator1 = createSeparator();

        // Name input with enhanced styling
        VBox nameBox = createEnhancedInputBox("🎭 Wizard Name", "Enter your legendary name...");
        TextField nameField = (TextField) ((HBox) nameBox.getChildren().get(1)).getChildren().get(0);

        // Separator line
        Rectangle separator2 = createSeparator();

        // Two-column grid for selections
        GridPane selectionsGrid = new GridPane();
        selectionsGrid.setHgap(20);
        selectionsGrid.setVgap(15);
        selectionsGrid.setAlignment(Pos.CENTER);

        // Face selection
        VBox faceBox = createEnhancedComboBox("👤 Face", new String[]{
                "Rugged Warrior 👨", "Wise Elder 👴", "Young Prodigy 👦"
        });
        ComboBox<String> faceCombo = (ComboBox<String>) ((HBox) faceBox.getChildren().get(1)).getChildren().get(0);

        // Hat selection
        VBox hatBox = createEnhancedComboBox("🎩 Headwear", new String[]{
                "Pointy Hat 🎩", "Wide Brim 👒", "Crown 👑", "Hood 🧢", "Top Hat 🎓"
        });
        ComboBox<String> hatCombo = (ComboBox<String>) ((HBox) hatBox.getChildren().get(1)).getChildren().get(0);

        // Robe color
        VBox robeBox = createEnhancedComboBox("👘 Robe", new String[]{
                "Azure Blue 💙", "Crimson Red ❤️", "Royal Purple 💜",
                "Emerald Green 💚", "Midnight Black 🖤", "Pure White 🤍"
        });
        ComboBox<String> robeCombo = (ComboBox<String>) ((HBox) robeBox.getChildren().get(1)).getChildren().get(0);

        // Staff type
        VBox staffBox = createEnhancedComboBox("🪄 Staff", new String[]{
                "Oak Wood 🪵", "Crystal 💎", "Bone 🦴", "Gold ⭐"
        });
        ComboBox<String> staffCombo = (ComboBox<String>) ((HBox) staffBox.getChildren().get(1)).getChildren().get(0);

        selectionsGrid.add(faceBox, 0, 0);
        selectionsGrid.add(hatBox, 1, 0);
        selectionsGrid.add(robeBox, 0, 1);
        selectionsGrid.add(staffBox, 1, 1);

        // Update preview on selection change
        faceCombo.setOnAction(e -> updatePreviewAnimation());
        hatCombo.setOnAction(e -> updatePreviewAnimation());
        robeCombo.setOnAction(e -> updatePreviewColor(robeCombo.getValue()));
        staffCombo.setOnAction(e -> updatePreviewAnimation());

        // Separator line
        Rectangle separator3 = createSeparator();

        // Enhanced start button with effects
        Button startBtn = new Button("⚔️ ENTER THE ARENA ⚔️");
        startBtn.setPrefWidth(320);
        startBtn.setPrefHeight(55);
        startBtn.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        startBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #FFD700, #FFA500, #FF8C00);" +
                        "-fx-text-fill: #1a0f2e;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: white;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.6), 15, 0.5, 0, 0);"
        );

        // Pulsing effect on button
        ScaleTransition btnPulse = new ScaleTransition(Duration.seconds(1.5), startBtn);
        btnPulse.setFromX(1.0);
        btnPulse.setFromY(1.0);
        btnPulse.setToX(1.03);
        btnPulse.setToY(1.03);
        btnPulse.setCycleCount(Animation.INDEFINITE);
        btnPulse.setAutoReverse(true);
        btnPulse.play();

        startBtn.setOnMouseEntered(e -> {
            startBtn.setStyle(
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

        startBtn.setOnMouseExited(e -> {
            startBtn.setStyle(
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

        startBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                name = "Wizard";
            }

            String face = parseFace(faceCombo.getValue());
            String hat = parseHat(hatCombo.getValue());
            String robe = parseRobe(robeCombo.getValue());
            String staff = parseStaff(staffCombo.getValue());

            playerCustomization = new PlayerCustomization(face, hat, robe, staff, name);
            enemyCustomization = new EnemyCustomization(playerCustomization);

            startBattle();
        });

        // Footer text
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

        customizationBox.getChildren().addAll(titleBox, customPanel);
        scrollPane.setContent(customizationBox);
        root.getChildren().addAll(starsPane, scrollPane);

        Scene scene = new Scene(root, 900, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("⚔️ Wizard Character Creation ⚔️");
        primaryStage.show();

        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private Pane createStarsEffect() {
        Pane starsPane = new Pane();
        starsPane.setMouseTransparent(true);

        // Create twinkling stars
        for (int i = 0; i < 50; i++) {
            Circle star = new Circle(1 + Math.random() * 2);
            star.setFill(Color.WHITE);
            star.setOpacity(0.3 + Math.random() * 0.7);
            star.setCenterX(Math.random() * 900);
            star.setCenterY(Math.random() * 800);

            // Twinkling animation
            FadeTransition twinkle = new FadeTransition(
                    Duration.seconds(1 + Math.random() * 3),
                    star
            );
            twinkle.setFromValue(0.2);
            twinkle.setToValue(1.0);
            twinkle.setCycleCount(Animation.INDEFINITE);
            twinkle.setAutoReverse(true);
            twinkle.setDelay(Duration.seconds(Math.random() * 2));
            twinkle.play();

            starsPane.getChildren().add(star);
        }

        return starsPane;
    }

    private Pane createVictoryStars() {
        Pane starsPane = new Pane();
        starsPane.setMouseTransparent(true);

        // Create twinkling stars for victory screen
        for (int i = 0; i < 50; i++) {
            Circle star = new Circle(1 + Math.random() * 2);
            star.setFill(Color.web("#FFD700"));
            star.setOpacity(0.3 + Math.random() * 0.7);
            star.setCenterX(Math.random() * 900);
            star.setCenterY(Math.random() * 750);

            // Twinkling animation
            FadeTransition twinkle = new FadeTransition(
                    Duration.seconds(1 + Math.random() * 3),
                    star
            );
            twinkle.setFromValue(0.2);
            twinkle.setToValue(1.0);
            twinkle.setCycleCount(Animation.INDEFINITE);
            twinkle.setAutoReverse(true);
            twinkle.setDelay(Duration.seconds(Math.random() * 2));
            twinkle.play();

            starsPane.getChildren().add(star);
        }

        return starsPane;
    }

    private Pane createDefeatStars() {
        Pane starsPane = new Pane();
        starsPane.setMouseTransparent(true);

        // Create twinkling stars for defeat screen
        for (int i = 0; i < 50; i++) {
            Circle star = new Circle(1 + Math.random() * 2);
            star.setFill(Color.web("#DC143C"));
            star.setOpacity(0.3 + Math.random() * 0.7);
            star.setCenterX(Math.random() * 900);
            star.setCenterY(Math.random() * 750);

            // Twinkling animation
            FadeTransition twinkle = new FadeTransition(
                    Duration.seconds(1 + Math.random() * 3),
                    star
            );
            twinkle.setFromValue(0.2);
            twinkle.setToValue(1.0);
            twinkle.setCycleCount(Animation.INDEFINITE);
            twinkle.setAutoReverse(true);
            twinkle.setDelay(Duration.seconds(Math.random() * 2));
            twinkle.play();

            starsPane.getChildren().add(star);
        }

        return starsPane;
    }

    private Rectangle createSeparator() {
        Rectangle separator = new Rectangle(400, 2);
        separator.setFill(new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.TRANSPARENT),
                new Stop(0.5, Color.web("#FFD700", 0.5)),
                new Stop(1, Color.TRANSPARENT)
        ));
        return separator;
    }

    private void updatePreviewAnimation() {
        ScaleTransition scale = new ScaleTransition(Duration.millis(200), previewIcon);
        scale.setFromX(1.0);
        scale.setFromY(1.0);
        scale.setToX(1.2);
        scale.setToY(1.2);
        scale.setAutoReverse(true);
        scale.setCycleCount(2);
        scale.play();
    }

    private void updatePreviewColor(String robe) {
        String color1, color2, color3;

        if (robe.contains("Blue")) {
            color1 = "#4169E1"; color2 = "#2E5CB8"; color3 = "#1E3A8A";
        } else if (robe.contains("Red")) {
            color1 = "#DC143C"; color2 = "#B22222"; color3 = "#8B0000";
        } else if (robe.contains("Purple")) {
            color1 = "#9370DB"; color2 = "#7B68EE"; color3 = "#6A5ACD";
        } else if (robe.contains("Green")) {
            color1 = "#32CD32"; color2 = "#228B22"; color3 = "#006400";
        } else if (robe.contains("Black")) {
            color1 = "#4A4A4A"; color2 = "#2F2F2F"; color3 = "#1A1A1A";
        } else {
            color1 = "#F0F0F0"; color2 = "#D3D3D3"; color3 = "#A9A9A9";
        }

        previewCircle.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(color1)),
                new Stop(0.5, Color.web(color2)),
                new Stop(1, Color.web(color3))
        ));

        DropShadow circleGlow = new DropShadow();
        circleGlow.setColor(Color.web(color1));
        circleGlow.setRadius(20);
        circleGlow.setSpread(0.5);
        previewCircle.setEffect(circleGlow);

        updatePreviewAnimation();
    }

    private VBox createEnhancedInputBox(String labelText, String placeholder) {
        VBox box = new VBox(8);
        box.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 15));
        label.setTextFill(Color.web("#FFD700"));

        TextField field = new TextField();
        field.setPromptText(placeholder);
        field.setPrefWidth(400);
        field.setPrefHeight(40);
        field.setFont(Font.font("Georgia", 14));
        field.setStyle(
                "-fx-background-color: rgba(26, 26, 46, 0.95);" +
                        "-fx-text-fill: white;" +
                        "-fx-prompt-text-fill: #9370DB;" +
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
                            "-fx-prompt-text-fill: #9370DB;" +
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
                            "-fx-prompt-text-fill: #9370DB;" +
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
        label.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        label.setTextFill(Color.web("#FFD700"));

        ComboBox<String> combo = new ComboBox<>();
        combo.getItems().addAll(items);
        combo.setValue(items[0]);
        combo.setPrefWidth(240);
        combo.setPrefHeight(38);
        combo.setStyle(
                "-fx-background-color: rgba(26, 26, 46, 0.95);" +
                        "-fx-font-family: Georgia;" +
                        "-fx-font-size: 13px;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 2;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;"
        );

        HBox comboBox = new HBox(combo);
        comboBox.setAlignment(Pos.CENTER);

        box.getChildren().addAll(label, comboBox);
        return box;
    }

    private String parseFace(String value) {
        if (value.contains("1") || value.contains("Rugged")) return "face1";
        if (value.contains("2") || value.contains("Elder")) return "face2";
        return "face3";
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

    private void startBattle() {
        gameEnded = false; // Reset game state

        StackPane root = new StackPane();

        // Same deep gradient as customization screen
        BackgroundFill bgFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0a0612")),
                        new Stop(0.3, Color.web("#1a0f2e")),
                        new Stop(0.6, Color.web("#2d1b4e")),
                        new Stop(1, Color.web("#1e0f3d"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        root.setBackground(new Background(bgFill));

        // Stars for battle screen too!
        starsPane = createStarsEffect();

        BorderPane mainLayout = new BorderPane();
        mainLayout.setPadding(new Insets(20));

        animationPane = new Pane();
        animationPane.setMouseTransparent(true);

        VBox topSection = createTopSection();
        VBox centerSection = createCenterSection();
        VBox bottomSection = createBottomSection();

        mainLayout.setTop(topSection);
        mainLayout.setCenter(centerSection);
        mainLayout.setBottom(bottomSection);

        root.getChildren().addAll(starsPane, mainLayout, animationPane);

        endTurnBtn.setOnAction(e -> {
            if (!gameEnded) {
                gc.endTurn();
                playTurnTransitionAnimation();
            }
        });

        Scene scene = new Scene(root, 900, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("⚔️ " + playerCustomization.getPlayerName() + " vs " + enemyCustomization.getEnemyName() + " ⚔️");

        gc.setUI(this);

        Player customPlayer = new Player(playerCustomization);
        Enemy customEnemy = new Enemy(enemyCustomization);
        gc.startGameWithCustomizations(customPlayer, customEnemy);
        refreshUI();

        playOpeningAnimation(root);
    }

    private VBox createTopSection() {
        VBox top = new VBox(15);
        top.setAlignment(Pos.CENTER);

        // Title with subtitle (cohesive with customization)
        VBox titleBox = new VBox(5);
        titleBox.setAlignment(Pos.CENTER);

        Label title = new Label("⚔️ ARENA OF MYSTIC COMBAT ⚔️");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 36));
        title.setTextFill(Color.web("#FFD700"));

        DropShadow titleGlow = new DropShadow();
        titleGlow.setColor(Color.web("#FFA500"));
        titleGlow.setRadius(25);
        titleGlow.setSpread(0.8);
        title.setEffect(titleGlow);

        Label subtitle = new Label("Battle for Magical Supremacy");
        subtitle.setFont(Font.font("Georgia", FontPosture.ITALIC, 14));
        subtitle.setTextFill(Color.web("#C4A47C"));
        subtitle.setOpacity(0.7);

        // Pulsing animation for title
        ScaleTransition pulse = new ScaleTransition(Duration.seconds(2), title);
        pulse.setFromX(1.0);
        pulse.setFromY(1.0);
        pulse.setToX(1.05);
        pulse.setToY(1.05);
        pulse.setCycleCount(Animation.INDEFINITE);
        pulse.setAutoReverse(true);
        pulse.play();

        // Subtitle fade
        FadeTransition subtitleFade = new FadeTransition(Duration.seconds(3), subtitle);
        subtitleFade.setFromValue(0.5);
        subtitleFade.setToValue(0.9);
        subtitleFade.setCycleCount(Animation.INDEFINITE);
        subtitleFade.setAutoReverse(true);
        subtitleFade.play();

        titleBox.getChildren().addAll(title, subtitle);

        // Battle arena with enhanced styling
        HBox arena = createBattleArena();

        top.getChildren().addAll(titleBox, arena);
        return top;
    }

    private HBox createBattleArena() {
        HBox arena = new HBox(40);
        arena.setAlignment(Pos.CENTER);
        arena.setPadding(new Insets(20));

        // Enhanced styling matching customization panel
        arena.setStyle(
                "-fx-background-color: linear-gradient(to bottom, rgba(42, 26, 74, 0.9), rgba(28, 17, 51, 0.9));" +
                        "-fx-border-color: linear-gradient(to right, #FFD700, #FFA500, #FFD700);" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 20;" +
                        "-fx-background-radius: 20;" +
                        "-fx-effect: dropshadow(gaussian, rgba(255, 215, 0, 0.4), 25, 0.3, 0, 0);"
        );

        VBox playerBox = createCharacterBox(true);

        // Enhanced VS section
        VBox vsBox = new VBox(8);
        vsBox.setAlignment(Pos.CENTER);

        Label vsLabel = new Label("⚔️\nVS\n⚔️");
        vsLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        vsLabel.setTextFill(Color.web("#FFD700"));
        vsLabel.setStyle("-fx-alignment: center;");

        DropShadow vsGlow = new DropShadow();
        vsGlow.setColor(Color.web("#FFA500"));
        vsGlow.setRadius(15);
        vsGlow.setSpread(0.6);
        vsLabel.setEffect(vsGlow);

        // Rotating animation for VS
        RotateTransition rotate = new RotateTransition(Duration.seconds(4), vsLabel);
        rotate.setByAngle(360);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play();

        vsBox.getChildren().add(vsLabel);

        VBox enemyBox = createCharacterBox(false);

        arena.getChildren().addAll(playerBox, vsBox, enemyBox);
        return arena;
    }

    private VBox createCharacterBox(boolean isPlayer) {
        VBox box = new VBox(12);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setPrefWidth(270);

        String borderColor = isPlayer ? "#4169E1" : "#DC143C";
        String bgColor1 = isPlayer ? "rgba(65, 105, 225, 0.15)" : "rgba(220, 20, 60, 0.15)";
        String bgColor2 = isPlayer ? "rgba(30, 58, 138, 0.15)" : "rgba(127, 29, 29, 0.15)";

        box.setStyle(
                "-fx-background-color: linear-gradient(to bottom, " + bgColor1 + ", " + bgColor2 + ");" +
                        "-fx-border-color: " + borderColor + ";" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-background-radius: 15;" +
                        "-fx-effect: dropshadow(gaussian, " + borderColor + ", 15, 0.3, 0, 0);"
        );

        // Character icon with decorative ring (like preview)
        StackPane iconPane = new StackPane();

        Circle outerRing = new Circle(55);
        outerRing.setFill(Color.TRANSPARENT);
        outerRing.setStroke(Color.web(borderColor));
        outerRing.setStrokeWidth(2);
        outerRing.setOpacity(0.5);

        Circle iconCircle = new Circle(45);
        iconCircle.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web(isPlayer ? "#4169E1" : "#DC143C")),
                new Stop(0.5, Color.web(isPlayer ? "#2E5CB8" : "#B22222")),
                new Stop(1, Color.web(isPlayer ? "#1E3A8A" : "#7F1D1D"))
        ));

        DropShadow iconGlow = new DropShadow();
        iconGlow.setColor(Color.web(borderColor));
        iconGlow.setRadius(20);
        iconGlow.setSpread(0.5);
        iconCircle.setEffect(iconGlow);

        Label icon = new Label(isPlayer ? "🧙‍♂️" : "🧙‍♀️");
        icon.setFont(Font.font(55));

        // Floating animation
        TranslateTransition floatAnim = new TranslateTransition(Duration.seconds(2.5), icon);
        floatAnim.setByY(-4);
        floatAnim.setCycleCount(Animation.INDEFINITE);
        floatAnim.setAutoReverse(true);
        floatAnim.play();

        iconPane.getChildren().addAll(outerRing, iconCircle, icon);

        String displayName = isPlayer ?
                playerCustomization.getPlayerName() :
                enemyCustomization.getEnemyName();

        Label nameLabel = new Label(displayName.toUpperCase());
        nameLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 17));
        nameLabel.setTextFill(Color.web("#FFD700"));
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-alignment: center;");
        nameLabel.setMaxWidth(230);

        DropShadow nameGlow = new DropShadow();
        nameGlow.setColor(Color.web(borderColor));
        nameGlow.setRadius(10);
        nameGlow.setSpread(0.4);
        nameLabel.setEffect(nameGlow);

        VBox hpBox = createStatBar(
                isPlayer ? playerHpLabel : enemyHpLabel,
                isPlayer ? playerHpBar : enemyHpBar,
                "❤️ HP", "#DC143C"
        );

        VBox mpBox = createStatBar(
                isPlayer ? playerMpLabel : enemyMpLabel,
                isPlayer ? playerMpBar : enemyMpBar,
                "💎 MANA", "#4169E1"
        );

        box.getChildren().addAll(iconPane, nameLabel, hpBox, mpBox);
        return box;
    }

    private VBox createStatBar(Label valueLabel, ProgressBar bar, String name, String color) {
        VBox statBox = new VBox(6);
        statBox.setAlignment(Pos.CENTER);

        Label statLabel = new Label(name);
        statLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        statLabel.setTextFill(Color.web("#FFD700"));

        valueLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        valueLabel.setTextFill(Color.WHITE);

        bar.setPrefWidth(200);
        bar.setPrefHeight(22);
        bar.setStyle(
                "-fx-accent: " + color + ";" +
                        "-fx-control-inner-background: rgba(26, 26, 46, 0.8);" +
                        "-fx-border-color: " + color + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 5;"
        );

        HBox labelBox = new HBox(10);
        labelBox.setAlignment(Pos.CENTER);
        labelBox.getChildren().addAll(statLabel, valueLabel);

        statBox.getChildren().addAll(labelBox, bar);
        return statBox;
    }

    private VBox createCenterSection() {
        VBox center = new VBox(12);
        center.setPadding(new Insets(20, 0, 20, 0));
        center.setAlignment(Pos.CENTER);

        Label handLabel = new Label("✨ YOUR SPELLBOOK ✨");
        handLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 20));
        handLabel.setTextFill(Color.web("#FFD700"));

        DropShadow handGlow = new DropShadow();
        handGlow.setColor(Color.web("#FFA500"));
        handGlow.setRadius(15);
        handGlow.setSpread(0.5);
        handLabel.setEffect(handGlow);

        // Enhanced hand container
        handPane.setAlignment(Pos.CENTER);
        handPane.setPadding(new Insets(15));
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
        VBox bottom = new VBox(12);

        Label logLabel = new Label("📜 BATTLE CHRONICLE");
        logLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        logLabel.setTextFill(Color.web("#FFD700"));

        DropShadow logGlow = new DropShadow();
        logGlow.setColor(Color.web("#FFA500"));
        logGlow.setRadius(10);
        logGlow.setSpread(0.4);
        logLabel.setEffect(logGlow);

        logArea.setEditable(false);
        logArea.setPrefHeight(100);
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
        endTurnBtn.setPrefWidth(280);
        endTurnBtn.setPrefHeight(52);
        endTurnBtn.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        endTurnBtn.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #DC143C, #8B0000);" +
                        "-fx-text-fill: white;" +
                        "-fx-background-radius: 15;" +
                        "-fx-border-color: #FFD700;" +
                        "-fx-border-width: 3;" +
                        "-fx-border-radius: 15;" +
                        "-fx-cursor: hand;" +
                        "-fx-effect: dropshadow(gaussian, rgba(220, 20, 60, 0.5), 15, 0.4, 0, 0);"
        );

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
                endTurnBtn.setStyle(
                        "-fx-background-color: linear-gradient(to bottom, #DC143C, #8B0000);" +
                                "-fx-text-fill: white;" +
                                "-fx-background-radius: 15;" +
                                "-fx-border-color: #FFD700;" +
                                "-fx-border-width: 3;" +
                                "-fx-border-radius: 15;" +
                                "-fx-cursor: hand;" +
                                "-fx-effect: dropshadow(gaussian, rgba(220, 20, 60, 0.5), 15, 0.4, 0, 0);"
                );
            }
        });

        HBox btnBox = new HBox(endTurnBtn);
        btnBox.setAlignment(Pos.CENTER);

        bottom.getChildren().addAll(logLabel, logArea, btnBox);
        return bottom;
    }

    public void addCardToHand(SpellCard card) {
        VBox cardBox = createCardUI(card);
        handPane.getChildren().add(cardBox);

        cardBox.setScaleX(0);
        cardBox.setScaleY(0);
        ScaleTransition appear = new ScaleTransition(Duration.millis(300), cardBox);
        appear.setToX(1);
        appear.setToY(1);
        appear.play();
    }

    private VBox createCardUI(SpellCard card) {
        VBox cardBox = new VBox(8);
        cardBox.setAlignment(Pos.CENTER);
        cardBox.setPadding(new Insets(10));
        cardBox.setPrefWidth(140);
        cardBox.setPrefHeight(200);

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
        Circle imageBg = new Circle(35);
        imageBg.setFill(Color.web("#1a1a2e"));

        Label imageLabel = new Label(getSpellIcon(card.getName()));
        imageLabel.setFont(Font.font(40));

        imagePane.getChildren().addAll(imageBg, imageLabel);

        Label nameLabel = new Label(card.getName());
        nameLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 13));
        nameLabel.setTextFill(Color.WHITE);
        nameLabel.setWrapText(true);
        nameLabel.setStyle("-fx-alignment: center;");

        HBox manaBox = new HBox(5);
        manaBox.setAlignment(Pos.CENTER);

        Label manaIcon = new Label("💎");
        manaIcon.setFont(Font.font(14));

        Label manaLabel = new Label(String.valueOf(card.getSpell().getManaCost()));
        manaLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 14));
        manaLabel.setTextFill(Color.CYAN);

        manaBox.getChildren().addAll(manaIcon, manaLabel);

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

        cardBox.setOnMouseEntered(e -> {
            if (!gameEnded) {
                cardBox.setEffect(glow);
                cardBox.setScaleX(1.1);
                cardBox.setScaleY(1.1);
            }
        });

        cardBox.setOnMouseExited(e -> {
            cardBox.setEffect(null);
            cardBox.setScaleX(1.0);
            cardBox.setScaleY(1.0);
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
            case "Fireball": return "🔥";
            case "Ice Blast": return "❄️";
            case "Lightning": return "⚡";
            case "Heal": return "💚";
            case "Poison Cloud": return "☠️";
            case "Drain": return "🩸";
            case "Shield": return "🛡️";
            case "Meteor": return "☄️";
            case "Regeneration": return "✨";
            case "Thunderbolt": return "⚡";
            case "Curse": return "👻";
            default: return "✨";
        }
    }

    private String getSpellDescription(String spellName) {
        switch (spellName) {
            case "Fireball":
                return "💥 FIREBALL\n\nDeal 10 damage and inflict Burn.\n\nBurn: 3 damage per turn for 3 turns.\n\n\"A classic spell of destruction!\"";
            case "Ice Blast":
                return "❄️ ICE BLAST\n\nDeal 15 damage and inflict Freeze.\n\nFreeze: Reduces enemy mana by 1 per turn for 2 turns.\n\n\"Chill your enemies to the bone!\"";
            case "Lightning":
                return "⚡ LIGHTNING\n\nDeal 25 pure damage.\n\nNo status effects.\n\n\"Raw elemental power!\"";
            case "Heal":
                return "💚 HEAL\n\nRestore 20 HP instantly.\n\n\"The power of restoration!\"";
            case "Poison Cloud":
                return "☠️ POISON CLOUD\n\nDeal 5 damage and inflict Poison.\n\nPoison: 4 damage per turn for 4 turns.\n\n\"Let them suffer slowly...\"";
            case "Drain":
                return "🩸 DRAIN\n\nDeal 12 damage and heal yourself for 12 HP.\n\n\"Steal their life force!\"";
            case "Shield":
                return "🛡️ SHIELD\n\nAbsorb up to 15 damage.\n\nLasts until depleted.\n\n\"Protection from harm!\"";
            case "Meteor":
                return "☄️ METEOR\n\nDeal 35 massive damage and inflict Burn.\n\nBurn: 3 damage per turn for 2 turns.\n\n\"Ultimate destruction!\"";
            case "Regeneration":
                return "✨ REGENERATION\n\nHeal 5 HP per turn for 4 turns.\n\nTotal: 20 HP over time.\n\n\"Sustained recovery!\"";
            case "Thunderbolt":
                return "⚡ THUNDERBOLT\n\nDeal 18 damage and inflict Stun.\n\nStun: Disrupts enemy mana for 1 turn.\n\n\"Strike with lightning!\"";
            case "Curse":
                return "👻 CURSE\n\nDeal 8 damage and inflict Weaken.\n\nWeaken: Reduces effectiveness for 3 turns.\n\n\"Curse your foe!\"";
            default:
                return "✨ A magical spell!";
        }
    }

    public void refreshHandDisplay() {
        handPane.getChildren().clear();
        Player p = gc.getPlayer();
        if (p != null && p.getHand() != null) {
            int cardCount = Math.min(5, p.getHand().size());
            for (int i = 0; i < cardCount; i++) {
                addCardToHand(p.getHand().get(i));
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

            // CHECK FOR GAME END
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

    private void showVictoryScreen() {
        StackPane root = new StackPane();

        // Victory gradient (gold/green tones)
        BackgroundFill bgFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#0a1e0a")),
                        new Stop(0.3, Color.web("#1a4d1a")),
                        new Stop(0.6, Color.web("#2d6e2d")),
                        new Stop(1, Color.web("#1e3d1e"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        root.setBackground(new Background(bgFill));

        // Victory stars (create new ones for victory screen)
        Pane victoryStars = createVictoryStars();

        VBox victoryBox = new VBox(25);
        victoryBox.setAlignment(Pos.CENTER);
        victoryBox.setPadding(new Insets(40));

        // Victory title
        Label victoryTitle = new Label("🏆 GLORIOUS VICTORY! 🏆");
        victoryTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 50));
        victoryTitle.setTextFill(Color.web("#FFD700"));

        DropShadow victoryGlow = new DropShadow();
        victoryGlow.setColor(Color.web("#32CD32"));
        victoryGlow.setRadius(40);
        victoryGlow.setSpread(1.0);
        victoryTitle.setEffect(victoryGlow);

        // Pulsing animation
        ScaleTransition victoryPulse = new ScaleTransition(Duration.seconds(1.5), victoryTitle);
        victoryPulse.setFromX(1.0);
        victoryPulse.setFromY(1.0);
        victoryPulse.setToX(1.1);
        victoryPulse.setToY(1.1);
        victoryPulse.setCycleCount(Animation.INDEFINITE);
        victoryPulse.setAutoReverse(true);
        victoryPulse.play();

        // Subtitle
        Label victorySubtitle = new Label("You have proven your magical prowess!");
        victorySubtitle.setFont(Font.font("Georgia", FontPosture.ITALIC, 20));
        victorySubtitle.setTextFill(Color.web("#90EE90"));

        // Victory panel
        VBox victoryPanel = new VBox(20);
        victoryPanel.setPadding(new Insets(40));
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
        Label winnerIcon = new Label("👑");
        winnerIcon.setFont(Font.font(100));

        RotateTransition iconRotate = new RotateTransition(Duration.seconds(3), winnerIcon);
        iconRotate.setByAngle(360);
        iconRotate.setCycleCount(Animation.INDEFINITE);
        iconRotate.play();

        // Winner name
        Label winnerName = new Label(playerCustomization.getPlayerName().toUpperCase());
        winnerName.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        winnerName.setTextFill(Color.web("#FFD700"));

        // Victory message
        Label victoryMessage = new Label("⚔️ Defeated " + enemyCustomization.getEnemyName() + " ⚔️");
        victoryMessage.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        victoryMessage.setTextFill(Color.web("#90EE90"));

        Rectangle separator = createSeparator();

        // Stats
        VBox statsBox = new VBox(10);
        statsBox.setAlignment(Pos.CENTER);

        Label hpRemaining = new Label("💚 HP Remaining: " + gc.getPlayer().getHp());
        hpRemaining.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        hpRemaining.setTextFill(Color.WHITE);

        Label mpRemaining = new Label("💎 Mana Remaining: " + gc.getPlayer().getMp());
        mpRemaining.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        mpRemaining.setTextFill(Color.WHITE);

        statsBox.getChildren().addAll(hpRemaining, mpRemaining);

        // Play again button
        Button playAgainBtn = createStyledButton("🔄 CHALLENGE ANOTHER WIZARD", "#32CD32", "#228B22");
        playAgainBtn.setOnAction(e -> showCustomizationScreen());

        // Return to creation button
        Button exitBtn = createStyledButton("🧙‍♂️ RETURN TO CREATION", "#4169E1", "#1E3A8A");
        exitBtn.setOnAction(e -> showCustomizationScreen());

        victoryPanel.getChildren().addAll(
                winnerIcon,
                winnerName,
                victoryMessage,
                separator,
                statsBox,
                playAgainBtn,
                exitBtn
        );

        victoryBox.getChildren().addAll(victoryTitle, victorySubtitle, victoryPanel);
        root.getChildren().addAll(victoryStars, victoryBox);

        Scene scene = new Scene(root, 900, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("🏆 VICTORY! 🏆");

        // Fade in
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void showDefeatScreen() {
        StackPane root = new StackPane();

        // Defeat gradient (dark red/purple tones)
        BackgroundFill bgFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#1e0a0a")),
                        new Stop(0.3, Color.web("#4d1a1a")),
                        new Stop(0.6, Color.web("#6e2d2d")),
                        new Stop(1, Color.web("#3d1e1e"))
                ),
                CornerRadii.EMPTY,
                Insets.EMPTY
        );
        root.setBackground(new Background(bgFill));

        // Defeat stars (create new ones for defeat screen)
        Pane defeatStars = createDefeatStars();

        VBox defeatBox = new VBox(25);
        defeatBox.setAlignment(Pos.CENTER);
        defeatBox.setPadding(new Insets(40));

        // Defeat title
        Label defeatTitle = new Label("💀 DEFEAT 💀");
        defeatTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 50));
        defeatTitle.setTextFill(Color.web("#DC143C"));

        DropShadow defeatGlow = new DropShadow();
        defeatGlow.setColor(Color.web("#8B0000"));
        defeatGlow.setRadius(40);
        defeatGlow.setSpread(1.0);
        defeatTitle.setEffect(defeatGlow);

        // Pulsing animation
        ScaleTransition defeatPulse = new ScaleTransition(Duration.seconds(1.5), defeatTitle);
        defeatPulse.setFromX(1.0);
        defeatPulse.setFromY(1.0);
        defeatPulse.setToX(1.08);
        defeatPulse.setToY(1.08);
        defeatPulse.setCycleCount(Animation.INDEFINITE);
        defeatPulse.setAutoReverse(true);
        defeatPulse.play();

        // Subtitle
        Label defeatSubtitle = new Label("Your magical journey ends here...");
        defeatSubtitle.setFont(Font.font("Georgia", FontPosture.ITALIC, 20));
        defeatSubtitle.setTextFill(Color.web("#FF6B6B"));

        // Defeat panel
        VBox defeatPanel = new VBox(20);
        defeatPanel.setPadding(new Insets(40));
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
        Label defeatIcon = new Label("⚰️");
        defeatIcon.setFont(Font.font(100));

        TranslateTransition iconFloat = new TranslateTransition(Duration.seconds(2), defeatIcon);
        iconFloat.setByY(-8);
        iconFloat.setCycleCount(Animation.INDEFINITE);
        iconFloat.setAutoReverse(true);
        iconFloat.play();

        // Defeated name
        Label defeatedName = new Label(playerCustomization.getPlayerName().toUpperCase());
        defeatedName.setFont(Font.font("Georgia", FontWeight.BOLD, 32));
        defeatedName.setTextFill(Color.web("#DC143C"));

        // Defeat message
        Label defeatMessage = new Label("⚔️ Vanquished by " + enemyCustomization.getEnemyName() + " ⚔️");
        defeatMessage.setFont(Font.font("Georgia", FontWeight.BOLD, 22));
        defeatMessage.setTextFill(Color.web("#FF6B6B"));

        Rectangle separator = createSeparator();

        // Motivational quote
        Label quote = new Label("\"Even the greatest wizards must fall before they can rise again\"");
        quote.setFont(Font.font("Georgia", FontPosture.ITALIC, 16));
        quote.setTextFill(Color.web("#FFD700"));
        quote.setWrapText(true);
        quote.setStyle("-fx-alignment: center;");
        quote.setMaxWidth(500);

        // Try again button
        Button tryAgainBtn = createStyledButton("⚔️ SEEK REDEMPTION", "#DC143C", "#8B0000");
        tryAgainBtn.setOnAction(e -> showCustomizationScreen());

        // Return to creation button
        Button exitBtn = createStyledButton("🧙‍♂️ RETURN TO CREATION", "#4A4A4A", "#2F2F2F");
        exitBtn.setOnAction(e -> showCustomizationScreen());

        defeatPanel.getChildren().addAll(
                defeatIcon,
                defeatedName,
                defeatMessage,
                separator,
                quote,
                tryAgainBtn,
                exitBtn
        );

        defeatBox.getChildren().addAll(defeatTitle, defeatSubtitle, defeatPanel);
        root.getChildren().addAll(defeatStars, defeatBox);

        Scene scene = new Scene(root, 900, 750);
        primaryStage.setScene(scene);
        primaryStage.setTitle("💀 Defeat 💀");

        // Fade in
        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private Button createStyledButton(String text, String color1, String color2) {
        Button btn = new Button(text);
        btn.setPrefWidth(350);
        btn.setPrefHeight(55);
        btn.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
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

    public void updateLog(String logText) {
        logArea.setText(logText);
        logArea.setScrollTop(Double.MAX_VALUE);
    }

    private void playOpeningAnimation(StackPane root) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), root);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    private void playTurnTransitionAnimation() {
        Rectangle flash = new Rectangle(900, 750);
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
        Circle effect = new Circle(30);
        effect.setFill(Color.web(getCardColor(spellName), 0.7));
        effect.setCenterX(450);
        effect.setCenterY(300);

        animationPane.getChildren().add(effect);

        ScaleTransition expand = new ScaleTransition(Duration.millis(500), effect);
        expand.setToX(3);
        expand.setToY(3);

        FadeTransition fade = new FadeTransition(Duration.millis(500), effect);
        fade.setToValue(0);

        ParallelTransition parallel = new ParallelTransition(expand, fade);
        parallel.setOnFinished(e -> animationPane.getChildren().remove(effect));
        parallel.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

