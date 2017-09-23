package sprint.ottogroup.com.crithpathui.ui.action.splunkimport;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class LineSplitterTest {

	@Test
	public void testSplit_1() {
		LineSplitter sut = new LineSplitter();
		String[] result = sut.split("abc\ndef");
		
		System.out.println(Arrays.toString(result));
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals("abc", result[0]);
		assertEquals("def", result[1]);
	}

	@Test
	public void testSplit_2() {
		LineSplitter sut = new LineSplitter();
		String[] result = sut.split("abc\ndef\n");
		
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals("abc", result[0]);
		assertEquals("def", result[1]);
	}

}
