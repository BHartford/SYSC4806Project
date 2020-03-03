package dataModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class AccessBook {

    private static final Logger log = LoggerFactory.getLogger(AccessBook.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessBook.class);
    }

    @Bean
    public CommandLineRunner demo(BookRepository repository) {
        return (args) -> {

            if(!repository.findAll().iterator().hasNext()) { //Repo Empty - Init
                ArrayList<Book> inventory = new ArrayList<Book>(Arrays.asList(
                        new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", 7.99),
                        new Book("Harry Potter and the Chamber of Secrets", "J.K. Rowling", 9.99),
                        new Book("Harry Potter and the Prisoner of Azkaban", "J.K. Rowling", 11.99),
                        new Book("Harry Potter and the Goblet of Fire", "J.K. Rowling", 12.99),
                        new Book("Design Patterns: Elements of Reusable Object-Oriented Software", "Gang of Four", 22.67)));

                for (Book b : inventory) {
                    repository.save(b);
                }
            }

            for (Book b : repository.findAll()) {
                log.info(b.toString());
            }
        };

    }
}
