package sprint.ottogroup.com.crithpathui.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class QuitApplicationActionListener implements ActionListener {

	public QuitApplicationActionListener() {
	}

	public void actionPerformed(ActionEvent e) {
		System.exit(0);
	}
}
