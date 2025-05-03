package Display;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DisplayJavaFX extends Application {
    private Stage stage;
    private Template template;
    private Listener listener;
    private boolean failed = false;
    private boolean ready = true;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        // Step 1: Show card insert UI
        CardInsertPage cardPage = new CardInsertPage(() -> {
            // Step 2: After valid card, start listener thread
            new Thread(() -> {
                try (ServerSocket serverSocket = new ServerSocket(12345)) {
                    System.out.printf("Port opened @ %s. Waiting for connection...\n", 12345);
                    Socket socket = serverSocket.accept();
                    System.out.printf("Reader connected from %s.\n", socket.getLocalAddress());
                    listener = new Listener(socket, this);
                    System.out.println("Closing port...");
                    serverSocket.close();

                    new Thread(listener).start(); // Start listening for templates
                } catch (IOException e) {
                    System.out.println("Port closed");
                }
            }).start();
        });

        primaryStage.setScene(cardPage.getScene());
        primaryStage.setTitle("Insert Card");
        primaryStage.show();
    }

    public void receiveTemplate(Template template) {
        System.out.println("Display.Template received");
        this.template = template;
        ready = false;

        Platform.runLater(() -> {
            VotingMachinePage newPage = new VotingMachinePage(template);

            // Button actions
            newPage.getPreviousButton().setOnAction(event -> {
                template.getPreviousButton().pressButton();
                ready = true;
            });

            newPage.getSubmitButton().setOnAction(event -> {
                template.getSubmitButton().pressButton();
                ready = true;
            });

            newPage.getNextButton().setOnAction(event -> {
                template.getNextButton().pressButton();
                ready = true;
            });

            stage.setScene(newPage.getScene());
        });
    }

    public boolean isReady() {
        return ready;
    }

    public boolean failure() {
        return failed;
    }

    public Template getTemplate() {
        return template;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
