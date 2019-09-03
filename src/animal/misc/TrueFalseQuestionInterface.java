package animal.misc;

public interface TrueFalseQuestionInterface extends QuestionInterface
{
  void MakePanel();
  void SetAnswer(boolean isCorrect);
  void SetQuestion(String questionText);
}
