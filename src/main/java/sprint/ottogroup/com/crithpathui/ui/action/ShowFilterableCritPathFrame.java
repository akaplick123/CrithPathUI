package sprint.ottogroup.com.crithpathui.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import sprint.ottogroup.com.crithpathui.ui.FilterableCritPathFrame;
import sprint.ottogroup.com.crithpathui.ui.MainFrame;

public class ShowFilterableCritPathFrame implements ActionListener {

	private MainFrame mainFrame;

	public ShowFilterableCritPathFrame(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void actionPerformed(ActionEvent e) {
		FilterableCritPathFrame frame = new FilterableCritPathFrame(mainFrame);
		mainFrame.addFrameCentered(frame, 80);
	}
}
