package generators.misc.roundrobin;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import algoanim.primitives.generators.Language;
import interactionsupport.models.FillInBlanksQuestionModel;
import interactionsupport.models.MultipleChoiceQuestionModel;
import interactionsupport.models.MultipleSelectionQuestionModel;
import interactionsupport.models.QuestionGroupModel;
import interactionsupport.models.TrueFalseQuestionModel;

public class Questions {

	private static int specificQuestionHelperCounter = 0;

	private static LinkedList<Integer> questionNumbers = new LinkedList<Integer>();

	private static double probRandom;

	private static double probSpecific;

	private static final int NUM_QUESTIONS = 6;



	public static void init(Language lang, double probRandom, double probSpecific) {
		Questions.probRandom = probRandom;
		Questions.probSpecific = probSpecific;

		lang.addQuestionGroup(new QuestionGroupModel("rotate", 5));
		lang.addQuestionGroup(new QuestionGroupModel("toend", 5));
		lang.addQuestionGroup(new QuestionGroupModel("alreadydone", 5));
		lang.addQuestionGroup(new QuestionGroupModel("stilltodo", 5));
		lang.addQuestionGroup(new QuestionGroupModel("wait", 5));
		lang.addQuestionGroup(new QuestionGroupModel("notcompletely", 5));

		questionNumbers.clear();
		for (int i = 1; i <= NUM_QUESTIONS; i++) {
			questionNumbers.add(i);
		}

	}


	public static void pickRandomQuestion(Language lang) {

		boolean newQuestion = (Math.random() < probRandom);

		if (!newQuestion || questionNumbers.isEmpty()) {
			return;
		}

		int index = new Random().nextInt(questionNumbers.size());
		int questionNumber = questionNumbers.get(index);
		questionNumbers.remove(index);

		switch (questionNumber) {
		case 1:
			TrueFalseQuestionModel q1 = new TrueFalseQuestionModel("choiceofk", true, 1);
			q1.setPrompt(MessageOrganizer.get("q_choiceofk"));
			q1.setFeedbackForAnswer(true, MessageOrganizer.get("q_choiceofk_ft"));
			q1.setFeedbackForAnswer(false, MessageOrganizer.get("q_choiceofk_ff"));
			lang.addTFQuestion(q1);
			break;

		case 2:
			TrueFalseQuestionModel q2 = new TrueFalseQuestionModel("interrupted", true, 1);
			q2.setPrompt(MessageOrganizer.get("q_interrupted"));
			q2.setFeedbackForAnswer(true, MessageOrganizer.get("q_interrupted_ft"));
			q2.setFeedbackForAnswer(false, MessageOrganizer.get("q_interrupted_ff"));
			lang.addTFQuestion(q2);
			break;

		case 3:
			MultipleSelectionQuestionModel q3 = new MultipleSelectionQuestionModel("thebigger");
			q3.setPrompt(MessageOrganizer.get("q_thebigger"));
			q3.addAnswer(MessageOrganizer.get("q_thebigger_a1"), 1, MessageOrganizer.get("q_thebigger_f1"));
			q3.addAnswer(MessageOrganizer.get("q_thebigger_a2"), 0, MessageOrganizer.get("q_thebigger_f2"));
			q3.addAnswer(MessageOrganizer.get("q_thebigger_a3"), 0, MessageOrganizer.get("q_thebigger_f3"));
			q3.addAnswer(MessageOrganizer.get("q_thebigger_a4"), 1, MessageOrganizer.get("q_thebigger_f4"));
			lang.addMSQuestion(q3);
			break;

		case 4:
			MultipleChoiceQuestionModel q4 = new MultipleChoiceQuestionModel("whichk");
			q4.setPrompt(MessageOrganizer.get("q_whichk_1") + "\n" + MessageOrganizer.get("q_whichk_2") + "\n" + MessageOrganizer.get("q_whichk_3")
					+ "\n" + MessageOrganizer.get("q_whichk_4"));
			q4.addAnswer("k <- 3", 1, MessageOrganizer.get("q_whichk_f1"));
			q4.addAnswer("k <- 2", 0, MessageOrganizer.get("q_whichk_f2"));
			q4.addAnswer("k <- 4", 0, MessageOrganizer.get("q_whichk_f3"));
			lang.addMCQuestion(q4);


		case 5:
			FillInBlanksQuestionModel q5 = new FillInBlanksQuestionModel("howmanyswitches");
			q5.setPrompt(MessageOrganizer.get("q_howmanyswitches_1") + "\n" + MessageOrganizer.get("q_howmanyswitches_2") + "\n"
					+ MessageOrganizer.get("q_howmanyswitches_3") + "\n" + MessageOrganizer.get("q_howmanyswitches_4"));
			q5.addAnswer("5", 1, MessageOrganizer.get("q_howmanyswitches_f"));
			lang.addFIBQuestion(q5);
			break;

		case 6:
			MultipleSelectionQuestionModel q6 = new MultipleSelectionQuestionModel("whichks");
			q6.setPrompt(MessageOrganizer.get("q_whichks_1") + "\n" + MessageOrganizer.get("q_whichks_2") + "\n" + MessageOrganizer.get("q_whichks_3")
					+ "\n" + MessageOrganizer.get("q_whichks_4"));
			q6.addAnswer("k <- 1", 1, MessageOrganizer.get("q_whichks_f1"));
			q6.addAnswer("k <- 2", 1, MessageOrganizer.get("q_whichks_f2"));
			q6.addAnswer("k <- 3", 0, MessageOrganizer.get("q_whichks_f3"));
			q6.addAnswer("k <- 4", 1, MessageOrganizer.get("q_whichks_f4"));
			q6.addAnswer("k <- 5", 0, MessageOrganizer.get("q_whichks_f5"));
			q6.addAnswer("k <- 6", 0, MessageOrganizer.get("q_whichks_f6"));
			lang.addMSQuestion(q6);
		}

		lang.nextStep();

	}


