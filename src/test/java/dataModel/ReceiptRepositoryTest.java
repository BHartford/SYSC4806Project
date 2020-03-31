package dataModel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import Model.Book;
import Model.BookRepository;
import Model.Receipt;
import Model.ReceiptRepository;
import Model.User;
import Model.UserRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReceiptRepositoryTest {

    private static final String USERNAME = "receiptBuyer";
    private static final String PASSWORD = "buyer123";
    private static final String BOOK_NAME = "The Book";
    private static final String AUTHOR_NAME = "The Author";
    private static final double PRICE = 100;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    User user = new User(USERNAME, PASSWORD);
    Book book = new Book(BOOK_NAME, AUTHOR_NAME, PRICE);

    @Before
    public void setUp() {
        entityManager.persist(user);
        entityManager.persist(book);
        entityManager.persist(new Receipt(user, Arrays.asList(book)));
    }

    @Test
    public void testFindByUser() {
        List<User> users = userRepository.findByUsername(USERNAME);
        User user = users.iterator().next();

        List<Receipt> receipts = receiptRepository.findByUser(user);

        assertNotNull(receipts);
        assertEquals(1, receipts.size());
        for (Receipt r : receipts) {
            assertThat(r.getItems().contains(book));
        }
    }

    @Test
    public void testFindByDate() throws ParseException {
        Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(java.time.LocalDate.now().toString());

        List<Receipt> receipts = receiptRepository.findByDate(currDate);

        assertNotNull(receipts);
        assertEquals(1, receipts.size());
        for (Receipt r : receipts) {
            assertThat(r.getItems().contains(book));
        }
    }
}