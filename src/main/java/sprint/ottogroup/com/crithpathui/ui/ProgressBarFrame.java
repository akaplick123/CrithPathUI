package sprint.ottogroup.com.crithpathui.ui;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

public class ProgressBarFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JProgressBar progressBar;
	private JTextArea taskOutput;
	private SwingWorker<?, ?> task;

	public ProgressBarFrame(String title, SwingWorker<?, ?> task, int maxProgress) {
		super(title, true, false, true, false);
		this.task = task;

		// init layout
		setLayout(new BorderLayout());

		// Create the UI.
		progressBar = new JProgressBar(0, maxProgress);
		progressBar.setValue(0);

		// Call setStringPainted now so that the progress bar height
		// stays the same whether or not the string is shown.
		progressBar.setStringPainted(true);
		progressBar.setIndeterminate(true);

		taskOutput = new JTextArea(10, 50);
		taskOutput.setMargin(new Insets(5, 5, 5, 5));
		taskOutput.setEditable(false);

		JPanel panel = new JPanel();
		panel.add(progressBar);

		add(panel, BorderLayout.PAGE_START);
		add(new JScrollPane(taskOutput), BorderLayout.CENTER);

		pack();
		task.addPropertyChangeListener(new AllListeners());
		addKeyListener(new AllListeners());
		taskOutput.addKeyListener(new AllListeners());
	}

	public void log(String msg) {
		taskOutput.append(msg + "\n");
	}

	private class AllListeners implements PropertyChangeListener, KeyListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if ("progress" == evt.getPropertyName()) {
				int progress = (Integer) evt.getNewValue();
				progressBar.setIndeterminate(false);
				progressBar.setValue(progress);

				// calc progress in percent
				int progressPercent = (progressBar.getValue() * 100 / progressBar.getMaximum());
				log(String.format("Completed %d%% of task.", progressPercent));

				if (task.isDone()) {
					log("Done.");
					setClosable(true);
				}
			}
		}

		public void keyTyped(KeyEvent e) {
		}

		public void keyPressed(KeyEvent e) {
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				if (isClosable()) {
					doDefaultCloseAction();
				}
			}
		}
	}
}
