package animal.misc;

public interface MultipleChoiceInterface extends QuestionInterface
{
  void MakePanel();
  void RandomizeAnswers();
  void SetCorrectAnswer(int answerIndex);
  void SetPossibleAnswer(String answerText);
  void SetQuestion(String questionText);
}
