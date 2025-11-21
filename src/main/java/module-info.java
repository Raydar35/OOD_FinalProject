module com.wizbiz.wizard_card_game {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;

    opens com.wizbiz.wizard_card_game to javafx.fxml;
    exports com.wizbiz.wizard_card_game;
    exports com.wizbiz.wizard_card_game.spells;
    opens com.wizbiz.wizard_card_game.spells to javafx.fxml;
}