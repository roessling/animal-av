package generators.compression.huffman.utils;

import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

public class ProbabilityFormatter {

	private static ProbabilityFormatter probFormatter = new ProbabilityFormatter();

	private static NumberFormat probablityFormat;

	private ProbabilityFormatter() {

		probablityFormat = NumberFormat.getInstance(new Locale("en"));
		probablityFormat.setMaximumFractionDigits(3);
		probablityFormat.setMinimumFractionDigits(3);
		probablityFormat.setRoundingMode(RoundingMode.HALF_UP);
	}

	public static String format(float probability) {

		return probFormatter.formatProbability(probability);
	}

	public String formatProbability(float probability) {

		return probablityFormat.format(probability);
	}

	public static void setLocale(Locale locale) {

		probablityFormat = NumberFormat.getInstance(locale);
		probablityFormat.setMaximumFractionDigits(3);
		probablityFormat.setMinimumFractionDigits(3);
		probablityFormat.setRoundingMode(RoundingMode.HALF_UP);
	}
}
