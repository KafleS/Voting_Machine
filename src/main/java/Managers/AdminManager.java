package Managers;

import Display.DisplayMain;
import BML.Ballot;
import simple.Battery;
import simple.LLSensor;
import simple.SDCard;
import simple.TPSensor;

import java.util.List;

public class AdminManager {

    private final SDCard sdCard;
    private final LLSensor llSensor;
    private final TPSensor tpSensor;
    private final Battery battery;

    public AdminManager(SDCard sdCard, LLSensor llSensor, TPSensor tpSensor, Battery battery) {
        this.sdCard = sdCard;
        this.llSensor = llSensor;
        this.tpSensor = tpSensor;
        this.battery = battery;
    }

    public boolean startVotingSession() {
        if (battery.failure() || tpSensor.failure() || llSensor.failure()) {
            System.out.println("Hardware failure detected. Cannot start voting session.");
            return false;
        }

        if (tpSensor.isTampered()) {
            System.out.println("System tampering detected. Cannot proceed.");
            return false;
        }

        try {
            List<String> jsonLines = sdCard.read();
            String json = String.join("", jsonLines);
            Ballot ballot = new Ballot(json);

            DisplayMain.setVotingOpen(true);
            System.out.println("Voting session has started.");
            return true;
        } catch (Exception e) {
            System.out.println("Failed to load ballot from SD card.");
            return false;
        }
    }

    public void initiateShutdown() {
        if (!llSensor.latch()) {
            System.out.println("Latch not secured. Cannot shut down safely.");
            return;
        }

        System.out.println("System shutting down...");
        System.exit(0);
    }
}
