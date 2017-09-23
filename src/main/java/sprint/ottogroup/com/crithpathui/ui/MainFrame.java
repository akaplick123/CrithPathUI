package sprint.ottogroup.com.crithpathui.ui;

import static java.lang.Math.max;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import sprint.ottogroup.com.crithpathui.data.Configuration;
import sprint.ottogroup.com.crithpathui.ui.action.ImportSplunkFilesActionListener;
import sprint.ottogroup.com.crithpathui.ui.action.QuitApplicationActionListener;
import sprint.ottogroup.com.crithpathui.ui.action.ShowFilterableCritPathFrame;
import sprint.ottogroup.com.crithpathui.ui.action.ShowJobExecutionFrame;

public class MainFrame extends JFrame {

	/** Frame title */
	private static final String MAIN_TITLE = "Critpath UI v1.0";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** all configs */
	private final Configuration config;

	private JDesktopPane desktop;

	public MainFrame(final Configuration config) {
		super(MAIN_TITLE);
		this.config = config;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createLayout();
		setSize(calculateSize());
		setLocation(calculateLoacation());
	}

	private Point calculateLoacation() {
		// center on screen
		Dimension screenSize = getToolkit().getScreenSize();
		Dimension frameSize = getSize();

		int x = (screenSize.width - frameSize.width) / 2;
		int y = (screenSize.height - frameSize.height) / 3;

		return new Point(x, y);
	}

	private Dimension calculateSize() {
		Dimension minSize = new Dimension(640, 480);
		Dimension screenSize = getToolkit().getScreenSize();
		// use 20% offset
		int width = (int) (screenSize.width * 0.8d);
		int height = (int) (screenSize.height * 0.8d);

		// don't make it smaller than 'minSize'
		width = max(minSize.width, width);
		height = max(minSize.height, height);

		return new Dimension(width, height);
	}

	private void createLayout() {
		desktop = new JDesktopPane();
		setContentPane(desktop);
		// Make dragging a little faster but perhaps uglier.
		desktop.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);

		// add menu items
		createMenuBar();
	}

	private void createMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu mFile = new JMenu("File");
		mFile.setMnemonic(KeyEvent.VK_F);
		mFile.getAccessibleContext().setAccessibleDescription("Files menu");
		mFile.setToolTipText(mFile.getAccessibleContext().getAccessibleDescription());

		JMenuItem miOpen = new JMenuItem("Import File(s) ...", KeyEvent.VK_I);
		miOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, ActionEvent.ALT_MASK));
		miOpen.getAccessibleContext().setAccessibleDescription("import json files from splunk queries");
		miOpen.setToolTipText(miOpen.getAccessibleContext().getAccessibleDescription());
		miOpen.addActionListener(new ImportSplunkFilesActionListener(this));

		JMenuItem miQuit = new JMenuItem("Quit", KeyEvent.VK_Q);
		miQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.ALT_MASK));
		miQuit.getAccessibleContext().setAccessibleDescription("close the current application");
		miQuit.setToolTipText(miQuit.getAccessibleContext().getAccessibleDescription());
		miQuit.addActionListener(new QuitApplicationActionListener());

		JMenu mView = new JMenu("View");
		mView.setMnemonic(KeyEvent.VK_V);
		mView.getAccessibleContext().setAccessibleDescription("Views menu");
		mView.setToolTipText(mFile.getAccessibleContext().getAccessibleDescription());

		JMenuItem miViewCritpathes = new JMenuItem("Crtitcal Pathes", KeyEvent.VK_C);
		miViewCritpathes.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
		miViewCritpathes.getAccessibleContext().setAccessibleDescription("shows all critical pathes");
		miViewCritpathes.setToolTipText(miViewCritpathes.getAccessibleContext().getAccessibleDescription());
		miViewCritpathes.addActionListener(new ShowFilterableCritPathFrame(this));

		JMenuItem miViewJobExecution = new JMenuItem("Job Executions", KeyEvent.VK_J);
		miViewJobExecution.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, ActionEvent.ALT_MASK));
		miViewJobExecution.getAccessibleContext().setAccessibleDescription("shows all job executions");
		miViewJobExecution.setToolTipText(miViewJobExecution.getAccessibleContext().getAccessibleDescription());
		miViewJobExecution.addActionListener(new ShowJobExecutionFrame(this));

		// construct all menu
		mFile.add(miOpen);
		mFile.addSeparator();
		mFile.add(miQuit);
		menuBar.add(mFile);
		mView.add(miViewCritpathes);
		mView.add(miViewJobExecution);
		menuBar.add(mView);
		setJMenuBar(menuBar);
	}

	public Configuration getConfig() {
		return config;
	}

	public void addFrameCentered(JInternalFrame frame) {
		Dimension frameSize = frame.getSize();
		Dimension desktopSize = desktop.getSize();

		int x = (desktopSize.width - frameSize.width) / 2;
		int y = (desktopSize.height - frameSize.height) / 3;

		frame.setLocation(Math.max(x, 0), Math.max(y, 0));
		frame.setVisible(true);
		desktop.add(frame);
		frame.moveToFront();
		try {
			frame.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
		frame.addInternalFrameListener(new MyInternalFrameListener());
	}

	public void addFrameCentered(JInternalFrame frame, int sizePercent) {
		final int widthFrame = desktop.getWidth() * sizePercent / 100;
		final int heightFrame = desktop.getHeight() * sizePercent / 100;

		frame.setSize(widthFrame, heightFrame);
		addFrameCentered(frame);
	}

	private static class MyInternalFrameListener extends InternalFrameAdapter {
		@Override
		public void internalFrameClosed(InternalFrameEvent e) {
			// TODO
		}
	}
}
