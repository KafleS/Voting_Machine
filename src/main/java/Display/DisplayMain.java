package Display;

import BML.Ballot;
import simple.SDCard;

import java.util.List;

public class DisplayMain {
    public static void main(String[] args) throws Exception {
        // Comment out display stuff for now
         Display display = new Display();


        SDCard sdCard = new SDCard(0, SDCard.Operation.read);
        String json = String.join("\n", sdCard.read());
        System.out.println("[SUCCESS] JSON read from SD card:");
        System.out.println(json);

        // STEP 2: Convert JSON to Ballot object
        System.out.println("\n[INFO] Converting JSON to Ballot object...");
        Ballot ballot = new Ballot(json);
        System.out.println("[SUCCESS] Ballot object created with " + ballot.getNumPropositions() + " propositions.");
        System.out.println("Ballot title: " + ballot.getPreamble().getBallotTitle());

        // STEP 3: Convert Ballot into a list of Template objects
        System.out.println("\n[INFO] Converting Ballot to Template objects...");
        List<Template> templates = TemplateFactory.fromBallot(ballot);
        System.out.println("[SUCCESS] Created " + templates.size() + " template(s).");



         System.out.println("\n[INFO] Sending templates to screen for display...");
         for (Template t : templates) {
             display.sendTemplate(t);
             while (!display.isReady()) {
                 Thread.sleep(1000);
             }
         }

        System.out.println("[DONE] Debug phase complete — no templates sent to screen.");
    }
}
