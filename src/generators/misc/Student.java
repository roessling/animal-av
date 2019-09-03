package generators.misc;
import java.text.DecimalFormat;

public class Student {
	public double gradeICS1;
	public double gradeICS2;

	static DecimalFormat f = new DecimalFormat("0.00");

	public Student(double IN_gradeICS1, double IN_gradeICS2) {
		this.gradeICS1 = IN_gradeICS1;
		this.gradeICS2 = IN_gradeICS2;
	}

	@Override
	public String toString() {
		String result = "";
		result = f.format(gradeICS1) + " | " + f.format(gradeICS2);

		return result;
	}
}
