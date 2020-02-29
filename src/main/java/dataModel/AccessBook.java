package dataModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Array;

@SpringBootApplication
public class AccessBook {

    private static final Logger log = LoggerFactory.getLogger(AccessBook.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessBook.class);
    }

    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {

            Book book1 = new Book("Book 1", "Chris", 22);
            Book book2 = new Book("Book 2", "Callum", 10);

            repository.save(book1);
            repository.save(book2);

            for (Book b : repository.findAll()) {
                log.info(b.toString());
            }
        };

    }
}
