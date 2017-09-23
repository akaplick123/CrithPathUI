package sprint.ottogroup.com.crithpathui.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.ui.MainFrame;
import sprint.ottogroup.com.crithpathui.ui.SingleCritPathFrame;
import sprint.ottogroup.com.crithpathui.ui.helper.SelectionProvider;

public class ShowSingleCritPathFrame implements ActionListener {
	private final MainFrame mainFrame;
	private final SelectionProvider<? extends CritPath> critPathProvider;

	public ShowSingleCritPathFrame(MainFrame mainFrame, SelectionProvider<? extends CritPath> selectionProvider) {
		this.mainFrame = mainFrame;
		this.critPathProvider = selectionProvider;
	}

	public void actionPerformed(ActionEvent e) {
		List<? extends CritPath> pathes = critPathProvider.selection();
		if (pathes.size() == 1) {
			CritPath path = pathes.get(0);
			SingleCritPathFrame frame = new SingleCritPathFrame(mainFrame, path);
			mainFrame.addFrameCentered(frame, 80);
		}
	}
}
