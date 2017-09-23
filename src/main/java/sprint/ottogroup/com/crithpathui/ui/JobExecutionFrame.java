package sprint.ottogroup.com.crithpathui.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;

import sprint.ottogroup.com.crithpathui.data.Repository;
import sprint.ottogroup.com.crithpathui.ui.helper.FilterMemory;
import sprint.ottogroup.com.crithpathui.ui.table.model.JobExecutionTableModel;
import sprint.ottogroup.com.crithpathui.ui.table.model.JobExecutionTableModel.ColumnsEnum;

public class JobExecutionFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Repository repository;
	private JTable table;
	private JobExecutionTableModel tableModel;
	private JTextField tfFilter;
	private FilterMemory filterMemory = new FilterMemory();
	private JPopupMenu popupMenu;
	private JButton bApply;

	public JobExecutionFrame(MainFrame mainFrame) {
		super("Executed Jobs", true, true, true, true);
		this.repository = mainFrame.getConfig().getRepository();

		// init Layout
		setLayout(new BorderLayout());
		initFilterComponent();

		tableModel = new JobExecutionTableModel(repository);
		table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		table.setAutoCreateRowSorter(true);

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

	private void initFilterComponent() {
		JPanel pFilter = new JPanel();
		pFilter.setLayout(new BorderLayout());
		JPanel pRight = new JPanel();
		pRight.setLayout(new FlowLayout());
		JPanel pLeft = new JPanel();
		pLeft.setLayout(new FlowLayout());

		final JButton bPrev = new JButton("<");
		bPrev.setEnabled(false);
		tfFilter = new JTextField();
		final JButton bNext = new JButton(">");
		bNext.setEnabled(false);
		bApply = new JButton("apply");

		pLeft.add(bPrev);
		pRight.add(bNext);
		pRight.add(bApply);
		pFilter.add(pLeft, BorderLayout.LINE_START);
		pFilter.add(tfFilter, BorderLayout.CENTER);
		pFilter.add(pRight, BorderLayout.LINE_END);
		add(pFilter, BorderLayout.PAGE_START);

		// add local actions
		bApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				filterMemory.addFilter(tfFilter.getText());
				bPrev.setEnabled(filterMemory.hasPreviousFilter());
				bNext.setEnabled(filterMemory.hasNextFilter());
				tableModel.setFilterText(tfFilter.getText());
			}
		});
		bPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfFilter.setText(filterMemory.getPreviousFilter());
				tfFilter.setCaretPosition(tfFilter.getText().length());
				bApply.doClick();
			}
		});
		bNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tfFilter.setText(filterMemory.getNextFilter());
				tfFilter.setCaretPosition(tfFilter.getText().length());
				bApply.doClick();
			}
		});
		tfFilter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					bApply.doClick();
				}
			}
		});
	}

	private void initPopupMenu() {
		popupMenu = new JPopupMenu();
		JMenuItem menuItem = new JMenuItem("filter that job name");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int tableViewRowIndex = table.getSelectedRow();
				int tableModelRowIndex = table.convertRowIndexToModel(tableViewRowIndex);
				String jobName = tableModel.getJobRun(tableModelRowIndex).getControlMName();
				tfFilter.setText(jobName);
				bApply.doClick();
			}
		});
		popupMenu.add(menuItem);
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
