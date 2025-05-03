package display;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class DisplayJavaFX extends Application {
    private Stage stage;

    private Template template;
    private Listener listener;
    private boolean failed = false;
    private boolean ready = true;
    private Label titleLabel;
    private VBox questionBox;
    private Button leftButton, middleButton, rightButton;
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;

        try (ServerSocket serverSocket = new ServerSocket(12345)) {
            while (!serverSocket.isClosed()) {
                System.out.printf("Port opened @ %s. Waiting for connection...\n", 12345);
                Socket socket = serverSocket.accept();
                System.out.printf("Reader connected from %s.\n", socket.getLocalAddress());
                listener = new Listener(socket, this);
                System.out.println("Closing port...");
                serverSocket.close();
            }
        } catch (IOException e) {
            System.out.println("Port closed");
        }

        primaryStage.setTitle("demo");
        primaryStage.show();
        new Thread(listener).start();
    }

    public void receiveTemplate(Template template) {
        System.out.println("Display.Template received");
        this.template = template;
        ready = false;

        Platform.runLater(() -> {
            VotingMachinePage newPage = new VotingMachinePage(template);

            // Set button actions
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
    public boolean failure() { return failed;}

    public Template getTemplate() {
        return template;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
