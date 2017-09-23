package sprint.ottogroup.com.crithpathui.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import sprint.ottogroup.com.crithpathui.data.CritPath;
import sprint.ottogroup.com.crithpathui.ui.CritPathCompareFrame;
import sprint.ottogroup.com.crithpathui.ui.MainFrame;
import sprint.ottogroup.com.crithpathui.ui.helper.SelectionProvider;

public class ShowCritPathCompareFrame implements ActionListener {

	private final MainFrame mainFrame;
	private final SelectionProvider<? extends CritPath> critPathProvider;

	public ShowCritPathCompareFrame(MainFrame mainFrame, SelectionProvider<? extends CritPath> selectionProvider) {
		this.mainFrame = mainFrame;
		this.critPathProvider = selectionProvider;
	}

	public void actionPerformed(ActionEvent e) {
		List<? extends CritPath> pathes = critPathProvider.selection();
		if (pathes.size() > 1) {
			CritPathCompareFrame frame = new CritPathCompareFrame(mainFrame, pathes);
			mainFrame.addFrameCentered(frame, 80);
		}
	}

}
