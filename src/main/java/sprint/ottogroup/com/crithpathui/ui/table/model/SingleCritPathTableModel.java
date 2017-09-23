package sprint.ottogroup.com.crithpathui.ui.table.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.data.JobRun;
import sprint.ottogroup.com.crithpathui.ui.helper.TimeUtil;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.DateCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.IntegerCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.ReturnCodeCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.SecondsCellRenderer;
import sprint.ottogroup.com.crithpathui.ui.table.renderer.StripedRowTableCellRenderer;

public class SingleCritPathTableModel extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8486194845751075039L;
	// private final Repository repository;
	private final List<RowEntry> visibleData = new ArrayList<RowEntry>();

	public SingleCritPathTableModel(CritPath path) {
		// this.repository = repository;

		// prepare data
		JobRun previousJobRun = null;
		int serialNo = 1;
		for (JobRun jobRun : path.getJobs()) {
			RowEntry entry = new RowEntry(jobRun, serialNo);
			if (previousJobRun != null) {
				int gap = TimeUtil.getDeltaInSeconds(previousJobRun.getFinishedTime(), jobRun.getStartingTime());
				entry.setGapToPreviousJobInSeconds(gap);
			}
			visibleData.add(entry);
			previousJobRun = jobRun;
			serialNo++;
		}
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

	private RowEntry getRowEntry(int rowIndex) {
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
		RowEntry rowData = getRowEntry(rowIndex);
		if (rowData != null) {
			visibleData.get(rowIndex);
			return ColumnsEnum.map(columnIndex).getValueAt(rowData);
		}

		return null;
	}

	private static class RowEntry {
		private final JobRun jobRun;
		private final int serialNo;
		private int gapToPreviousJobInSeconds;

		public RowEntry(JobRun jobRun, int serialNo) {
			this.jobRun = jobRun;
			this.serialNo = serialNo;
		}

		public void setGapToPreviousJobInSeconds(int gapToPreviousJobInSeconds) {
			this.gapToPreviousJobInSeconds = gapToPreviousJobInSeconds;
		}

		public int getGapToPreviousJobInSeconds() {
			return gapToPreviousJobInSeconds;
		}

		public JobRun getJobRun() {
			return jobRun;
		}

		public int getSerialNo() {
			return serialNo;
		}
	}

	public static interface ColumnMetadata {
		public String getColumnName();

		public Class<?> getColumnClass();

		public TableCellRenderer getColumnRenderer();

		public Object getValueAt(RowEntry value);

		public int getPreferredWidth();
	}

	public static enum ColumnsEnum {
		/** Serialnumber, starting at 1 */
		SERIALNO(0, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return String.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new IntegerCellRenderer();
			}

			public String getColumnName() {
				return "#";
			}

			public Object getValueAt(RowEntry value) {
				return value.getSerialNo();
			}

			public int getPreferredWidth() {
				return 30;
			}
		}),
		/** Name of Job */
		NAME(1, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return String.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new StripedRowTableCellRenderer();
			}

			public String getColumnName() {
				return "Name";
			}

			public Object getValueAt(RowEntry value) {
				return value.getJobRun().getControlMName();
			}

			public int getPreferredWidth() {
				return 350;
			}
		}),
		/** Start time */
		STARTTIME(2, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Date.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new DateCellRenderer();
			}

			public String getColumnName() {
				return "Start";
			}

			public Object getValueAt(RowEntry value) {
				return value.getJobRun().getStartingTime();
			}

			public int getPreferredWidth() {
				return 130;
			}
		}),
		/** Finish time */
		FINISHTIME(3, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Date.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new DateCellRenderer();
			}

			public String getColumnName() {
				return "Finished";
			}

			public Object getValueAt(RowEntry value) {
				return value.getJobRun().getFinishedTime();
			}

			public int getPreferredWidth() {
				return 130;
			}
		}),
		/** duration in seconds */
		DURATION(4, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Integer.class;
			}

			public String getColumnName() {
				return "Duration";
			}

			public TableCellRenderer getColumnRenderer() {
				return new SecondsCellRenderer();
			}

			public Object getValueAt(RowEntry value) {
				return value.getJobRun().getDurationInSeconds();
			}

			public int getPreferredWidth() {
				return 60;
			}
		}),
		/** gap to previous job */
		GAP(5, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Integer.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new SecondsCellRenderer(5);
			}

			public String getColumnName() {
				return "Gap";
			}

			public Object getValueAt(RowEntry value) {
				return value.getGapToPreviousJobInSeconds();
			}

			public int getPreferredWidth() {
				return 40;
			}
		}),
		/** return code */
		RETURNCODE(6, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Integer.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new ReturnCodeCellRenderer();
			}

			public String getColumnName() {
				return "RC";
			}

			public Object getValueAt(RowEntry value) {
				return value.getJobRun().getReturnCode();
			}

			public int getPreferredWidth() {
				return 35;
			}
		}),
		/** number of reruns */
		RERUNCOUNT(7, new ColumnMetadata() {
			public Class<?> getColumnClass() {
				return Integer.class;
			}

			public TableCellRenderer getColumnRenderer() {
				return new IntegerCellRenderer();
			}

			public String getColumnName() {
				return "Rerun";
			}

			public Object getValueAt(RowEntry value) {
				return value.getJobRun().getRerunNo();
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
