package jhave.support;

import javax.swing.JPanel;

public interface QuestionInterface
{
  void evaluate(boolean isCorrect);
  JPanel GetPanel();
  void reset();
  void SetText(String text);
}
