package Model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import Model.Book;

public class BookTest {

	@Test
	public void testOptionalValues() {
		Book book = new Book("title", "author", 10.99);

		assertEquals("title", book.getTitle());
		assertEquals("author", book.getAuthor());
		assertEquals(10.99, book.getPrice());
		assertEquals(Book.DEFAULT_DESCRIPTION, book.getDescription());
		assertEquals(Book.DEFAULT_QUANTITY, book.getQuantity());
		assertEquals(Book.DEFAULT_YEAR, book.getYear());
	}

	@Test
	public void testCustomValues() {
		Book book = new Book("title", "author", 2020, "description", 10.99, 3, 4.5, 2);

		assertEquals("title", book.getTitle());
		assertEquals("author", book.getAuthor());
		assertEquals(10.99, book.getPrice());
		assertEquals("description", book.getDescription());
		assertEquals(3, book.getQuantity());
		assertEquals(2020, book.getYear());
		assertEquals(4.5, book.getRating());
	}

	@Test
	public void testCustomAndDefaultValues() {
		Book book = new Book("title", "author", null, null, 10.99, 3, null, 0);

		assertEquals("title", book.getTitle());
		assertEquals("author", book.getAuthor());
		assertEquals(10.99, book.getPrice());
		assertEquals(Book.DEFAULT_DESCRIPTION, book.getDescription());
		assertEquals(3, book.getQuantity());
		assertEquals(Book.DEFAULT_YEAR, book.getYear());
		assertEquals(Book.DEFAULT_RATING, book.getRating());
		
	}
}
