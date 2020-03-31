package Model;

import org.junit.jupiter.api.Test;

import Model.User;

import static Model.User.BUYER;
import static Model.User.SELLER;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTest {

    private static final String BUYER_USERNAME = "admin";
    private static final String SELLER_USERNAME = "guest";
    private static final String PASSWORD_ONE = "password1";
    private static final String PASSWORD_TWO = "password2";

    @Test
    public void testSeller() {
        User user = new User(SELLER_USERNAME, PASSWORD_ONE, SELLER);

        assertEquals(SELLER_USERNAME, user.getUsername());
        assertEquals(PASSWORD_ONE, user.getPassword());
        assertEquals(SELLER, user.getTypeOfUser());
    }

    @Test
    public void testBuyer() {
        User user = new User(BUYER_USERNAME, PASSWORD_TWO, BUYER);

        assertEquals(BUYER_USERNAME, user.getUsername());
        assertEquals(PASSWORD_TWO, user.getPassword());
        assertEquals(BUYER, user.getTypeOfUser());
    }

    @Test
    public void testBuyerOptionalValue() {
        User user = new User(BUYER_USERNAME, PASSWORD_TWO);

        assertEquals(BUYER_USERNAME, user.getUsername());
        assertEquals(PASSWORD_TWO, user.getPassword());
        assertEquals(BUYER, user.getTypeOfUser());
    }
}
