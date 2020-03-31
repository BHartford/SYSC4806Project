package dataModel;

import org.junit.jupiter.api.Test;

import Model.Book;
import Model.Receipt;
import Model.User;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ReceiptTest {

    private static final String BOOK_TITLE = "Refactoring";
    private static final String BOOK_AUTHOR = "Martin Fowler";
    private static final double BOOK_PRICE1 = 99.99;
    private static final double BOOK_PRICE2 = 67.98;
    private static final double BOOK_PRICE3 = 21.97;

    @Test
    void testCreateReceiptNoItems() {
        User user = new User();
        Receipt receipt = new Receipt(user);

        assertEquals(user, receipt.getUser());
        assertNotNull(receipt.getItems());
    }

    @Test
    void testCreateReceiptHasItems() {
        User user = new User();
        Book book1 = new Book();
        Book book2 = new Book();

        List<Book> books = Arrays.asList(book1, book2);

        Receipt receipt = new Receipt(user, books);

        assertEquals(user, receipt.getUser());
        assertEquals(books, receipt.getItems());
    }

    @Test
    void addReceiptAddOneItem() {
        User user = new User();
        Book book1 = new Book();

        Receipt receipt = new Receipt(user);

        assertEquals(0, receipt.getItemCount());
        receipt.addItems(book1);
        assertEquals(1, receipt.getItemCount());
        assertThat(receipt.getItems().contains(book1));

    }

    @Test
    void addReceiptAddMultipleItems() {
        User user = new User();
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();

        Receipt receipt = new Receipt(user);

        assertEquals(0, receipt.getItems().size());
        receipt.addItems(book1, book2, book3);
        assertEquals(3, receipt.getItems().size());

    }

    @Test
    void getTransactionItemsPriceNoItem() {
        User user = new User();

        Receipt receipt = new Receipt(user);

        assertEquals(0, receipt.getTotal());
    }

    @Test
    void getTransactionItemsPriceSingleItem() {
        User user = new User();
        Book book = new Book(BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE1);

        List<Book> books = Arrays.asList(book);

        Receipt receipt = new Receipt(user, books);

        assertEquals(book.getPrice(), receipt.getTotal());
    }

    @Test
    void getTransactionItemsPriceMultipleItem() {
        User user = new User();
        Book book1 = new Book(BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE1);
        Book book2 = new Book(BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE2);
        Book book3 = new Book(BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE3);

        List<Book> books = Arrays.asList(book1, book2, book2, book3);
        double expectedPrice = book1.getPrice() + book2.getPrice() * 2 + book3.getPrice();

        Receipt receipt = new Receipt(user, books);

        assertEquals(expectedPrice, receipt.getTotal());
    }
}