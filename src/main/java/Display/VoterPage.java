package Display;

import  simple.SDCard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import javafx.geometry.Insets;
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
import java.util.ArrayList;
import java.util.List;

public class VoterPage {
    private final Scene scene;
    private final Button previousButton;
    private final Button submitButton;
    private final Button nextButton;
    private static final java.util.Map<String, VoteData> allSelectedVotes = new java.util.HashMap<>();
    private final Template template;

    public VoterPage(Template t) {
        this.template = t;


        Label id = new Label(String.valueOf(t.getId()));
        id.setAlignment(Pos.CENTER);
        id.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        id.setMinHeight(40);
        id.setMaxHeight(40);
        id.setMaxWidth(600);

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
                VoteData vote = new VoteData(
                        String.valueOf(template.getId()),
                        template.getTitle(),
                        template.getDescription(),
                        option
                );
                allSelectedVotes.put(vote.id, vote);
                System.out.println("Saved: " + vote.id + " → " + vote.option);
                System.out.println("all selected votes: " + allSelectedVotes.keySet());
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

    private static class VoteData {
        String id;
        String title;
        String description;
        String option;

        VoteData(String id, String title, String description, String option) {
            this.id = id;
            this.title = title;
            this.description = description;
            this.option = option;
        }
    }

    private void setDimensionsOnButton(Button button) {
        button.setMinSize(200, 80);
        button.setMaxSize(200, 80);
    }



    private void submitVote() {
        if (allSelectedVotes.isEmpty()) {
            System.out.println("No selections made.");
            return;
        }

        ObjectMapper mapper = new ObjectMapper();

        try {
            // Read existing data
            SDCard readCard1 = new SDCard(1, SDCard.Operation.read);
            SDCard readCard2 = new SDCard(2, SDCard.Operation.read);

            ArrayNode root1 = mapper.createArrayNode();
            String json1 = String.join("\n", readCard1.read());
            if (!json1.isBlank()) {
                root1 = (ArrayNode) mapper.readTree(json1);
            }

            ArrayNode root2 = mapper.createArrayNode();
            String json2 = String.join("\n", readCard2.read());
            if (!json2.isBlank()) {
                root2 = (ArrayNode) mapper.readTree(json2);
            }

            // Remove previous entries by ID
            for (String id : allSelectedVotes.keySet()) {
                for (int i = root1.size() - 1; i >= 0; i--) {
                    if (root1.get(i).get("id").asText().equals(id)) {
                        root1.remove(i);
                    }
                }
                for (int i = root2.size() - 1; i >= 0; i--) {
                    if (root2.get(i).get("id").asText().equals(id)) {
                        root2.remove(i);
                    }
                }
            }

            for (String id : allSelectedVotes.keySet()) {
                VoteData voteData = allSelectedVotes.get(id);
                ObjectNode vote = mapper.createObjectNode();
                vote.put("id", voteData.id);
                vote.put("title", voteData.title);
                vote.put("description", voteData.description);
                vote.put("selectedOption", voteData.option);

                root1.add(vote);
                root2.add(vote.deepCopy());
            }

            // Write to files
            new SDCard(1, SDCard.Operation.overwrite)
                    .overwrite(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root1));
            new SDCard(2, SDCard.Operation.overwrite)
                    .overwrite(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root2));

            System.out.println(" All votes saved.");
            allSelectedVotes.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() { return scene; }
    public Button getPreviousButton() { return previousButton; }
    public Button getSubmitButton() { return submitButton; }
    public Button getNextButton() { return nextButton; }
}


