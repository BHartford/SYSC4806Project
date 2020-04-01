package Model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import Model.Book;
import Model.BookRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTest {

    private static final String BOOK_NAME = "The Book";
    private static final String AUTHOR_NAME = "The Author";
    private static final double PRICE = 100;
    private static final int EXPECTED_NUMBER_OF_BOOKS = 1;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository repository;

    @Test
    public void testFindByTitle() {

        entityManager.persist(new Book(BOOK_NAME, AUTHOR_NAME, PRICE));

        List<Book> books = repository.findByTitle(BOOK_NAME);

        assertEquals(EXPECTED_NUMBER_OF_BOOKS, books.size());

        assertThat(books).extracting(Book::getTitle).containsOnly(BOOK_NAME);

    }

    @Test
    public void testFindByAuthor() {

        entityManager.persist(new Book(BOOK_NAME, AUTHOR_NAME, PRICE));

        List<Book> books = repository.findByAuthor(AUTHOR_NAME);

        assertEquals(EXPECTED_NUMBER_OF_BOOKS, books.size());

        assertThat(books).extracting(Book::getAuthor).containsOnly(AUTHOR_NAME);

    }

}
