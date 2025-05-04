package Display;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class VoterPage {
    private final Scene scene;
    private final Button previousButton;
    private final Button submitButton;
    private final Button nextButton;

    public VoterPage(Template t) {
        // Title
        Label title = new Label(t.getTitle());
        title.setAlignment(Pos.CENTER);
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        title.setMinHeight(40);
        title.setMaxHeight(40);
        title.setMaxWidth(600);

        // Description
        Label description = new Label(t.getDescription());
        description.setAlignment(Pos.CENTER);
        description.setStyle("-fx-font-size: 18px;");
        description.setWrapText(true);
        description.setMinHeight(140);
        description.setMaxHeight(140);
        description.setPrefWidth(600);

        // Instructions
        Label instructions = new Label(t.getInstructions());
        instructions.setAlignment(Pos.CENTER);
        instructions.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        instructions.setMinHeight(40);
        instructions.setMaxHeight(40);
        instructions.setPrefWidth(600);

        // Options
        VBox optionsBox = new VBox();
        optionsBox.setMaxHeight(500);
        optionsBox.setPrefWidth(600);
        optionsBox.setAlignment(Pos.CENTER);

        for (String option : t.getQuestionData().getOptions()) {
            Button button = new Button(option);
            button.setMinSize(600, 100);
            button.setMaxSize(600, 100);
            button.setStyle("-fx-font-size: 18px;");
            optionsBox.getChildren().add(button);
        }

        // Bottom Buttons
        this.previousButton = new Button("← Previous");
        previousButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        setDimensionsOnButton(previousButton);

        this.submitButton = new Button("Submit ✔");
        submitButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        setDimensionsOnButton(submitButton);

        this.nextButton = new Button("Next →");
        nextButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        setDimensionsOnButton(nextButton);

        HBox navigationButtons = new HBox(0, previousButton, submitButton, nextButton);
        navigationButtons.setAlignment(Pos.BOTTOM_CENTER);

        // Set status of buttons
        previousButton.setDisable(!t.getPreviousButton().getActive());
        submitButton.setDisable(!t.getSubmitButton().getActive());
        nextButton.setDisable(!t.getNextButton().getActive());

        // Pad out options height
        Region optionSpacer = new Region();
        VBox.setVgrow(optionSpacer, Priority.ALWAYS);

        // Layout
        VBox mainContent = new VBox(0, title, description, instructions, optionsBox, optionSpacer, navigationButtons);
        //mainContent.setPadding(new Insets(15, 15, 25, 15));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setMaxWidth(600);
        mainContent.setMinHeight(800);
        mainContent.setMaxHeight(800);

        // init scene
        scene = new Scene(mainContent, 600, 800);
    }

    private void setDimensionsOnButton(Button button) {
        button.setMinSize(200, 80);
        button.setMaxSize(200, 80);
    }

    public Scene getScene() {return scene;}
    public Button getPreviousButton() {return previousButton;}
    public Button getSubmitButton() {return submitButton;}
    public Button getNextButton() {return nextButton;}
}
