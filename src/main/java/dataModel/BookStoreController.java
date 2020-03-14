package dataModel;

import Logging.LoggingLibrary;
import dataModel.BookRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookStoreController {

    @Autowired
    private BookRepository repository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "add_book";

    @GetMapping("/")
    public String landingPage(Model model){
        return index(model);
    }

    @GetMapping("/view")
    public String index(Model model) {
        model.addAttribute("books", repository.findAll());
        return "index";
    }

    @GetMapping("/viewbook")
    public String display(Model model, @RequestParam(value = "bookID") long bookID) {
        model.addAttribute("books", repository.findById(bookID));
        return "viewbook";
    }

    @GetMapping("/cart")
    public String showCart(Model model, @RequestParam(value = "books") List<String> books) {
    	List<Long> bookIds = books.stream().map(Long::parseLong).collect(Collectors.toList());
        model.addAttribute("books", repository.findByIdIn(bookIds.toArray(new Long[bookIds.size()])));
        return "viewCart";
    }

    @PostMapping("/addbook")
    public String display(Model model, @ModelAttribute Book newBook) {
        kafkaTemplate.send(TOPIC, LoggingLibrary.getTime() + newBook.toString());
    	repository.save(newBook);
        model.addAttribute("books", repository.findAll());
        model.addAttribute("newBook", null);
        return "index";
    }
    
    @GetMapping("/addbook")
    public String directToAddBook(Model model) {
    	model.addAttribute("newBook", new Book());
        return "addbook";
    }
    
    @PostMapping("/searchByTitle")
    public String titleSearch(Model model, @RequestParam(value = "title") String title) {
        model.addAttribute("books", repository.findByTitle(title));
        return "viewbook";
    }
    
    @PostMapping("/searchByAuthor")
    public String authorSearch(Model model, @RequestParam(value = "author") String author) {
        model.addAttribute("books", repository.findByAuthor(author));
        return "viewbook";
    }
}
