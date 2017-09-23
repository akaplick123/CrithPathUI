package sprint.ottogroup.com.crithpathui.ui.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import sprint.ottogroup.com.crithpathui.ui.MainFrame;
import sprint.ottogroup.com.crithpathui.ui.action.splunkimport.ImportSplunkFilesProcess;

public class ImportSplunkFilesActionListener implements ActionListener {

	private MainFrame mainFrame;

	public ImportSplunkFilesActionListener(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	public void actionPerformed(ActionEvent e) {
		// prepare file chooser
		final JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Import Splunk Files ...");
		fc.setApproveButtonText("Import");
		fc.setApproveButtonToolTipText("starts the import of selected files");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(true);
		final FileNameExtensionFilter jsonFileFilter = new FileNameExtensionFilter("JSON Files", "json", "txt");
		fc.addChoosableFileFilter(jsonFileFilter);
		fc.addChoosableFileFilter(new FileNameExtensionFilter("Compressed Files", "zip"));
		fc.setAcceptAllFileFilterUsed(true);
		fc.setFileFilter(jsonFileFilter);
		if (mainFrame.getConfig().getLastImportDirectory() != null) {
			fc.setCurrentDirectory(mainFrame.getConfig().getLastImportDirectory());
		}

		int returnVal = fc.showOpenDialog(mainFrame);
		// remember current directory
		mainFrame.getConfig().setLastImportDirectory(fc.getCurrentDirectory());

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File[] files = fc.getSelectedFiles();
			new ImportSplunkFilesProcess(files, mainFrame).start();
		}
	}
}
