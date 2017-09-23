package sprint.ottogroup.com.crithpathui.ui.table.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;

public class StripedRowTableCellRenderer extends DefaultTableCellRenderer {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// choose whatever color you prefer
	// i didn't like this color
	// private static final Color STRIPE = UIManager.getColor("textHighlight");

	private static final Color STRIPE = new Color(0.929f, 0.953f, 0.996f);
	private static final Color WHITE = UIManager.getColor("Table.background");

	public StripedRowTableCellRenderer() {
//		setOpaque(true); // MUST do this for background to show up.
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
				column);

		if (!isSelected) {
			if (row % 2 == 0) {
				setBackground(WHITE);
			} else {
				setBackground(STRIPE);
			}
		}

		return this;
	}
}
