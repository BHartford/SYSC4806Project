package dataModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    private static final String BUYER_USERNAME_1 = "test_buyer_1";
    private static final String BUYER_USERNAME_2 = "test_buyer_2";
    private static final String SELLER_USERNAME = "test_seller";
    private static final String PASSWORD_ONE = "password1";
    private static final String PASSWORD_TWO = "password2";
    private static final String BOOK_TITLE_1 = "Refactoring";
    private static final String BOOK_TITLE_2 = "Patterns of Enterprise Application Architecture";
    private static final String BOOK_TITLE_3 = "UML Distilled";
    private static final String BOOK_AUTHOR = "Martin Fowler";
    private static final double BOOK_PRICE = 99.99;
    private static final int EXPECTED_NUMBER_OF_USERS = 1;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername() {
        entityManager.persist(new User(BUYER_USERNAME_1, PASSWORD_ONE, User.BUYER));

        List<User> users = userRepository.findByUsername(BUYER_USERNAME_1);

        assertEquals(EXPECTED_NUMBER_OF_USERS, users.size());

        assertThat(users).extracting(User::getUsername).containsOnly(BUYER_USERNAME_1);
    }

    @Test
    public void testFindByTypeOfUser() {
        entityManager.persist(new User(BUYER_USERNAME_1, PASSWORD_ONE, User.BUYER));
        entityManager.persist(new User(SELLER_USERNAME, PASSWORD_TWO, User.SELLER));

        List<User> users = userRepository.findByTypeOfUser(User.BUYER);

        assertThat(users).extracting(User::getTypeOfUser).containsOnly(User.BUYER);
    }

    @Test
    public void testPurchaseHistoryOfUser() {
        User user1 = new User(BUYER_USERNAME_1, PASSWORD_ONE, User.BUYER);
        Book book1 = new Book(BOOK_TITLE_1, BOOK_AUTHOR, BOOK_PRICE);
        user1.addPurchase(book1);

        entityManager.persist(book1);
        entityManager.persist(user1);

        List<User> users = userRepository.findByUsername(BUYER_USERNAME_1);

        for (User u : users) {
            assert (u.getPurchaseHistory().size() == user1.getPurchaseHistory().size());
            assertThat(u.getPurchaseHistory()).extracting(Book::getTitle).contains(BOOK_TITLE_1);
            assertThat(u.getPurchaseHistory()).extracting(Book::getAuthor).contains(BOOK_AUTHOR);
            assertThat(u.getPurchaseHistory()).extracting(Book::getPrice).contains(BOOK_PRICE);
        }
    }

    @Test
    public void testPurchaseHistoryOfMultipleUser() {
        User user1 = new User(BUYER_USERNAME_1, PASSWORD_ONE, User.BUYER);
        User user2 = new User(BUYER_USERNAME_2, PASSWORD_TWO, User.BUYER);
        Book book1 = new Book(BOOK_TITLE_1, BOOK_AUTHOR, BOOK_PRICE);
        Book book2 = new Book(BOOK_TITLE_2, BOOK_AUTHOR, BOOK_PRICE);
        Book book3 = new Book(BOOK_TITLE_3, BOOK_AUTHOR, BOOK_PRICE);

        user1.addPurchase(book1);
        user1.addPurchase(book2);

        user2.addPurchase(book2);
        user2.addPurchase(book3);

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);
        entityManager.persist(user1);
        entityManager.persist(user2);

        List<User> returnedUser1 = userRepository.findByUsername(BUYER_USERNAME_1);

        for (User u : returnedUser1) {
            assert (u.getPurchaseHistory().size() == user1.getPurchaseHistory().size());
            assertThat(u.getPurchaseHistory()).extracting(Book::getTitle).contains(BOOK_TITLE_1, BOOK_TITLE_2);
            assertThat(u.getPurchaseHistory()).extracting(Book::getAuthor).contains(BOOK_AUTHOR);
            assertThat(u.getPurchaseHistory()).extracting(Book::getPrice).contains(BOOK_PRICE);
        }

        List<User> returnedUser2 = userRepository.findByUsername(BUYER_USERNAME_2);

        for (User u : returnedUser2) {
            assert (u.getPurchaseHistory().size() == user2.getPurchaseHistory().size());
            assertThat(u.getPurchaseHistory()).extracting(Book::getTitle).contains(BOOK_TITLE_2, BOOK_TITLE_3);
            assertThat(u.getPurchaseHistory()).extracting(Book::getAuthor).contains(BOOK_AUTHOR);
            assertThat(u.getPurchaseHistory()).extracting(Book::getPrice).contains(BOOK_PRICE);
        }
    }

}
