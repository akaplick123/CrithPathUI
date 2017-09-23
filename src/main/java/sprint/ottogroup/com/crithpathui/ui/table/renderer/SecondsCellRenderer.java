package sprint.ottogroup.com.crithpathui.ui.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;

import sprint.ottogroup.com.crithpathui.ui.helper.TimeUtil;

public class SecondsCellRenderer extends StripedRowTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Integer threshold;

	public SecondsCellRenderer() {
		this(null, "X");
	}

	public SecondsCellRenderer(int threshold) {
		this(threshold, "X");
	}

	private SecondsCellRenderer(Integer threshold, String x) {
		setHorizontalAlignment(SwingConstants.RIGHT);
		this.threshold = threshold;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		if (value instanceof Number) {
			Number secondsRaw = (Number) value;
			String stringValue = TimeUtil.deltaToString(secondsRaw.intValue());
			if (threshold != null && Math.abs(secondsRaw.intValue()) < threshold.intValue()) {
				// don't display anything if value is below defined threshold
				stringValue = "";
			}
			super.getTableCellRendererComponent(table, stringValue, isSelected, hasFocus, row, column);
		} else {
			super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}

		return this;
	}
}
