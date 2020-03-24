package dataModel;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ReceiptRepositoryTest {

    private static final String USERNAME = "buyer";
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

    @BeforeEach
    public void setup() {
        User user = new User(USERNAME, PASSWORD);
        Book book = new Book(BOOK_NAME, AUTHOR_NAME, PRICE);

        entityManager.persist(user);
        entityManager.persist(book);
        entityManager.persist(new Receipt(user, Arrays.asList(book)));
    }

    @Test
    public void testFindByUser() {
        User user = userRepository.findAll().iterator().next();

        List<Receipt> receipt = receiptRepository.findByUser(user);

        assertNotNull(receipt);
        assertEquals(1, receipt.size());
    }

    @Test
    public void testFindByDate() throws ParseException {
        Date currDate = new SimpleDateFormat("yyyy-MM-dd").parse(java.time.LocalDate.now().toString());

        List<Receipt> receipt = receiptRepository.findByDate(currDate);

        assertNotNull(receipt);
        assertEquals(1, receipt.size());
    }
}