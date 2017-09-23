package sprint.ottogroup.com.crithpathui.ui.table.renderer;

import java.awt.Component;
import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public class IntegerCellRenderer extends StripedRowTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final DecimalFormat df;

	public IntegerCellRenderer() {
		this("#,##0");
	}

	public IntegerCellRenderer(String format) {
		this.df = new DecimalFormat(format);
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp;
		if (value instanceof Number) {
			Number numValue = (Number) value;
			String stringValue = df.format(numValue);
			comp = super.getTableCellRendererComponent(table, stringValue, isSelected, hasFocus, row, column);
		} else {
			comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		}

		return comp;
	}
}
