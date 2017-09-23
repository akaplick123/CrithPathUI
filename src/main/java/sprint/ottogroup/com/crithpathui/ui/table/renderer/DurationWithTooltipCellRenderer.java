package sprint.ottogroup.com.crithpathui.ui.table.renderer;

import java.awt.Component;
import java.text.SimpleDateFormat;

import javax.swing.JTable;

import sprint.ottogroup.com.crithpathui.data.JobRun;

public class DurationWithTooltipCellRenderer extends SecondsCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat sdf = new SimpleDateFormat("E, dd.MM. HH:mm");

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value == null) {
			super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
			setToolTipText(null);
		}
		if (value instanceof JobRun) {
			JobRun job = (JobRun)value;		
			super.getTableCellRendererComponent(table, job.getDurationInSeconds(), isSelected, hasFocus, row, column);
			setToolTipText(buildToolTipText(job));
			return this;
		}

		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

	private String buildToolTipText(JobRun job) {
		return "<html>Starttime: " + sdf.format(job.getStartingTime()) + "<br/>"
				+ "Finishtime: " + sdf.format(job.getFinishedTime())+"</html>";
	}
}
