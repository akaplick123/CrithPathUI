package sprint.ottogroup.com.crithpathui.ui.action.splunkimport;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class CsvExtractor {

	public List<String[]> extractCsv(String... lines) {
		return extractCsv(5, ";", lines);
	}

	public List<String[]> extractCsv(int minColumns, String delim, String... lines) {
		List<String[]> result = new ArrayList<String[]>();

		for (String line: lines) {
			if (line.contains(delim)) {
				String[] cells = line.split(Pattern.quote(delim));
				if (cells.length >= minColumns) {
					result.add(cells);
				}
			}
		}

		return result;
	}
}
