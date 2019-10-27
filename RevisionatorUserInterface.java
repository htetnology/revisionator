//Thinzar Htet 180295461 

import java.io.PrintStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RevisionatorUserInterface {
	private Scanner input;
	private PrintStream output;
	private Revisionator revisionator;

	public RevisionatorUserInterface() {
		input = new Scanner(System.in);
		output = System.out;
		revisionator = new Revisionator();
	}

	public void run() {
		while (true) {
			showMenu();
			askUserForMenuChoice();
			int choice = getNumberFromUser();
			executeChoice(choice);
		}
	}

	private void showMenu() {
		output.println("Choose one of the following options:");
		output.println("1. Take a quiz");
		output.println("2. Quit");
	}

	private void askUserForMenuChoice() {
		output.print("Enter your choice: ");
	}

	private int getNumberFromUser() {
		while (true) {
			try {
				String text = getTextFromUser();
				return Integer.parseInt(text);
			} catch (NumberFormatException ex) {
				output.print("Please enter a valid number:");
			}
		}

	}

	private void executeChoice(int choice) {
		if (choice == 1) {
			quiz();
		} else if (choice == 2) {
			output.println("Bye!");
			System.exit(0);
		} else {
			output.println("I don't know how to do that.");
		}
	}

	private void quiz() {
		List<String> userSelectedCategories = getValidQuestionCategoriesFromUser();
		int userSelectedNumberOfQuestions = getValidNumberOfQuestions(userSelectedCategories);
		List<Question> questions = revisionator.getQuizFromCategories(userSelectedCategories,
				userSelectedNumberOfQuestions);
		openingMessage(userSelectedCategories, questions);
		runQuiz(questions);
	}

	private List<String> getValidQuestionCategoriesFromUser() {
		List<String> categories = getQuestionCategories();
		List<String> userSelectedCategories;
		output.println("There are questions in these categories: " + prettyPrint(categories));
		do {
			output.print(
					"Enter the categories you would like the questions to be selected from. Please put a space between each category: ");
			String userInput = getTextFromUser();
			userSelectedCategories = getUserSelectedCategories(userInput);
			if (userSelectedCategories.size() == 0) {
				output.println("No categories recognised. Please try again");
			}
		} while (userSelectedCategories.size() == 0);

		return userSelectedCategories;
	}

	private void displayTotalQuestionsInUserSelectedCategories(int totalQuestionsInUserSelectedCategories) {
		output.print("There are " + totalQuestionsInUserSelectedCategories + " questions available. ");
	}

	private void askUserForNumberOfQuizQuestions() {
		output.print("How many questions should the quiz have? ");
	}

	private boolean tooLowNumberOfQuestions(int userSelectedNumberOfQuestions) {
		return userSelectedNumberOfQuestions < 1;
	}

	private boolean tooHighNumberOfQuestions(int userSelectedNumberOfQuestions,
			int totalQuestionsInUserSelectedCategories) {
		return userSelectedNumberOfQuestions > totalQuestionsInUserSelectedCategories;
	}

	private boolean invalidNumberOfQuestions(int userSelectedNumberOfQuestions,
			int totalQuestionsInUserSelectedCategories) {
		return userSelectedNumberOfQuestions < 1
				|| userSelectedNumberOfQuestions > totalQuestionsInUserSelectedCategories;
	}

	private int getValidNumberOfQuestions(List<String> userSelectedCategories) {
		int userSelectedNumberOfQuestions;
		int totalQuestionsInUserSelectedCategories = getNumberOfQuestionsInCategories(userSelectedCategories);
		displayTotalQuestionsInUserSelectedCategories(totalQuestionsInUserSelectedCategories);
		do {
			askUserForNumberOfQuizQuestions();
			userSelectedNumberOfQuestions = getNumberFromUser();
			if (tooLowNumberOfQuestions(userSelectedNumberOfQuestions)) {
				output.println("Too low. Please try again.");
			} else if (tooHighNumberOfQuestions(userSelectedNumberOfQuestions,
					totalQuestionsInUserSelectedCategories)) {
				output.println("Too high. Please try again.");
			}
		} while (invalidNumberOfQuestions(userSelectedNumberOfQuestions, totalQuestionsInUserSelectedCategories));

		return userSelectedNumberOfQuestions;
	}

	private int getNumberOfQuestionsInCategories(List<String> categories) {
		int total = 0;
		for (int i = 0; i < categories.size(); i++) {
			total += revisionator.getNumberOfQuestionsInCategory(categories.get(i));
		}
		return total;
	}

	private String prettyPrint(List<String> list) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < list.size(); i++) {
			builder.append(list.get(i));
			if (i < list.size() - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

	private void openingMessage(List<String> categories, List<Question> questions) {
		output.println();
		String categoryOrCategories = "category";
		if (categories.size() > 1)
			categoryOrCategories = "categories";

		String questionOrQuestions = "question";
		if (questions.size() > 1)
			questionOrQuestions = "questions";

		output.println("You have selected a quiz with " + questions.size() + " " + questionOrQuestions + " from the "
				+ categoryOrCategories + " " + prettyPrint(categories) + ". Good luck!");
		output.println();
	}

	private void runQuiz(List<Question> questions) {
		TimeKeeper stopwatch = new TimeKeeper();
		stopwatch.start();
		List<Boolean> quizAnswers = new ArrayList<Boolean>();
		for (int i = 0; i < questions.size(); i++) {
			String userAnswer = askQuestionAndGetAnswer(questions.get(i), i + 1);
			quizAnswers.add(isCorrectAnswer(questions.get(i), userAnswer));
		}
		stopwatch.stop();
		showQuizResults(quizAnswers, stopwatch);
	}

	private String askQuestionAndGetAnswer(Question question, int number) {
		output.println("Question " + number + ":");
		output.println(question.getQuestion());
		output.print("Your answer: ");
		String answer = getTextFromUser();
		output.println();

		return answer;

	}

	private boolean isCorrectAnswer(Question question, String answer) {
		return question.getAnswer().equalsIgnoreCase(answer.trim());
	}

	private void showQuizResults(List<Boolean> answers, TimeKeeper stopwatch) {
		String timeTaken = TimeFormatter.millisecondsToMinutesAndSeconds(stopwatch.getElapsedMilliseconds());
		int correct = howManyCorrect(answers);
		int total = answers.size();
		double percentage = correct * (100f / total);
		String encouragement = encouragingMessage(percentage);
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		String roundedPercentage = df.format(percentage);

		System.out.println("You scored " + correct + "/" + total + " and took " + timeTaken
				+ " to complete the quiz. That's " + roundedPercentage + "%. " + encouragement);
	}

	protected String encouragingMessage(double percentage) {
		if (percentage < 0)
			return "How have you done that?!";

		if (percentage == 0)
			return "You can only get better!";

		if (percentage > 0 && percentage <= 20)
			return "You got some right!";

		if (percentage > 20 && percentage <= 40)
			return "You did OK!";

		if (percentage > 40 && percentage <= 60)
			return "Nice job!";

		if (percentage > 60 && percentage <= 80)
			return "Very good!";

		return "Excellent!";
	}

	private int howManyCorrect(List<Boolean> answers) {
		int correctAnswers = 0;
		for (int i = 0; i < answers.size(); i++) {
			if (answers.get(i))
				correctAnswers++;
		}
		return correctAnswers;
	}

	private String getTextFromUser() {
		return input.nextLine();
	}

	private List<String> getQuestionCategories() {
		return revisionator.getCategories();
	}

	public List<String> getUserSelectedCategories(String input) {
		List<String> validCategories = new ArrayList<>();
		String[] chosenCategories = input.split(" ");
		List<String> existingCategories = getQuestionCategories();

		for (int i = 0; i < chosenCategories.length; i++) {
			for (int j = 0; j < existingCategories.size(); j++) {
				String category = existingCategories.get(j);
				if (category.equalsIgnoreCase(chosenCategories[i])) {
					validCategories.add(category);
				}
			}
		}
		return validCategories;
	}

	public static void main(String[] args) {
		RevisionatorUserInterface ui = new RevisionatorUserInterface();
		ui.run();
	}
}
