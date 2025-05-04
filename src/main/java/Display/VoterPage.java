package Display;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class VoterPage {
    private final Scene scene;
    private final Button previousButton;
    private final Button submitButton;
    private final Button nextButton;
    private final List<String> selectedOptions = new ArrayList<>();
    private final Template template;

    public VoterPage(Template t) {
        this.template = t;

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

            button.setOnAction(e -> {
                if (!selectedOptions.contains(option)) {
                    selectedOptions.add(option);
                }
            });

            optionsBox.getChildren().add(button);
        }

        // Bottom Buttons
        this.previousButton = new Button("\u2190 Previous");
        previousButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        setDimensionsOnButton(previousButton);

        this.submitButton = new Button("Submit \u2714");
        submitButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        setDimensionsOnButton(submitButton);

        this.nextButton = new Button("Next \u2192");
        nextButton.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        setDimensionsOnButton(nextButton);

        submitButton.setOnAction(e -> submitVote());

        HBox navigationButtons = new HBox(0, previousButton, submitButton, nextButton);
        navigationButtons.setAlignment(Pos.BOTTOM_CENTER);

        // Set status of buttons
        previousButton.setDisable(!t.getPreviousButton().getActive());
        submitButton.setDisable(!t.getSubmitButton().getActive());
        nextButton.setDisable(!t.getNextButton().getActive());

        Label confirmation = new Label("");
        confirmation.setStyle("-fx-font-size: 14px; -fx-text-fill: green;");
        submitButton.setOnAction(e -> {
            submitVote();
            confirmation.setText("Vote submitted successfully.");
        });

        // Pad out options height
        Region optionSpacer = new Region();
        VBox.setVgrow(optionSpacer, Priority.ALWAYS);

        // Layout
        VBox mainContent = new VBox(0, title, description, instructions, optionsBox, confirmation, optionSpacer,
                navigationButtons);
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



    private void submitVote() {
        if (selectedOptions.isEmpty()) {
            System.out.println("No selection made.");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        // Define both output files
        File file1 = new File("voter1.txt");
        File file2 = new File("voter2.txt");

        try {
            // Initialize or load existing data for both files
            ArrayNode root1 = file1.exists() && file1.length() > 0
                    ? (ArrayNode) mapper.readTree(file1)
                    : mapper.createArrayNode();

            ArrayNode root2 = file2.exists() && file2.length() > 0
                    ? (ArrayNode) mapper.readTree(file2)
                    : mapper.createArrayNode();

            // Create vote entry for each selected option
            for (String selected : selectedOptions) {
                ObjectNode vote = mapper.createObjectNode();
                vote.put("title", template.getTitle());
                vote.put("description", template.getDescription());
                vote.put("selectedOption", selected);

                root1.add(vote);
                root2.add(vote.deepCopy()); // deepCopy to avoid shared reference
            }

            // Save both files
            mapper.writerWithDefaultPrettyPrinter().writeValue(file1, root1);
            mapper.writerWithDefaultPrettyPrinter().writeValue(file2, root2);

            System.out.println("Votes recorded to voter1.txt and voter2.txt: " + selectedOptions);
            selectedOptions.clear(); // reset

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public Scene getScene() { return scene; }
    public Button getPreviousButton() { return previousButton; }
    public Button getSubmitButton() { return submitButton; }
    public Button getNextButton() { return nextButton; }
}
