package sprint.ottogroup.com.crithpathui.data;

import java.io.File;

public class Configuration {

	// FIXME: remove local setup
	private File lastImportDirectory = new File("/Users/Andre/Desktop/critpath");
	private final Repository repository = new Repository();

	public File getLastImportDirectory() {
		return lastImportDirectory;
	}
	
	public void setLastImportDirectory(File lastImportDirectory) {
		this.lastImportDirectory = lastImportDirectory;
	}

	public Repository getRepository() {
		return repository;
	}
}
