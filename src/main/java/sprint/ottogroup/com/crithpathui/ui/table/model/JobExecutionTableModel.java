package sprint.ottogroup.com.crithpathui.ui.table.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.data.JobRun;
import sprint.ottogroup.com.crithpathui.data.Repository;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.DateCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.ReturnCodeCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.SecondsCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.StripedRowTableCellRenderer;

public class JobExecutionTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8486194845751075039L;
	// private final Repository repository;
	private final List<JobRun> data = new ArrayList<JobRun>();
	private final List<JobRun> visibleData = new ArrayList<JobRun>();

	public JobExecutionTableModel(Repository repository) {
		// this.repository = repository;

		// extract all job executions and order by name, start date
		TreeSet<JobRun> runs = new TreeSet<JobRun>(JobRun.COMPARE_BY_NAME_STARTTIME_FINISHTIME);
		for (JobRun run : repository.getRuns()) {
			if (!runs.contains(run)) {
				runs.add(run);
			}
		}
		for (CritPath path : repository.getPaths()) {
			for (JobRun run : path.getJobs()) {
				if (!runs.contains(run)) {
					runs.add(run);
				}
			}
		}

		data.addAll(runs);
		visibleData.addAll(runs);
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
		for (JobRun run : data) {
			if (filterText.equals(run.getControlMName())) {
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
		List<JobRun> newFiltered = new ArrayList<JobRun>();
		for (JobRun run : data) {
			matcher.reset(run.getControlMName());
			if (matcher.matches()) {
				newFiltered.add(run);
			}
		}

		visibleData.clear();
		visibleData.addAll(newFiltered);
		fireTableDataChanged();
	}

	public JobRun getJobRun(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < visibleData.size()) {
			return visibleData.get(rowIndex);
		}

		return null;
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

	@Override
	public String getColumnName(int columnIndex) {
		return ColumnsEnum.map(columnIndex).getColumnName();
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return ColumnsEnum.map(columnIndex).getColumnClass();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		JobRun rowData = getJobRun(rowIndex);
		if (rowData != null) {
			return ColumnsEnum.map(columnIndex).getValueAt(rowData);
		}

		return null;
	}

	public static interface ColumnMetadata {
		public String getColumnName();

		public Class<?> getColumnClass();

		public TableCellRenderer getColumnRenderer();

		public Object getValueAt(JobRun value);

		public int getPreferredWidth();
	}

	public static enum ColumnsEnum {
		/** Control-M Name */
		NAME(0, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return String.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new StripedRowTableCellRenderer();
			}

			public String getColumnName() {
				return "Control-M Name";
			}

			public Object getValueAt(JobRun value) {
				return value.getControlMName();
			}

			public int getPreferredWidth() {
				return 330;
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

			public Object getValueAt(JobRun value) {
				return value.getStartingTime();
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

			public Object getValueAt(JobRun value) {
				return value.getFinishedTime();
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

			public Object getValueAt(JobRun value) {
				return value.getDurationInSeconds();
			}

			public int getPreferredWidth() {
				return 60;
			}
		}),
		/** return code */
		RETURNCODE(4, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Integer.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new ReturnCodeCellRenderer();
			}

			public String getColumnName() {
				return "RC";
			}

			public Object getValueAt(JobRun value) {
				return value.getReturnCode();
			}

			public int getPreferredWidth() {
				return 35;
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
