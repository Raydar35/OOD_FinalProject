package com.wizbiz.wizard_card_game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 * CustomizationScreen - UI for player to customize their wizard appearance
 * Shows before the game starts
 */
public class CustomizationScreen {

    private Stage stage;
    private PlayerCustomization customization;
    private ImageView previewImage;

    // Customization options
    private ComboBox<String> faceSelector;
    private ComboBox<String> hatSelector;
    private ComboBox<String> robeColorSelector;
    private ComboBox<String> staffSelector;
    private TextField nameField;

    public CustomizationScreen(Stage stage) {
        this.stage = stage;
        this.customization = new PlayerCustomization();
    }

    /**
     * Show the customization screen
     * @param onComplete Callback when customization is complete
     */
    public void show(Runnable onComplete) {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #2a1a4a;"); // Dark purple background

        // Title
        Label title = new Label("Customize Your Wizard");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: gold;");

        // Preview area
        VBox previewBox = createPreviewArea();

        // Customization controls
        VBox controlsBox = createCustomizationControls();

        // Start button
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-font-size: 16px; -fx-padding: 10 30; " +
                "-fx-background-color: #4a90e2; -fx-text-fill: white;");
        startButton.setOnAction(e -> {
            // Save customization
            customization.setPlayerName(nameField.getText());
            customization.setFaceType(faceSelector.getValue());
            customization.setHatType(hatSelector.getValue());
            customization.setRobeColor(robeColorSelector.getValue());
            customization.setStaffType(staffSelector.getValue());

            // Proceed to game
            onComplete.run();
        });

        root.getChildren().addAll(title, previewBox, controlsBox, startButton);

        Scene scene = new Scene(root, 600, 700);
        stage.setScene(scene);
        stage.setTitle("Wizard Customization");
        stage.show();
    }

    private VBox createPreviewArea() {
        VBox previewBox = new VBox(10);
        previewBox.setAlignment(Pos.CENTER);
        previewBox.setStyle("-fx-border-color: gold; -fx-border-width: 2; " +
                "-fx-border-radius: 10; -fx-padding: 20; " +
                "-fx-background-color: #3a2a5a; -fx-background-radius: 10;");

        Label previewLabel = new Label("Preview");
        previewLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: gold;");

        // Preview image
        previewImage = new ImageView();
        previewImage.setFitWidth(200);
        previewImage.setFitHeight(200);
        previewImage.setPreserveRatio(true);

        // Load default preview
        updatePreview();

        previewBox.getChildren().addAll(previewLabel, previewImage);
        return previewBox;
    }

    private VBox createCustomizationControls() {
        VBox controlsBox = new VBox(10);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.setPadding(new Insets(10));

        // Name input
        HBox nameBox = new HBox(10);
        nameBox.setAlignment(Pos.CENTER);
        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        nameField = new TextField("Wizard");
        nameField.setPrefWidth(200);
        nameBox.getChildren().addAll(nameLabel, nameField);

        // Face selector (only 3 options now)
        HBox faceBox = createOptionRow("Face:",
                new String[]{"face1", "face2", "face3"});
        faceSelector = (ComboBox<String>) faceBox.getChildren().get(1);
        faceSelector.setOnAction(e -> updatePreview());

        // Hat selector
        HBox hatBox = createOptionRow("Hat:",
                new String[]{"pointy_hat", "wide_brim_hat", "crown", "hood", "top_hat"});
        hatSelector = (ComboBox<String>) hatBox.getChildren().get(1);
        hatSelector.setOnAction(e -> updatePreview());

        // Robe color selector
        HBox robeBox = createOptionRow("Robe Color:",
                new String[]{"blue", "red", "purple", "green", "black", "white"});
        robeColorSelector = (ComboBox<String>) robeBox.getChildren().get(1);
        robeColorSelector.setOnAction(e -> updatePreview());

        // Staff selector
        HBox staffBox = createOptionRow("Staff:",
                new String[]{"wooden_staff", "crystal_staff", "bone_staff", "gold_staff"});
        staffSelector = (ComboBox<String>) staffBox.getChildren().get(1);
        staffSelector.setOnAction(e -> updatePreview());

        controlsBox.getChildren().addAll(nameBox, faceBox, hatBox, robeBox, staffBox);
        return controlsBox;
    }

    private HBox createOptionRow(String labelText, String[] options) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER);

        Label label = new Label(labelText);
        label.setStyle("-fx-text-fill: white; -fx-font-size: 14px;");
        label.setPrefWidth(100);

        ComboBox<String> selector = new ComboBox<>();
        selector.getItems().addAll(options);
        selector.setValue(options[0]);
        selector.setPrefWidth(200);

        row.getChildren().addAll(label, selector);
        return row;
    }

    private void updatePreview() {
        // Try to load composite image first
        String face = faceSelector != null ? faceSelector.getValue() : "face1";
        String hat = hatSelector != null ? hatSelector.getValue() : "pointy_hat";
        String robe = robeColorSelector != null ? robeColorSelector.getValue() : "blue";
        String staff = staffSelector != null ? staffSelector.getValue() : "wooden_staff";

        String compositePath = "/images/avatars/" + face + "_" + hat + "_" +
                robe + "_" + staff + ".png";

        try {
            Image image = new Image(getClass().getResourceAsStream(compositePath));
            previewImage.setImage(image);
        } catch (Exception e) {
            // If composite doesn't exist, create layered view
            updateLayeredPreview(face, hat, robe, staff);
        }
    }

    private void updateLayeredPreview(String face, String hat, String robe, String staff) {
        // Create a stacked pane to layer images
        StackPane layeredView = new StackPane();

        try {
            // Load and stack images (bottom to top)
            ImageView robeImg = loadImageView("/images/robes/" + robe + "_robe.png", 200);
            ImageView faceImg = loadImageView("/images/faces/" + face + ".png", 200);
            ImageView hatImg = loadImageView("/images/hats/" + hat + ".png", 200);
            ImageView staffImg = loadImageView("/images/staffs/" + staff + ".png", 200);

            layeredView.getChildren().addAll(robeImg, faceImg, hatImg, staffImg);

            // Take snapshot and set as preview
            // (Or just show placeholder if images don't exist)
        } catch (Exception e) {
            // Show placeholder if images aren't available
            showPlaceholder();
        }
    }

    private ImageView loadImageView(String path, int size) {
        try {
            Image img = new Image(getClass().getResourceAsStream(path));
            ImageView imgView = new ImageView(img);
            imgView.setFitWidth(size);
            imgView.setFitHeight(size);
            imgView.setPreserveRatio(true);
            return imgView;
        } catch (Exception e) {
            return new ImageView(); // Empty if not found
        }
    }

    private void showPlaceholder() {
        // Show a placeholder text if images aren't loaded
        // You could also create a simple colored rectangle
        previewImage.setImage(null);
    }

    public PlayerCustomization getCustomization() {
        return customization;
    }
}