package dataModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
public class AccessBookStore {

    private static final Logger log = LoggerFactory.getLogger(AccessBookStore.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessBookStore.class);
    }

    @Bean
    public CommandLineRunner demo(BookRepository bookRepository, UserRepository userRepository) {
        return (args) -> {

			if(!userRepository.findAll().iterator().hasNext()) { //Repo Empty - Init
				User user1 = new User("seller", "seller123", User.SELLER);
				User user2 = new User("buyer", "buyer123", User.BUYER);

				userRepository.save(user1);
				userRepository.save(user2);
			}

            if(!bookRepository.findAll().iterator().hasNext()) { //Repo Empty - Init
                ArrayList<Book> inventory = new ArrayList<Book>(Arrays.asList(
                		new Book("Harry Potter and the Sorcerer's Stone", "J.K. Rowling", 1997, 
                        		"The first novel in the Harry Potter series and Rowling's debut novel, it follows Harry Potter, a young wizard who"
                        		+ " discovers his magical heritage on his eleventh birthday, when he receives a letter of acceptance to Hogwarts.",
                        		7.99, 34),
                        new Book("Harry Potter and the Chamber of Secrets", "J.K. Rowling", 1998,
                        		"The plot follows Harry's second year at Hogwarts School of Witchcraft and Wizardry, during which a series of "
                        		+ "messages on the walls of the school's corridors warn that the Chamber of Secrets has been opened.",
                        		9.99, 22),
                        new Book("Harry Potter and the Prisoner of Azkaban", "J.K. Rowling", 1999,
                        		"The book follows Harry Potter, a young wizard, in his third year at Hogwarts School of Witchcraft and Wizardry. Harry"
                        		+ " investigates Sirius Black, an escaped prisoner from Azkaban, believed to be one of Lord Voldemort's old allies",
                        		11.99, 31),
                        new Book("Harry Potter and the Goblet of Fire", "J.K. Rowling", 2000,
                        		"It follows Harry Potter, a wizard in his fourth year at Hogwarts School of Witchcraft and Wizardry, and the mystery "
                        		+ "surrounding the entry of Harry's name into the Triwizard Tournament, in which he is forced to compete.",
                        		12.99, 16),
                        new Book("Design Patterns: Elements of Reusable Object-Oriented Software", "Gang of Four", 1994,
                        		"Design Patterns is a modern classic in the literature of object-oriented development, offering timeless and elegant "
                        		+ "solutions to common problems in software design.",
                        		22.67, 5),
                        new Book("Alias Grace", "Margaret Atwood", 1996,
                        		"The story fictionalizes the notorious 1843 murders of Thomas Kinnear and his housekeeper Nancy Montgomery in Canada"
                        		+ " West. Two servants of the Kinnear household, Grace Marks and James McDermott, were convicted of the crime.",
                        		19.99, 17),
                        new Book("Under The Dome", "Stephen King", 2009,
                        		"Set in and around a small Maine town, it tells an intricate, multi-character story of how the town's inhabitants "
                        		+ "contend with the calamity of being suddenly cut off from the outside world by an invisible barrier that drops out of the sky",
                        		16.99, 9),
                        new Book("American Gods", "Neil Gaiman", 2001,
                        		"Shadow is an ex-convict who is released from prison three days early when his wife Laura is killed in a car accident."
                        		+ " He takes a job as a bodyguard for a mysterious con man, Mr. Wednesday, and travels with him across America.",
                        		16.99, 45),
                		new Book("The Alchemist", "Paulo Coelho", 1993,
                				"The Alchemist follows the journey of an Andalusian shepherd boy named Santiago. Believing a recurring dream to be prophetic, "
                				+ "he asks a Gypsy fortune teller in the nearby town about its meaning.",
                				9.99, 27),
                		new Book("The Hitchhiker's Guide To The Galaxy", "Douglas Adams", 1979,
                				"The broad narrative of Hitchhiker follows the misadventures of the last surviving man, Arthur Dent, following the demolition "
                				+ "of the Earth by a Vogon constructor fleet to make way for a hyperspace bypass.",
                				11.99, 16),
                		new Book("The Handmaid's Tale", "Margaret Atwood", 1985,
                				"The Handmaid's Tale explores themes of subjugated women in a patriarchal society and the various means by which these women "
                				+ "resist and attempt to gain individuality and independence.",
                				13.99, 48),
                		new Book("The Testaments", "Margaret Atwood", 2019,
                				"The novel is set 15 years after the events of The Handmaid's Tale. It is narrated by Aunt Lydia, a character from the previous "
                				+ "novel; Agnes, a young woman living in Gilead; and Daisy, a young woman living in Canada.",
                				17.99, 88),
                		new Book("Norse Mythology", "Neil Gaiman", 2017,
                				"The book is Gaiman's retelling of several stories from Norse mythology. These stories include the theft of Thor's hammer, the "
                				+ "binding of Fenrir and other tales about the Aesir.",
                				14.99, 8),
                		new Book("Meditations", "Marcus Aurelius", 180,
                				"Meditations is a series of personal writings by Marcus Aurelius, Roman Emperor from 161 to 180 AD, recording his private notes "
                				+ "to himself and ideas on Stoic philosophy.",
                				9.99, 3),
                		new Book("The Man In The High Castle", "Phillip K. Dick", 1962,
                				" Published and set in 1962, the novel takes place fifteen years after a different end to World War II, and depicts intrigues "
                				+ "between the victorious Axis Powers — primarily, Japan and Nazi Germany — as they rule over the former United States",
                				13.99, 21),
                		new Book("Nineteen Eighty-Four", "George Orwell", 1949,
                				"The story takes place in an imagined future, the year 1984, when much of the world has fallen victim to perpetual war,"
                				+ "omnipresent government surveillance, historical negationism, and propaganda.",
                				9.99, 25)
                		));

                for (Book b : inventory) {
                    bookRepository.save(b);
                }
            }

            for (Book b : bookRepository.findAll()) {
                log.info(b.toString());
            }


			for (User u : userRepository.findAll()) {
				log.info(u.toString());
			}
        };

    }
}
