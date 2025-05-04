package Display;

import BML.Ballot;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import simple.SDCard;

import java.util.List;

public class DisplayMain extends Application {
    private Display display;

    private static boolean isVotingOpen = false;
    private static Ballot loadedBallot;
    private static List<Template> loadedTemplates;

    public static void setVotingOpen(boolean open) {
        isVotingOpen = open;
    }

    public static boolean isVotingOpen() {
        return isVotingOpen;
    }

    public static Ballot getLoadedBallot() {
        return loadedBallot;
    }

    public static List<Template> getLoadedTemplates() {
        return loadedTemplates;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        CardInsertPage cardPage = new CardInsertPage(primaryStage);
        primaryStage.setTitle("Insert Card");
        primaryStage.setScene(cardPage.getScene());
        primaryStage.show();

        new Thread(() -> runDisplayLogic(primaryStage)).start();
    }

    private void runDisplayLogic(Stage stage) {
        try {
            display = new Display();

            SDCard sdCard = new SDCard(0, SDCard.Operation.read);
            String json = String.join("\n", sdCard.read());
            System.out.println("[SUCCESS] JSON read from SD card:");
            System.out.println(json);

            loadedBallot = new Ballot(json);
            loadedTemplates = TemplateFactory.fromBallot(loadedBallot);

            System.out.println("\n[INFO] Sending templates to screen for display...");
            for (Template t : loadedTemplates) {
                display.sendTemplate(t);
                while (!display.isReady()) {
                    Thread.sleep(1000);
                }
            }
            System.out.println("[DONE] All templates sent to screen.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}