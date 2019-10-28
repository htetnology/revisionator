import java.util.List;

public class Category {
    private List<Question> questions;
    private String name;

    public Category(String name, List<Question> questions) {
        this.questions = questions;
        this.name = name;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getName() {
        return name;
    }

    public int size() {
        return questions.size();
    }
}
