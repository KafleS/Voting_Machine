package display;

import java.io.IOException;
import java.net.Socket;

public class Display extends SocketHandler{
    public Display() throws IOException{
        super(new Socket("localhost", 12345));
    }

    /**
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized boolean isReady() throws IOException, ClassNotFoundException{
        output.writeObject("isready");
        Object returnob = input.readObject();
        if (returnob instanceof Boolean){
            return (Boolean) returnob;
        }
        else return false;
    }

    /**
     * Sends a template to DisplayJavaFX to be displayed.
     * @param t Template to be displayed
     * @throws IOException
     */
    public synchronized void sendTemplate(Template t) throws IOException {
        output.writeObject(t);
    }

    /**
     * getTemplate will make a call over sockets to DisplayJavaFX to get the template from the JavaFX
     * including what changes may have been made.
     * @return Template received from DisplayJavaFX. null if non-Template received.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized Template getTemplate() throws IOException, ClassNotFoundException{
        output.writeObject("gettemplate");
        Object returnob = input.readObject();
        if (returnob instanceof Template){
            return (Template) returnob;
        }
        else return null;
    }

    /**
     * failure will make a call over sockets to DisplayJavaFX to check if failure has occurred.
     * @return true if failed. false if not. false if non Boolean return.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public synchronized boolean failure() throws IOException, ClassNotFoundException{
        output.writeObject("failure");
        Object returnob = input.readObject();
        if (returnob instanceof Boolean){
            return (Boolean) returnob;
        }
        else return false;
    }

}