	@SuppressWarnings("unchecked")
	public static void getSpecificQuestion(Language lang, String name, Object... params) {

		boolean newQuestion = (Math.random() < probSpecific);

		if (!newQuestion) {
			return;
		}


		switch (name) {
		case "rotate":
			boolean rotate = (Boolean) params[0];
			TrueFalseQuestionModel q1 = new TrueFalseQuestionModel("rotate" + specificQuestionHelperCounter, rotate, 1);
			q1.setPrompt(MessageOrganizer.get("q_rotate"));
			q1.setFeedbackForAnswer(true, (rotate) ? MessageOrganizer.get("q_rotate_tt") : MessageOrganizer.get("q_rotate_tf"));
			q1.setFeedbackForAnswer(false, (rotate) ? MessageOrganizer.get("q_rotate_ft") : MessageOrganizer.get("q_rotate_ff"));
			q1.setGroupID("rotate");

			lang.addTFQuestion(q1);
			break;

		case "toEnd":
			boolean toEnd = (Boolean) params[0];
			TrueFalseQuestionModel q2 = new TrueFalseQuestionModel("toEnd" + specificQuestionHelperCounter, toEnd, 1);
			q2.setPrompt(MessageOrganizer.get("q_toEnd"));
			q2.setFeedbackForAnswer(true, (toEnd) ? MessageOrganizer.get("q_toEnd_tt") : MessageOrganizer.get("q_toEnd_tf"));
			q2.setFeedbackForAnswer(false, (toEnd) ? MessageOrganizer.get("q_toEnd_ft") : MessageOrganizer.get("q_toEnd_ff"));
			q2.setGroupID("toend");

			lang.addTFQuestion(q2);
			break;

		case "alreadydone":
			List<String> alreadyDone = (List<String>) params[0];
			FillInBlanksQuestionModel q3 = new FillInBlanksQuestionModel("alreadydone" + specificQuestionHelperCounter);
			q3.setPrompt(MessageOrganizer.get("q_alreadydone"));
			q3.addAnswer("" + alreadyDone.size(), 1,
					(alreadyDone.size() == 0) ? MessageOrganizer.get("q_alreadydone_f0")
							: ((alreadyDone.size() == 1)
									? MessageOrganizer.get("q_alreadydone_f1_a") + alreadyDone.get(0) + MessageOrganizer.get("q_alreadydone_f1_b")
									: MessageOrganizer.get("q_alreadydone_f2_a") + alreadyDone.size() + MessageOrganizer.get("q_alreadydone_f2_b")
											+ alreadyDone));
			q3.setGroupID("alreadydone");

			lang.addFIBQuestion(q3);
			break;

		case "stilltodo":
			List<String> stillToDo = (List<String>) params[0];
			FillInBlanksQuestionModel q4 = new FillInBlanksQuestionModel("stilltodo" + specificQuestionHelperCounter);
			q4.setPrompt(MessageOrganizer.get("q_stilltodo"));
			q4.addAnswer("" + stillToDo.size(), 1, (stillToDo.size() == 0) ? MessageOrganizer.get("q_stilltodo_f0")
					: ((stillToDo.size() == 1)
							? MessageOrganizer.get("q_stilltodo_f1_a") + stillToDo.get(0) + MessageOrganizer.get("q_stilltodo_f1_b")
							: MessageOrganizer.get("q_stilltodo_f2_a") + stillToDo.size() + MessageOrganizer.get("q_stilltodo_f2_b") + stillToDo));
			q4.setGroupID("stilltodo");

			lang.addFIBQuestion(q4);
			break;

		case "wait":
			boolean wait = (Boolean) params[0];
			boolean several = (Boolean) params[1];
			MultipleChoiceQuestionModel q5 = new MultipleChoiceQuestionModel("wait" + specificQuestionHelperCounter);
			q5.setPrompt(MessageOrganizer.get("q_wait"));
			q5.addAnswer(MessageOrganizer.get("q_wait_a1"), (wait && !several) ? 1 : 0,
					(wait && !several) ? MessageOrganizer.get("q_wait_f-correct")
							: (MessageOrganizer.get("q_wait_f-notCorrect")
									+ (!wait ? MessageOrganizer.get("q_wait_f1_a") : MessageOrganizer.get("q_wait_f1_b"))));
			q5.addAnswer(MessageOrganizer.get("q_wait_a2"), (wait && several) ? 1 : 0,
					(wait && several) ? MessageOrganizer.get("q_wait_f-correct")
							: (MessageOrganizer.get("q_wait_f-notCorrect")
									+ (!wait ? MessageOrganizer.get("q_wait_f2_a") : MessageOrganizer.get("q_wait_f2_b"))));
			q5.addAnswer(MessageOrganizer.get("q_wait_a3"), (!wait) ? 1 : 0,
					(!wait) ? MessageOrganizer.get("q_wait_f-correct") : MessageOrganizer.get("q_wait_f3"));
			q5.setGroupID("wait");

			lang.addMCQuestion(q5);
			break;

		case "notcompletely":
			List<String> all = (List<String>) params[0];
			List<String> notCompletely = (List<String>) params[1];
			MultipleSelectionQuestionModel q6 = new MultipleSelectionQuestionModel("notcompletely" + specificQuestionHelperCounter);
			q6.setPrompt(MessageOrganizer.get("q_notcompletely_1")
					+ ((notCompletely.isEmpty() || notCompletely.size() == 1) ? MessageOrganizer.get("q_notcompletely_2")
							: MessageOrganizer.get("q_notcompletely_3") + notCompletely.size() + MessageOrganizer.get("q_notcompletely_4")));

			for (String pName : all) {
				boolean contained = notCompletely.contains(pName);
				q6.addAnswer(pName, contained ? 1 : 0, contained ? (pName + MessageOrganizer.get("q_notcompletely_f-correct"))
						: (pName + MessageOrganizer.get("q_notcompletely_f-notCorrect")));
			}
			q6.addAnswer(MessageOrganizer.get("q_notcompletely_a"), notCompletely.isEmpty() ? 1 : 0, notCompletely.isEmpty()
					? (MessageOrganizer.get("q_notcompletely_aT")) : (MessageOrganizer.get("q_notcompletely_aF") + notCompletely));

			q6.setGroupID("notcompletely");
			lang.addMSQuestion(q6);
		}

		specificQuestionHelperCounter++;

		lang.nextStep();

	}


}
