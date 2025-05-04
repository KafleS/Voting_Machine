package Display;

import BML.Ballot;
import BML.Proposition;

import java.util.ArrayList;
import java.util.List;

public class TemplateFactory {
    public static List<Template> fromBallot(Ballot ballot) {
        List<Template> templates = new ArrayList<>();

        for (int i = 0; i < ballot.getNumPropositions(); i++) {
            Proposition p = ballot.getProposition(i);

            String instruction = p.getMaxSelections() == 1
                    ? "Please select only one option below:"
                    : "Please select up to " + p.getMaxSelections() + " options below:";

            // Use getOption(index) instead of getOptions()
            String[] options = new String[p.getNumOptions()];
            for (int j = 0; j < p.getNumOptions(); j++) {
                options[j] = p.getOption(j);
            }

            templates.add(new Template(
                    p.getId(),
                    p.getTitle(),
                    p.getDescription(),
                    instruction,
                    new ButtonData(i != 0),
                    new ButtonData(i == ballot.getNumPropositions() - 1),
                    new ButtonData(i != ballot.getNumPropositions() - 1),
                    new QuestionInfo(options)
            ));
        }

        return templates;
    }
}
