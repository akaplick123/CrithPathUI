package sprint.ottogroup.com.crithpathui.ui.action.splunkimport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sprint.ottogroup.com.crithpathui.ui.action.splunkimport.CsvTable.TableRow;

public class CsvTable implements Iterable<TableRow> {
	private final List<TableRow> data = new ArrayList<TableRow>();
	private final Map<String, Integer> headerToColumnMap = new HashMap<String, Integer>();

	public CsvTable(List<String[]> data) {
		// first line is header
		prepareData(data);
	}

	private void prepareData(List<String[]> data) {
		boolean headerRow = true;
		for (String[] row : data) {
			if (headerRow) {
				for (int idx = 0; idx < row.length; idx++) {
					headerToColumnMap.put(row[idx], idx);
				}
				headerRow = false;
			} else {
				this.data.add(new TableRow(row));
			}
		}
	}

	public class TableRow {
		private final String[] data;

		public TableRow(String[] data) {
			this.data = data;
		}

		/**
		 * @param columnIndex
		 *            0 based index
		 * @return value or <code>null</code>
		 */
		public String value(int columnIndex) {
			if (0 <= columnIndex && columnIndex < data.length) {
				return data[columnIndex];
			}
			return null;
		}

		/**
		 * @param columnName
		 *            case-sensitive column name
		 * @return value or <code>null</code>
		 */
		public String get(String columnName) {
			Integer columnIndex = headerToColumnMap.get(columnName);
			if (columnIndex == null) {
				return null;
			}
	
			return value(columnIndex.intValue());
		}
	}

	public Iterator<TableRow> iterator() {
		return data.iterator();
	}
}
