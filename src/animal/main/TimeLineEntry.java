package animal.main;

public class TimeLineEntry
{
  String label;
  int stepNumber;

  public TimeLineEntry(String header, int step)
  {
    label = header;
    stepNumber = step;
  }

  public String getLabel()
  {
    return label;
  }

  public int getStep()
  {
    return stepNumber;
  }

  public String toString()
  {
    return label +" ("+stepNumber+")";
  }

}
