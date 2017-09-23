package sprint.ottogroup.com.crithpathui.ui.helper;

import java.util.Date;

public class TimeUtil {

	private TimeUtil() {
	}

	/**
	 * @param a
	 *            some date
	 * @param b
	 *            some date
	 * @return b - a in seconds
	 */
	public static int getDeltaInSeconds(Date a, Date b) {
		if (a == null || b == null) {
			return 0;
		}

		long aTime = a.getTime();
		long bTime = b.getTime();
		return (int) ((bTime - aTime) / 1000L);
	}

	/**
	 * @param deltaInSeconds
	 *            delta in seconds
	 * @return [-][HH:][MM:]SS e.g. 2:32
	 */
	public static String deltaToString(int deltaInSeconds) {
		String prefix = "";
		if (deltaInSeconds < 0) {
			prefix = "-";
		}
		int secondsAbs = Math.abs(deltaInSeconds);
		int rest = secondsAbs;
		int seconds = rest % 60;
		rest = (rest - seconds) / 60;
		int minutes = rest % 60;
		rest = (rest - minutes) / 60;
		int hours = rest;

		if (hours > 0) {
			return prefix + digits(hours) + ":" + digits2(minutes) + ":" + digits2(seconds);
		}

		// hours are 0
		if (minutes > 0) {
			return prefix + digits(minutes) + ":" + digits2(seconds);
		}

		// minutes are hours are 0
		return prefix + digits(seconds);
	}

	private static String digits(int value) {
		return Integer.toString(value);
	}

	private static String digits2(int value) {
		if (value < 10) {
			return "0" + digits(value);
		}
		return digits(value);
	}
}
