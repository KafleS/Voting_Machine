package Display;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;

public class CardInsertPage {
    private final Scene scene;

    public CardInsertPage(Stage stage) {
        Label prompt = new Label("Insert Card (e.g., admin, voter):");
        TextField cardInput = new TextField();
        cardInput.setPromptText("Enter card type...");
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            String input = cardInput.getText().trim().toLowerCase();

            if (input.equals("admin")) {
                AdminPage adminPage = new AdminPage(stage);
                stage.setScene(adminPage.getScene());

            } else if (input.equals("voter")) {
                if (DisplayMain.isVotingOpen()) {
                    List<Template> templates = DisplayMain.getLoadedTemplates();
                    if (templates != null && !templates.isEmpty()) {
                        displayVoterTemplates(stage, templates, 0);
                    } else {
                        prompt.setText("Ballot data not available yet.");
                    }
                } else {
                    prompt.setText("Voting is not open yet. Ask admin.");
                }

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

    private void displayVoterTemplates(Stage stage, List<Template> templates, int index) {
        Template current = templates.get(index);
        VoterPage voterPage = new VoterPage(current);

        voterPage.getPreviousButton().setOnAction(e -> {
            if (index > 0) {
                displayVoterTemplates(stage, templates, index - 1);
            } else {
                stage.setScene(new CardInsertPage(stage).getScene());
            }
        });

//        voterPage.getSubmitButton().setOnAction(e -> {
//            System.out.println("Submitted vote for: " + current.getTitle());
//            stage.setScene(new CardInsertPage(stage).getScene());
//        });

        voterPage.getNextButton().setOnAction(e -> {
            if (index < templates.size() - 1) {
                displayVoterTemplates(stage, templates, index + 1);
            }
        });

        stage.setScene(voterPage.getScene());
    }
}