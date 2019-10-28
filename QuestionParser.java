import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;//needed for the parse(Path) method in this class
import java.nio.file.Paths;//use the Paths class's get() method to turn a filenane into a path to a file (see parse(String) below)
import java.util.ArrayList;
import java.util.List;

public class QuestionParser {
    public List<Question> parse(String filename) throws IOException {
       return parse(Paths.get(filename));
    }

    public List<Question> parse(Path path) throws IOException {
        //Introduced in Java 8, Files.readAllLines(Path) reads in all the lines from a file as UTF-8 characters and puts them in a List for us
        //UTF-8 is the most commonly used unicode character encoding
        List<String> fileContents = Files.readAllLines(path);
        return parseLines(fileContents);
    }

    public List<Question> parseLines(List<String> fileContents) {
        List<Question> questions = new ArrayList<>();
        StringBuilder questionText = new StringBuilder();
        for (int i = 0; i < fileContents.size(); i++) {
            String line = fileContents.get(i);
            if (line.startsWith("Answer")) {//once we find a line starting with "Answer" we make a new Question object
                Question question = createQuestion(questionText, line);
                questions.add(question);
                questionText.setLength(0); //clear the StringBuffer so it's ready for the next question
            } else if (line.trim().isEmpty()) {
                //skip empty lines
            } else {
                questionText.append(line);
                questionText.append(System.lineSeparator());//System.lineSeparator() is a platform independent line separator
                //if the line does not start with "Answer" we add the line to the StringBuilder. Note some questions have more than
                //one line, so we want to make sure all the lines do not run together, hence the System.lineSeparator()
            }
        }
        return questions;
    }
    
    private Question createQuestion(StringBuilder question, String answer) {
        String[] answerSplit = answer.split(" ", 2);
        //answerSplit[0] will be the string "Answer", and answerSplit[1] will be the actual answer
        question.deleteCharAt(question.length() - 1); //get rid of the trailing newline
        return new Question(question.toString(), answerSplit[1]);
    }
}
