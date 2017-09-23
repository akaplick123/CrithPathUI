package sprint.ottogroup.com.crithpathui.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.data.Repository;
import sprint.ottogroup.com.crithpathui.ui.action.ShowCritPathCompareFrame;
import sprint.ottogroup.com.crithpathui.ui.action.ShowSingleCritPathFrame;
import sprint.ottogroup.com.crithpathui.ui.helper.FilterMemory;
import sprint.ottogroup.com.crithpathui.ui.helper.SelectionProvider;
import sprint.ottogroup.com.crithpathui.ui.table.model.FilterableCritPathesTableModel;
import sprint.ottogroup.com.crithpathui.ui.table.model.FilterableCritPathesTableModel.ColumnsEnum;

public class FilterableCritPathFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final MainFrame mainFrame;
	private final Repository repository;
	private JTable table;
	private FilterableCritPathesTableModel tableModel;
	private JTextField tfFilter;
	private FilterMemory filterMemory = new FilterMemory();
	private JPopupMenu popupMenu;
	private JButton bApply;
	private JMenuItem miShowCritPathCompare;
	private JMenuItem miShowSingleCritPath;
	private JMenuItem miSetFilter;

	public FilterableCritPathFrame(MainFrame mainFrame) {
		super("Critical Pathes", true, true, true, true);
		this.mainFrame = mainFrame;
		this.repository = mainFrame.getConfig().getRepository();

		// init Layout
		setLayout(new BorderLayout());
		initFilter();

		tableModel = new FilterableCritPathesTableModel(repository);
		table = new JTable(tableModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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

	protected void initFilter() {
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

	private List<CritPath> selectedCritPathes() {
		List<CritPath> result = new ArrayList<CritPath>();
		for (int tableViewRowIndex : table.getSelectedRows()) {
			int tableModelRowIndex = table.convertRowIndexToModel(tableViewRowIndex);
			CritPath path = tableModel.getCritPath(tableModelRowIndex);
			result.add(path);
		}
		return result;
	}

	private Set<String> selectedCritPathesNames() {
		HashSet<String> result = new HashSet<String>();
		for (CritPath path : selectedCritPathes()) {
			result.add(path.getName());
		}
		return result;
	}

	private void initPopupMenu() {
		popupMenu = new JPopupMenu();
		miSetFilter = new JMenuItem("set filter to current name");
		miSetFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Set<String> names = selectedCritPathesNames();
				if (names.size() == 1) {
					String pathName = names.iterator().next();
					tfFilter.setText(pathName);
					bApply.doClick();
				}
			}
		});
		miShowSingleCritPath = new JMenuItem("show details");
		miShowSingleCritPath
				.addActionListener(new ShowSingleCritPathFrame(mainFrame, new SelectionProvider<CritPath>() {
					public List<CritPath> selection() {
						return selectedCritPathes();
					}
				}));
		miShowCritPathCompare = new JMenuItem("compare selected pathes");
		miShowCritPathCompare
				.addActionListener(new ShowCritPathCompareFrame(mainFrame, new SelectionProvider<CritPath>() {
					public List<CritPath> selection() {
						return selectedCritPathes();
					}
				}));

		popupMenu.add(miSetFilter);
		popupMenu.add(miShowSingleCritPath);
		popupMenu.add(miShowCritPathCompare);
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
				miSetFilter.setEnabled(selectedCritPathesNames().size() == 1);
				miShowSingleCritPath.setEnabled(table.getSelectedRowCount() == 1);
				miShowCritPathCompare.setEnabled(table.getSelectedRowCount() > 1);

				popupMenu.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}
