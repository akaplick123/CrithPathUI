package sprint.ottogroup.com.crithpathui.ui.helper;

import java.util.ArrayList;
import java.util.List;

public class FilterMemory {

	private final List<String> filters = new ArrayList<String>();
	private int currentFilterIndex = -1;

	/**
	 * adds a new filter at the current position. And marks that filter as
	 * "current filter"
	 * 
	 * @param filter
	 *            a valid filter text
	 */
	public void addFilter(String filter) {
		if (filter == null || filter.trim().length() == 0) {
			return;
		}
		if (filter.equals(getCurrentFilter())) {
			return;
		}

		// add filter after current position
		currentFilterIndex++;
		if (currentFilterIndex < 0) {
			filters.add(0, filter);
			currentFilterIndex = 0;
		} else if (currentFilterIndex >= filters.size()) {
			filters.add(filter);
			currentFilterIndex = filters.size() - 1;
		} else {
			filters.add(currentFilterIndex, filter);
		}
	}

	public String getCurrentFilter() {
		if (0 <= currentFilterIndex && currentFilterIndex < filters.size()) {
			return filters.get(currentFilterIndex);
		}

		return "";
	}

	public boolean hasPreviousFilter() {
		return (currentFilterIndex - 1) >= 0;
	}

	/**
	 * @return the new "current filter" after decreasing "current filter"
	 *         position.
	 */
	public String getPreviousFilter() {
		if (hasPreviousFilter()) {
			currentFilterIndex--;
		}
		return getCurrentFilter();
	}

	public boolean hasNextFilter() {
		return (currentFilterIndex + 1) < filters.size();
	}

	public String getNextFilter() {
		if (hasNextFilter()) {
			currentFilterIndex++;
		}
		return getCurrentFilter();
	}

	public int size() {
		return filters.size();
	}
}
