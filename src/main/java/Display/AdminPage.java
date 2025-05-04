
package Display;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AdminPage {
    private final Scene scene;

    public AdminPage(Stage stage) {
        Button openVoting = new Button("Open Voting");
        Button backButton = new Button("Back");

        openVoting.setOnAction(e -> {
            DisplayMain.setVotingOpen(true);
            System.out.println("Voting is now OPEN.");
        });

        backButton.setOnAction(e -> {
            CardInsertPage cardPage = new CardInsertPage(stage);
            stage.setScene(cardPage.getScene());
        });

        VBox layout = new VBox(30, openVoting, backButton);
        layout.setMinSize(600, 800);
        layout.setAlignment(javafx.geometry.Pos.CENTER);

        this.scene = new Scene(layout);
    }

    public Scene getScene() {
        return scene;
    }
}