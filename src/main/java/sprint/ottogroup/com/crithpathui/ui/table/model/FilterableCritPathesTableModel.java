package sprint.ottogroup.com.crithpathui.ui.table.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.data.Repository;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.DateCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.IntegerCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.SecondsCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.StripedRowTableCellRenderer;

public class FilterableCritPathesTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8486194845751075039L;
	// private final Repository repository;
	private final List<CritPath> data = new ArrayList<CritPath>();
	private final List<CritPath> visibleData = new ArrayList<CritPath>();

	public FilterableCritPathesTableModel(Repository repository) {
		// this.repository = repository;

		// extract all job executions and order by name, start date
		for (CritPath path : repository.getPaths()) {
			data.add(path);
		}
		
		Collections.sort(data, CritPath.COMPARE_BY_NAME_AND_ODATE);
		visibleData.addAll(data);
	}

	public void setFilterText(String filterText) {
		if (filterText.contains("*")) {
			applyRegexFilter(filterText);
		} else if (hasExactMatch(filterText)) {
			applyRegexFilter(filterText);
		} else {
			applyRegexFilter("*" + filterText + "*");
		}
	}

	private boolean hasExactMatch(String filterText) {
		for (CritPath path : data) {
			if (filterText.equals(path.getName())) {
				return true;
			}
		}

		return false;
	}

	private void applyRegexFilter(String filterText) {
		// replace * by (.*) and mark others as quote
		StringTokenizer tokenizer = new StringTokenizer(filterText, "*", true);
		StringBuilder regex = new StringBuilder();
		// match case insensitive
		regex.append("(?i)");

		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken();
			if (token != null && token.length() > 0) {
				if (token.startsWith("*")) {
					regex.append("(.*)");
				} else {
					regex.append(Pattern.quote(token));
				}
			}
		}

		Matcher matcher = Pattern.compile(regex.toString()).matcher("");

		// list with all JobRuns that are visible after applying the filter
		List<CritPath> newFiltered = new ArrayList<CritPath>();
		for (CritPath path: data) {
			matcher.reset(path.getName());
			if (matcher.matches()) {
				newFiltered.add(path);
			}
		}

		visibleData.clear();
		visibleData.addAll(newFiltered);
		fireTableDataChanged();
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public int getRowCount() {
		return visibleData.size();
	}

	public ColumnsEnum[] getColumns() {
		return ColumnsEnum.values();
	}

	public int getColumnCount() {
		return getColumns().length;
	}

	public CritPath getCritPath(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < visibleData.size()) {
			return visibleData.get(rowIndex);
		}

		return null;
	}


	@Override
	public String getColumnName(int columnIndex) {
		return ColumnsEnum.map(columnIndex).getColumnName();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return ColumnsEnum.map(columnIndex).getColumnClass();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		CritPath rowData = getCritPath(rowIndex);
		if (rowData != null) {
			visibleData.get(rowIndex);
			return ColumnsEnum.map(columnIndex).getValueAt(rowData);
		}

		return null;
	}

	public static interface ColumnMetadata {
		public String getColumnName();

		public Class<?> getColumnClass();

		public TableCellRenderer getColumnRenderer();

		public Object getValueAt(CritPath value);

		public int getPreferredWidth();
	}

	public static enum ColumnsEnum {
		/** Name of Crithpath */
		NAME(0, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return String.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new StripedRowTableCellRenderer();
			}

			public String getColumnName() {
				return "Name";
			}

			public Object getValueAt(CritPath value) {
				return value.getName();
			}
			
			public int getPreferredWidth() {
				return 350;
			}
		}),
		/** Start time */
		STARTTIME(1, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Date.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new DateCellRenderer();
			}

			public String getColumnName() {
				return "Start";
			}

			public Object getValueAt(CritPath value) {
				return value.firstStartingTime();
			}
			
			public int getPreferredWidth() {
				return 130;
			}
		}),
		/** Finish time */
		FINISHTIME(2, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Date.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new DateCellRenderer();
			}

			public String getColumnName() {
				return "Finished";
			}

			public Object getValueAt(CritPath value) {
				return value.lastFinishedTime();
			}
			
			public int getPreferredWidth() {
				return 130;
			}
		}),
		/** duration in seconds */
		DURATION(3, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Integer.class;
			}

			public String getColumnName() {
				return "Duration";
			}

			public TableCellRenderer getColumnRenderer() {
				return new SecondsCellRenderer();
			}

			public Object getValueAt(CritPath value) {
				return value.durationInSeconds();
			}
			
			public int getPreferredWidth() {
				return 60;
			}
		}),
		/** return code */
		NUMBER_OF_JOBS(4, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Integer.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new IntegerCellRenderer();
			}

			public String getColumnName() {
				return "Size";
			}

			public Object getValueAt(CritPath value) {
				return value.numberOfJobs();
			}
			
			public int getPreferredWidth() {
				return 40;
			}
		});

		private final int columnIndex;
		private final ColumnMetadata metadata;

		private ColumnsEnum(int columnIndex, ColumnMetadata metadata) {
			this.columnIndex = columnIndex;
			this.metadata = metadata;
		}

		public int getColumnIndex() {
			return columnIndex;
		}

		public ColumnMetadata getMetadata() {
			return metadata;
		}

		public static ColumnMetadata map(int columnIndex) {
			for (ColumnsEnum entry : values()) {
				if (entry.columnIndex == columnIndex) {
					return entry.metadata;
				}
			}

			return ColumnsEnum.NAME.metadata;
		}
	}
}
