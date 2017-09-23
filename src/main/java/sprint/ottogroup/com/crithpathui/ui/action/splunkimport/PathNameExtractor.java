package sprint.ottogroup.com.crithpathui.ui.action.splunkimport;

public class PathNameExtractor {
	public String extractPathName(String... sysoutLines) {
		for (String line: sysoutLines) {
			if (line.contains("pathname=")) {
				int idx1 = line.indexOf("pathname=");
				int idx2 = line.indexOf(" ", idx1+1);
				if (idx2 > idx1) {
					String pathName = line.substring(idx1+"pathname=".length(), idx2);
					return pathName;
				}
			}
		}
		
		return null;
	}
}