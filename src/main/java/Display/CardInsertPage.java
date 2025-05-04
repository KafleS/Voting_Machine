package Display;

import Cards.CardType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import simple.CardReader;
import Managers.AdminManager;
import simple.SDCard;
import simple.LLSensor;
import simple.TPSensor;
import simple.Battery;

import java.util.List;

public class CardInsertPage {
    private final Scene scene;
    private final CardReader cardReader;
    private static boolean votingOpen = false;

    public CardInsertPage(Stage stage) {
        this.cardReader = new CardReader();

        Label prompt = new Label("Insert Card (e.g., A12345678 or V12345678):");
        TextField cardInput = new TextField();
        cardInput.setPromptText("Enter card ID...");
        Button submitButton = new Button("Submit");

        submitButton.setOnAction(e -> {
            String cardId = cardInput.getText().trim();

            if (cardReader.failure()) {
                prompt.setText("Card reader malfunction. Please contact technician.");
                return;
            }

            if (cardReader.isCardPresent()) {
                prompt.setText("A card is already inserted. Please eject it first.");
                return;
            }

            try {
                cardReader.insertCard(cardId);
                CardType type = cardReader.getCardType();

                if (type == CardType.UNKNOWN) {
                    cardReader.ejectCard();
                    prompt.setText("Invalid card. Must start with A/V followed by 8 digits.");
                    return;
                }

                System.out.println("Card inserted: " + cardReader.readCard());

                switch (type) {
                    case ADMIN:
                        VBox adminLayout = new VBox(20);
                        adminLayout.setAlignment(Pos.CENTER);
                        Label adminLabel = new Label("Admin Menu");
                        Button controlVotingBtn = new Button();
                        Label statusLabel = new Label();
                        Button ejectButton = new Button("Eject Card");

                        LLSensor latch = new LLSensor();
                        TPSensor tpSensor = new TPSensor(latch);
                        SDCard sdCard = new SDCard(0, SDCard.Operation.read);

                        AdminManager adminManager = new AdminManager(
                                sdCard,
                                latch,
                                tpSensor,
                                new Battery()
                        );

                        if (!votingOpen) {
                            controlVotingBtn.setText("Open Voting Session");
                            controlVotingBtn.setOnAction(ev -> {
                                boolean started = adminManager.startVotingSession();
                                if (started) {
                                    votingOpen = true;
                                    statusLabel.setText("\u2705 Voting session has started.");
                                    controlVotingBtn.setText("Close Voting Session");
                                } else {
                                    statusLabel.setText("\u274C Could not start session. Check hardware/ballot.");
                                }
                            });
                        } else {
                            controlVotingBtn.setText("Close Voting Session");
                            controlVotingBtn.setOnAction(ev -> {
                                votingOpen = false;
                                statusLabel.setText("\u2705 Voting session closed.");
                                controlVotingBtn.setDisable(true);
                            });
                        }

                        ejectButton.setOnAction(ev -> {
                            try {
                                cardReader.ejectCard();
                                stage.setScene(new CardInsertPage(stage).getScene());
                            } catch (Exception ex) {
                                statusLabel.setText("Eject failed: " + ex.getMessage());
                            }
                        });

                        adminLayout.getChildren().addAll(adminLabel, controlVotingBtn, statusLabel, ejectButton);
                        Scene adminScene = new Scene(adminLayout, 600, 800);
                        stage.setScene(adminScene);
                        break;

                    case VOTER:
                        if (votingOpen) {
                            List<Template> templates = DisplayMain.getLoadedTemplates();
                            if (templates != null && !templates.isEmpty()) {
                                displayVoterTemplates(stage, templates, 0);
                            } else {
                                prompt.setText("Ballot data not available yet.");
                            }
                        } else {
                            prompt.setText("Voting is not open yet. Ask admin.");
                            cardReader.ejectCard();
                        }
                        break;
                }

            } catch (Exception ex) {
                prompt.setText("Error: " + ex.getMessage());
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

        voterPage.getNextButton().setOnAction(e -> {
            if (index < templates.size() - 1) {
                displayVoterTemplates(stage, templates, index + 1);
            } else {
                VBox finishLayout = new VBox(20);
                finishLayout.setAlignment(Pos.CENTER);
                Label doneLabel = new Label("✅ Your vote has been recorded.");
                Button ejectButton = new Button("Eject Card");

                ejectButton.setOnAction(ev -> {
                    try {
                        cardReader.ejectCard();
                        stage.setScene(new CardInsertPage(stage).getScene());
                    } catch (Exception ex) {
                        doneLabel.setText("Eject failed: " + ex.getMessage());
                    }
                });

                finishLayout.getChildren().addAll(doneLabel, ejectButton);
                Scene finishScene = new Scene(finishLayout, 600, 800);
                stage.setScene(finishScene);
            }
        });

        stage.setScene(voterPage.getScene());
    }
}
