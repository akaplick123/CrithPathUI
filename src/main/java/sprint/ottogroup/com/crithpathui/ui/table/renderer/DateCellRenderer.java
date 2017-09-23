package sprint.ottogroup.com.crithpathui.ui.table.renderer;

import java.awt.Component;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public class DateCellRenderer extends StripedRowTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final SimpleDateFormat sdf;

	public DateCellRenderer() {
		this("E, dd.MM. HH:mm");
	}

	public DateCellRenderer(String format) {
		this.sdf = new SimpleDateFormat(format);
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp;
		if (value instanceof Date) {
			Date dateValue = (Date) value;
			String stringValue = sdf.format(dateValue);
			comp = super.getTableCellRendererComponent(table, stringValue, isSelected, hasFocus, row, column);
		} else {
			comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}

		return comp;
	}
}
