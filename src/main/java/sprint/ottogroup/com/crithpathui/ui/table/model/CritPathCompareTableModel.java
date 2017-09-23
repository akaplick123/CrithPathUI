package sprint.ottogroup.com.crithpathui.ui.table.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.data.JobRun;
import sprint.ottogroup.com.crithpathui.ui.helper.PathMerger;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.DurationWithTooltipCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.SecondsCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.StripedRowTableCellRenderer;

public class CritPathCompareTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8486194845751075039L;
	// private final Repository repository;
	private final List<RowEntry> visibleData = new ArrayList<RowEntry>();
	private final List<ColumnMetadata> visibleColumns = new ArrayList<ColumnMetadata>();

	public CritPathCompareTableModel(List<? extends CritPath> pathes) {
		// this.repository = repository;
		prepareData(pathes);
	}

	private void prepareData(List<? extends CritPath> pathes) {
		createColumns(pathes);
		createRowData(pathes);
	}

	private void createRowData(List<? extends CritPath> pathes) {
		// extract merged path
		PathMerger merger = new PathMerger();
		for (CritPath path : pathes) {
			merger.addPath(toJobNames(path.getJobs()));
		}

		List<String> mergedPath = merger.merge();
		for (String jobName : mergedPath) {
			RowEntry entry = createRowEntry(jobName, pathes);
			visibleData.add(entry);
		}
	}

	private static RowEntry createRowEntry(String jobName, List<? extends CritPath> pathes) {
		List<JobRun> jobRuns = new ArrayList<JobRun>(pathes.size());

		for (CritPath path: pathes) {
			JobRun foundJob = findJobByName(jobName, path);
			jobRuns.add(foundJob);
		}

		RowEntry entry = new RowEntry(jobName, jobRuns);
		return entry;
	}

	private static JobRun findJobByName(String jobName, CritPath path) {
		for (JobRun job: path.getJobs()) {
			if (jobName.equals(job.getControlMName())) {
				return job;
			}
		}

		return null;
	}

	private static List<String> toJobNames(List<JobRun> jobs) {
		List<String> result = new ArrayList<String>();
		for (JobRun job : jobs) {
			result.add(job.getControlMName());
		}
		return result;
	}

	private void createColumns(List<? extends CritPath> pathes) {
		int columnIndex = 0;
		visibleColumns.add(new JobNameColumn(columnIndex++));
		visibleColumns.add(new DurationDeltaColumn(columnIndex++));
		for (int pathIndex = 0; pathIndex < pathes.size(); pathIndex++) {
			visibleColumns.add(new DurationColumn(columnIndex++, pathIndex));
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public int getRowCount() {
		return visibleData.size();
	}

	public Collection<ColumnMetadata> getColumns() {
		return visibleColumns;
	}

	public int getColumnCount() {
		return getColumns().size();
	}

	private RowEntry getRowEntry(int rowIndex) {
		if (rowIndex >= 0 && rowIndex < visibleData.size()) {
			return visibleData.get(rowIndex);
		}

		return null;
	}

	private ColumnMetadata getColumn(int columnIndex) {
		return visibleColumns.get(columnIndex);
	}

	@Override
	public String getColumnName(int columnIndex) {
		final ColumnMetadata column = getColumn(columnIndex);

		if (column != null) {
			return column.getColumnName();
		}
		return null;
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		final ColumnMetadata column = getColumn(columnIndex);

		if (column != null) {
			return column.getColumnClass();
		}

		return String.class;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		RowEntry rowData = getRowEntry(rowIndex);
		ColumnMetadata column = getColumn(columnIndex);

		if (rowData != null && column != null) {
			return getColumn(columnIndex).getValueAt(rowData);
		}

		return null;
	}

	private static class RowEntry {
		private String name;
		private List<JobRun> jobRuns;
		private int maxDeltaInSeconds;

		public RowEntry(String name, List<JobRun> jobRuns) {
			this.name = name;
			this.jobRuns = jobRuns;

			// calc min and max of duration
			int minDuration = -1;
			int maxDuration = -1;
			for (JobRun run : jobRuns) {
				if (run != null) {
					if (minDuration == -1) {
						maxDuration = minDuration = run.getDurationInSeconds();
					}
					minDuration = Math.min(minDuration, run.getDurationInSeconds());
					maxDuration = Math.max(maxDuration, run.getDurationInSeconds());
				}
			}

			this.maxDeltaInSeconds = maxDuration - minDuration;
		}

		public String getName() {
			return name;
		}

		public int getMaxDeltaInSeconds() {
			return maxDeltaInSeconds;
		}

		public JobRun getJobRun(int pathIndex) {
			return jobRuns.get(pathIndex);
		}
	}

	public static interface ColumnMetadata {
		public String getColumnName();

		public Class<?> getColumnClass();

		public TableCellRenderer getColumnRenderer();

		public Object getValueAt(RowEntry value);

		public int getPreferredWidth();

		public int getColumnIndex();
	}

	private static abstract class AbstractColumn implements ColumnMetadata {
		private final String columnName;
		private final Class<?> columnClass;
		private final TableCellRenderer columnRenderer;
		private final int preferredWidth;
		private final int columnIndex;

		public AbstractColumn(String columnName, Class<?> columnClass, TableCellRenderer columnRenderer,
				int preferredWidth, int columnIndex) {
			this.columnName = columnName;
			this.columnClass = columnClass;
			this.columnRenderer = columnRenderer;
			this.preferredWidth = preferredWidth;
			this.columnIndex = columnIndex;
		}

		public AbstractColumn(String columnName, int preferredWidth, int columnIndex) {
			this(columnName, String.class, new StripedRowTableCellRenderer(), preferredWidth, columnIndex);
		}

		public String getColumnName() {
			return columnName;
		}

		public Class<?> getColumnClass() {
			return columnClass;
		}

		public TableCellRenderer getColumnRenderer() {
			return columnRenderer;
		}

		public int getPreferredWidth() {
			return preferredWidth;
		}

		public int getColumnIndex() {
			return columnIndex;
		}
	}

	private static class JobNameColumn extends AbstractColumn {
		public JobNameColumn(int columnIndex) {
			super("Name", 350, columnIndex);
		}

		public Object getValueAt(RowEntry value) {
			return value.getName();
		}
	}

	private static class DurationDeltaColumn extends AbstractColumn {
		public DurationDeltaColumn(int columnIndex) {
			super("Delta", Integer.class, new SecondsCellRenderer(30), 60, columnIndex);
		}

		public Object getValueAt(RowEntry value) {
			return value.getMaxDeltaInSeconds();
		}
	}

	private static class DurationColumn extends AbstractColumn {
		private final int pathIndex;

		public DurationColumn(int columnIndex, int pathIndex) {
			super("Duration", Integer.class, new DurationWithTooltipCellRenderer(), 60, columnIndex);
			this.pathIndex = pathIndex;
		}

		public Object getValueAt(RowEntry value) {
			return value.getJobRun(pathIndex);
		}
	}
}
