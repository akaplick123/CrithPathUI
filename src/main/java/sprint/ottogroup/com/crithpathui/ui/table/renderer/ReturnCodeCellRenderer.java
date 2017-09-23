package sprint.ottogroup.com.crithpathui.ui.table.renderer;

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.SwingConstants;

public class ReturnCodeCellRenderer extends StripedRowTableCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReturnCodeCellRenderer() {
		setHorizontalAlignment(SwingConstants.RIGHT);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		return comp;
	}
}
