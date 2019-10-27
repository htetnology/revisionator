//Thinzar Htet 180295461

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Revisionator {
	private QuestionParser parser;
	private List<Category> categories;
	private Random random;

	public Revisionator() {
		random = new Random();
		parser = new QuestionParser();
		categories = new ArrayList<>();
		loadQuizQuestions();
	}

	private void loadQuizQuestions() {
		List<String> quizFiles = getQuizFiles();
		List<Question> questions;
		try {
			for (int i = 0; i < quizFiles.size(); i++) {
				String quizFile = quizFiles.get(i);
				questions = parser.parse(quizFile);
				addCategory(quizFile, questions);
			}
		} catch (IOException ex) {
			System.out.println("The quiz file cannot be found");
		}
	}

	private List<String> getQuizFiles() {
		List<String> files = new ArrayList<>();
		File dir = new File(".");
		String[] allFiles = dir.list();
		for (int i = 0; i < allFiles.length; i++) {
			if (allFiles[i].endsWith(".quiz")) {
				files.add(allFiles[i]);
			}
		}
		return files;
	}

	private void addCategory(String filename, List<Question> questions) {
		String categoryName = getCategoryName(filename);
		Category category = new Category(categoryName, questions);
		categories.add(category);
	}

	private String getCategoryName(String filename) {
		return filename.substring(0, filename.length() - 5);
	}

	public List<String> getCategories() {
		List<String> categoryNames = new ArrayList<>();
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			categoryNames.add(category.getName());
		}
		return categoryNames;
	}

	public int getNumberOfQuestionsInCategory(String categoryName) {
		for (int i = 0; i < categories.size(); i++) {
			Category category = categories.get(i);
			if (category.getName().equals(categoryName)) {
				return category.size();
			}
		}
		return 0;
	}

	public List<Question> getQuizFromCategories(List<String> chosenCategoryNames, int numberOfQuestions) {
		List<Category> chosenCategories = getChosenCategories(chosenCategoryNames);
		List<Question> randomQuestions = new ArrayList<>();
		for (int i = 0; i < numberOfQuestions;) {
			Category category = getRandomCategory(chosenCategories);
			Question question = getRandomQuestion(category.getQuestions());
			if (!randomQuestions.contains(question)) {
				randomQuestions.add(question);
				i++;
			}
		}
		return randomQuestions;
	}

	private Category getRandomCategory(List<Category> categories) {
		int randomCategory = random.nextInt(categories.size());
		return categories.get(randomCategory);
	}

	private Question getRandomQuestion(List<Question> questions) {
		int randomQuestion = random.nextInt(questions.size());
		return questions.get(randomQuestion);
	}

	private List<Category> getChosenCategories(List<String> categoryNames) {
		List<Category> chosenCategories = new ArrayList<>();
		for (int i = 0; i < categories.size(); i++) {
			Category currentCategory = categories.get(i);
			for (int j = 0; j < categoryNames.size(); j++) {
				if (currentCategory.getName().equals(categoryNames.get(j))) {
					chosenCategories.add(currentCategory);
				}
			}
		}
		return chosenCategories;
	}
}
