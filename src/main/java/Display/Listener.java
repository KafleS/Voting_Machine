package Display;

import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class Listener extends SocketHandler implements Runnable {
    private DisplayJavaFX displayJavaFX;

    public Listener(Socket socket, DisplayJavaFX displayJavaFX) {
        super(socket);
        this.displayJavaFX = displayJavaFX;
    }

    @Override
    public void run() {
        System.out.println("Frontend Listener is active");
        Object object = null;
        while (socket.isConnected()) {
            try {
                System.out.println("Waiting on object");
                object = input.readObject();
                System.out.println("Object read");
                if (object instanceof String) {
                    String tempstring = (String) object;
                    if (tempstring.equals("isready")){
                        output.writeObject(displayJavaFX.isReady());
                    }
                    else if (tempstring.equals("gettemplate")){
                        output.writeObject(displayJavaFX.getTemplate());
                    }
                    else if (tempstring.equals("failure")){
                        output.writeObject(displayJavaFX.failure());
                    }
                }
                else if (object instanceof Template){
                    System.out.println("Display.Template gotten");
                    Template temp = (Template) object;
                    Platform.runLater(() -> displayJavaFX.receiveTemplate(temp));
                }
            }
            catch (IOException | ClassNotFoundException e) {
                System.out.println("Frontend Listener Was unable to send message " + object);
                e.printStackTrace();
            }
        }
        System.out.println("Socket disconnected.");
    }
}
