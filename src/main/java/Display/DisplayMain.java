package display;

import java.io.IOException;
import java.util.ArrayList;

public class DisplayMain {
    private static ArrayList<Template> templates;

    private static void printQuestionInfo(Template t){
        if (t.getQuestionData() instanceof QuestionInfo) {
            System.out.println(t.getQuestionData());
        }
    }
    
    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        Display display = new Display();

        /** election set up **/
        //Election set up (A1)
        Template electionSetUp = new Template(
                "Election Setup",
                "Follow these steps before starting the election process: \n" +
                        " - Open back door and insert all SD cards and paper trail cartridge.\n" +
                        " - Ensure UPS battery is correctly inserted.\n" +
                        " - Close the door.",
                "Select \"Open Election\" once all steps are completed, and select \"Submit\"",
                new ButtonData(false),
                new ButtonData(true),
                new ButtonData(false),
                new QuestionInfo(new String[]{"Open Election"})
        );

        //supervisor card (A2)
        Template sCard = new Template(
                "",
                "Please Insert Supervisor Card.",
                "",
                new ButtonData(false),
                new ButtonData(false),
                new ButtonData(false),
                new QuestionInfo(new String[0])
        );

        //voting setup (A3)
        Template votingSetUp = new Template(
                "Voting Setup",
                "Select \"Open Voting\" to begin the voting session \n" +
                        "or select \"Close Election\" to close election session.",
                "Please select only one option below: ",
                new ButtonData(false),
                new ButtonData(true),
                new ButtonData(false),
                new QuestionInfo(new String[]{"Open Voting", "Close Election"})
        );

        /** election close **/
        // A4: Insert Supervisor Card Again
        Template svCard = new Template(
                "",
                "Please Insert Supervisor Card or Voter Card.",
                "",
                new ButtonData(false),
                new ButtonData(false),
                new ButtonData(false),
                new QuestionInfo(new String[0])
        );
        // A5: Please Remove the Card
        Template removeCardTemplate = new Template(
                "",
                "Please Remove Card.",
                "", // No sub-instruction
                new ButtonData(false), // Previous
                new ButtonData(false), // Submit
                new ButtonData(false), // Next
                new QuestionInfo(new String[0])
        );
        // A6: Close Voting (Post-Voting Setup)
        Template closeVotingTemplate = new Template(
                "Voting Setup", // Title should match the earlier "Voting Setup"
                "Select \"Close Voting\" to end the voting session", // Main description
                "Select \"Close Voting\" and select \"Submit\"", // Instruction
                new ButtonData(false), // Previous
                new ButtonData(true),  // Submit
                new ButtonData(false), // Next
                new QuestionInfo(new String[]{"Close Voting"})
        );



        /** ballot questions **/
        // Proposition: "Presidential Election" (B1)
        Template president = new Template(
                "2025 Presidential Election",
                "",
                "Please select only one option below:",
                new ButtonData(false),
                new ButtonData(false),
                new ButtonData(true),
                new QuestionInfo(new String[]{"Alice (Democrat)", "Bob (Republican)", "Carol (Independent)"})
        );

        // Proposition: "Constitutional Amendment" (B2)
        Template amendment = new Template(
                "Constitutional Amendment",
                "Increase the property tax exemption for veterans from $4,000 to $10,000",
                "Please select only one option below:",
                new ButtonData(true),
                new ButtonData(false),
                new ButtonData(true),
                new QuestionInfo(new String[]{"Yes", "No"})
        );

        // Proposition: "Board of Commissioners" (B3)
        Template commissioners = new Template(
                "Board of Commissioners",
                "",
                "Please select as many as three options below:",
                new ButtonData(true),
                new ButtonData(false),
                new ButtonData(true),
                new QuestionInfo(new String[]{"Justine", "Jones", "Mike", "Kurt", "Craig"})
        );

        // Confirmation/Ballot Submission (B4)
        Template confirmation = new Template(
                "Ballot Submission",
                "Press the \"Submit\" button to confirm your selections,\n or press the \"Previous\" button to verify or " +
                        "change your selections.",
                "",
                new ButtonData(true),
                new ButtonData(true),
                new ButtonData(false),
                new QuestionInfo(new String[0])
        );


        /** faults **/

        Template powerError = new Template(
                "ERROR",
                "Power to machine was interrupted. \n Insert Supervisor Card and shutdown machine.",
                "Select \"Shutdown Machine\" and select \"Submit\"",
                new ButtonData(false),
                new ButtonData(true),
                new ButtonData(false),
                new QuestionInfo(new String[]{"Shutdown Machine"})
        );

        Template tamperError = new Template(
                "ERROR",
                "Machine Tamper Sensor was triggered. \n Insert Supervisor Card and shutdown machine.",
                "Select \"Shutdown Machine\" and select \"Submit\"",
                new ButtonData(false),
                new ButtonData(true),
                new ButtonData(false),
                new QuestionInfo(new String[]{"Shutdown Machine"})
        );

        Template timeoutError = new Template(
                "ERROR",
                "Voter timeout has occurred. \n Insert Supervisor Card and shutdown machine.",
                "Select \"Shutdown Machine\" and select \"Submit\"",
                new ButtonData(false),
                new ButtonData(true),
                new ButtonData(false),
                new QuestionInfo(new String[]{"Shutdown Machine"})
        );

        templates = new ArrayList<>();

        templates.add(sCard);//A2
        templates.add(electionSetUp);//A1
        templates.add(votingSetUp);//A3
        templates.add(removeCardTemplate);//A5
        templates.add(svCard); //A4
        templates.add(president);//B1
        templates.add(amendment);//B2
        templates.add(commissioners);//B3
        templates.add(confirmation);//B4
        templates.add(removeCardTemplate);//A5
        templates.add(svCard);//A4
        templates.add(closeVotingTemplate);//A6
        templates.add(votingSetUp);//A3
        templates.add(removeCardTemplate);//A5
        templates.add(powerError);//C1
        templates.add(tamperError);//C2
        templates.add(timeoutError);//C3


        display.sendTemplate(electionSetUp);

        int index = 0;
        while (true) {
            display.sendTemplate(templates.get(index));
            index++;
            Thread.sleep(1500);
        }
    }
}
