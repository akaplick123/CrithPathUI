package sprint.ottogroup.com.crithpathui.ui;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.ui.table.model.SingleCritPathTableModel;
import sprint.ottogroup.com.crithpathui.ui.table.model.SingleCritPathTableModel.ColumnsEnum;

public class SingleCritPathFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	private SingleCritPathTableModel tableModel;
	private JPopupMenu popupMenu;

	public SingleCritPathFrame(MainFrame mainFrame, CritPath path) {
		super("Path '" + path.getName() + "'", true, true, true, true);

		// init Layout
		setLayout(new BorderLayout());

		tableModel = new SingleCritPathTableModel(path);
		table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setAutoCreateRowSorter(false);

		for (final ColumnsEnum column : tableModel.getColumns()) {
			int columnIndex = column.getColumnIndex();
			TableColumn tableColumn = table.getColumnModel().getColumn(columnIndex);
			tableColumn.setCellRenderer(column.getMetadata().getColumnRenderer());
			tableColumn.setPreferredWidth(column.getMetadata().getPreferredWidth());
		}

		add(new JScrollPane(table), BorderLayout.CENTER);
		initPopupMenu();
		setSize(640, 480);
	}

	private void initPopupMenu() {
		popupMenu = new JPopupMenu();
		table.addMouseListener(new PopupListener());
	}

	private class PopupListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			maybeShowPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			maybeShowPopup(e);
		}

		private void maybeShowPopup(MouseEvent e) {
			if (e.isPopupTrigger() && table.getSelectedRowCount() != 0) {
				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}
