package sprint.ottogroup.com.crithpathui.ui.helper;

import static org.junit.Assert.*;

import org.junit.Test;

public class FilterMemoryTest {

	@Test
	public void testAddFilter_Null() {
		FilterMemory sut = new FilterMemory();
		sut.addFilter(null);
		assertEquals(0, sut.size());
	}

	@Test
	public void testAddFilter_Empty() {
		FilterMemory sut = new FilterMemory();
		sut.addFilter("");
		assertEquals(0, sut.size());
	}

	@Test
	public void testAddFilter_add() {
		FilterMemory sut = new FilterMemory();
		sut.addFilter("f1");
		assertEquals(1, sut.size());
		assertEquals("f1", sut.getCurrentFilter());
	}

	@Test
	public void testAddFilter_add_prev() {
		FilterMemory sut = new FilterMemory();
		sut.addFilter("f1");
		sut.addFilter("f2");
		assertEquals("Case 1", "f2", sut.getCurrentFilter());
		assertEquals("Case 2", "f1", sut.getPreviousFilter());
		assertEquals("Case 3", "f1", sut.getCurrentFilter());
		sut.addFilter("fInBetween");
		assertEquals("Case 4", "fInBetween", sut.getCurrentFilter());
		assertEquals("Case 5", "f2", sut.getNextFilter());
		assertEquals("Case 6", "f2", sut.getCurrentFilter());
		assertEquals("Case 7", false, sut.hasNextFilter());
	}

	@Test
	public void testAddFilter_add_add() {
		FilterMemory sut = new FilterMemory();
		sut.addFilter("f1");
		sut.addFilter("f2");
		assertEquals(2, sut.size());
		assertEquals("f2", sut.getCurrentFilter());
	}

	@Test
	public void testGetCurrentFilter_Empty() {
		FilterMemory sut = new FilterMemory();
		assertEquals("", sut.getCurrentFilter());
	}

	@Test
	public void testHasPreviousFilter_Empty() {
		FilterMemory sut = new FilterMemory();
		assertEquals(false, sut.hasPreviousFilter());
	}

	@Test
	public void testHasNextFilter_Empty() {
		FilterMemory sut = new FilterMemory();
		assertEquals(false, sut.hasNextFilter());
	}

	@Test
	public void testHasPreviousFilter_OneElement() {
		FilterMemory sut = new FilterMemory();
		sut.addFilter("f1");
		assertEquals(false, sut.hasPreviousFilter());
	}

	@Test
	public void testHasNextFilter_OneElement() {
		FilterMemory sut = new FilterMemory();
		sut.addFilter("f1");
		assertEquals(false, sut.hasNextFilter());
	}

	@Test
	public void testSize_Empty() {
		FilterMemory sut = new FilterMemory();
		assertEquals(0, sut.size());
	}

}
