package Display;

import java.io.Serializable;

public class Template implements Serializable {
    private final String title;
    private final String description;
    private final String instructions;
    private final ButtonData PreviousButton;
    private final ButtonData SubmitButton;
    private final ButtonData NextButton;
    private final QuestionInfo questionData; // QuestionInfo

    // Constructor
    public Template(String title, String description, String instructions, ButtonData PreviousButton, ButtonData SubmitButton, ButtonData NextButton, QuestionInfo questionData) {
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.PreviousButton = PreviousButton;
        this.SubmitButton = SubmitButton;
        this.NextButton = NextButton;
        this.questionData = questionData;
    }

    public String getTitle() {return title;}
    public String getDescription() {return description;}
    public String getInstructions() {return instructions;}
    public ButtonData getPreviousButton() {return PreviousButton;}
    public ButtonData getSubmitButton() {return SubmitButton;}
    public ButtonData getNextButton() {return NextButton;}
    public QuestionInfo getQuestionData() {return questionData;}
}
