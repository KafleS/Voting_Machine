package Display;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class CardInsertPage {
    private final Scene scene;
    private final TextField cardInput;
    private final Button submitButton;

    public CardInsertPage(Runnable onValidCard) {
        Label prompt = new Label("Insert Card (e.g., admin, voter):");
        prompt.setStyle("-fx-font-size: 16px;");

        cardInput = new TextField();
        cardInput.setPromptText("Enter card type...");
        cardInput.setMaxWidth(300);

        submitButton = new Button("Submit");
        submitButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        submitButton.setOnAction(e -> {
            String input = cardInput.getText().trim().toLowerCase();
            if (input.equals("admin") || input.equals("voter")) {
                onValidCard.run(); // Load the rest of the UI
            } else {
                prompt.setText("Invalid card. Try 'admin' or 'voter'");
            }
        });

        VBox layout = new VBox(20, prompt, cardInput, submitButton);
        layout.setAlignment(Pos.CENTER);
        layout.setMinSize(600, 800);

        this.scene = new Scene(layout);
    }

    public Scene getScene() {
        return scene;
    }
}
