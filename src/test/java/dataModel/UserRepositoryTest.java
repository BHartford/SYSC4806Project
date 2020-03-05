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

    private static final String BUYER_USERNAME = "test_buyer";
    private static final String SELLER_USERNAME = "test_seller";
    private static final String PASSWORD_ONE = "password1";
    private static final String PASSWORD_TWO = "password2";
    private static final int SELLER = 0;
    private static final int BUYER = 1;
    private static final String BOOK_TITLE = "Refactoring";
    private static final String BOOK_AUTHOR = "Martin Fowler";
    private static final double BOOK_PRICE = 99.99;
    private static final int EXPECTED_NUMBER_OF_USERS = 1;

    @Autowired
    private  TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByUsername(){
        entityManager.persist(new User(BUYER_USERNAME, PASSWORD_ONE, BUYER));

        List<User> users = userRepository.findByUsername(BUYER_USERNAME);

        assertEquals(EXPECTED_NUMBER_OF_USERS, users.size());

        assertThat(users).extracting(User::getUsername).containsOnly(BUYER_USERNAME);
    }

    @Test
    public void testFindByTypeOfUser(){
        entityManager.persist(new User(BUYER_USERNAME, PASSWORD_ONE, BUYER));
        entityManager.persist(new User(SELLER_USERNAME, PASSWORD_TWO, SELLER));

        List<User> users = userRepository.findByTypeOfUser(BUYER);

        assertThat(users).extracting(User::getTypeOfUser).containsOnly(BUYER);
    }

    @Test
    public void testPurchaseHistoryOfUser(){
        User user1 = new User(BUYER_USERNAME, PASSWORD_ONE, BUYER);
        Book book1 = new Book(BOOK_TITLE, BOOK_AUTHOR, BOOK_PRICE);
        user1.addPurchase(book1);

        entityManager.persist(book1);
        entityManager.persist(user1);

        List<User> users = userRepository.findByUsername(BUYER_USERNAME);

        for (User u : users){
            assert(u.getPurchaseHistory().size() == user1.getPurchaseHistory().size());
            assertThat(u.getPurchaseHistory()).extracting(Book::getTitle).contains(BOOK_TITLE);
            assertThat(u.getPurchaseHistory()).extracting(Book::getAuthor).contains(BOOK_AUTHOR);
            assertThat(u.getPurchaseHistory()).extracting(Book::getPrice).contains(BOOK_PRICE);
        }
    }


}
