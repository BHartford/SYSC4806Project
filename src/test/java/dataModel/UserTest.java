package dataModel;

import static dataModel.User.BUYER;
import static dataModel.User.SELLER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import java.util.List;

public class UserTest {

    private static final String BUYER_USERNAME = "admin";
    private static final String SELLER_USERNAME = "guest";
    private static final String PASSWORD_ONE = "password1";
    private static final String PASSWORD_TWO = "password2";
    private static final String BOOK_TITLE = "Refactoring";
    private static final String BOOK_AUTHOR = "Martin Fowler";
    private static final double BOOK_PRICE = 99.99;

    @Test
    public void testSeller(){
        User user = new User(SELLER_USERNAME, PASSWORD_ONE, SELLER);

        assertEquals(SELLER_USERNAME, user.getUsername());
        assertEquals(PASSWORD_ONE, user.getPassword());
        assertEquals(SELLER, user.getTypeOfUser());
        assertTrue(user.getPurchaseHistory().isEmpty());
    }

    @Test
    public void testBuyer(){
        User user = new User(BUYER_USERNAME, PASSWORD_TWO, BUYER);

        assertEquals(BUYER_USERNAME, user.getUsername());
        assertEquals(PASSWORD_TWO, user.getPassword());
        assertEquals(BUYER, user.getTypeOfUser());
        assertTrue(user.getPurchaseHistory().isEmpty());
    }

    @Test
    public void testBuyerOptionalValue(){
        User user = new User(BUYER_USERNAME, PASSWORD_TWO);

        assertEquals(BUYER_USERNAME, user.getUsername());
        assertEquals(PASSWORD_TWO, user.getPassword());
        assertEquals(BUYER, user.getTypeOfUser());
        assertTrue(user.getPurchaseHistory().isEmpty());
    }

    @Test
    public void testPurchaseBookHistory(){
        User user = new User(BUYER_USERNAME, PASSWORD_TWO);
        Book book = new Book(BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE);

        user.addPurchase(book);

        List<Book> bookFromPurchaseHistory = user.getPurchaseHistory();

        for (Book b : bookFromPurchaseHistory){
            assertEquals(BOOK_TITLE, b.getTitle());
            assertEquals(BOOK_AUTHOR, b.getAuthor());
            assertEquals(BOOK_PRICE, b.getPrice());
        }
    }


}
