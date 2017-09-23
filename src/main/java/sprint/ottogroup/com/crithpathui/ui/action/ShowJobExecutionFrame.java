package sprint.ottogroup.com.crithpathui.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sprint.ottogroup.com.crithpathui.ui.JobExecutionFrame;
import sprint.ottogroup.com.crithpathui.ui.MainFrame;

public class ShowJobExecutionFrame implements ActionListener {

	private MainFrame mainFrame;

	public ShowJobExecutionFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void actionPerformed(ActionEvent e) {
		JobExecutionFrame frame = new JobExecutionFrame(mainFrame);
		mainFrame.addFrameCentered(frame, 80);
	}
}
