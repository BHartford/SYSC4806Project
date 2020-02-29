package dataModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccessBook implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AccessBook.class);

    @Autowired
    private BookRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(AccessBook.class, args);
    }

    @Override
    public void run(String... args) {

        Book book1 = new Book("Book 1", "Chris", 22);
        Book book2 = new Book("Book 2", "Callum", 10);

        repository.save(book1);
        repository.save(book2);

        for (Book book : repository.findAll()) {
            log.info(book.toString());
        }

    }
}
