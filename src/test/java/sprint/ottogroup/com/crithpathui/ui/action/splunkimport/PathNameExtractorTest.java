package sprint.ottogroup.com.crithpathui.ui.action.splunkimport;

import static org.junit.Assert.*;

import org.junit.Test;

public class PathNameExtractorTest {

	@Test
	public void testExtractPathName() {
		PathNameExtractor sut = new PathNameExtractor();
		String result = sut.extractPathName(
				"getCriticalPath.sh env=PROD pathname=CRITPATH_SP_GROSSKUNDENEXPORT_GRUPPE nodeid=ctmproda.ov.otto.de startjobs=GROSSKUNDEN_NK_EXPORT_START_ID16656 orderid=1n8na odate=20170609");
		assertEquals("CRITPATH_SP_GROSSKUNDENEXPORT_GRUPPE", result);
	}

}
