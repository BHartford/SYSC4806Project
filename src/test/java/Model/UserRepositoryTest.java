package Model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import Model.User;
import Model.UserRepository;

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

}
