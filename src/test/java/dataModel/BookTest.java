package dataModel;

import org.junit.jupiter.api.Test;

public class BookTest {
	
	@Test
	public void testOptionalValues() {
		Book book = new Book("title", "author", 10.99);
	}
}
