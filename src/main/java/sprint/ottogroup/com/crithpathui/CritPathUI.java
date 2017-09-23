package sprint.ottogroup.com.crithpathui;

import static java.awt.EventQueue.invokeLater;

import sprint.ottogroup.com.crithpathui.data.Configuration;
import sprint.ottogroup.com.crithpathui.ui.MainFrame;

public class CritPathUI {
	public static void main(String[] args) {
		final Configuration config = new Configuration();
		invokeLater(new Runnable() {
			public void run() {
				new MainFrame(config).setVisible(true);
			}
		});
	}
}
