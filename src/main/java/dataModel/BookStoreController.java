package dataModel;

import dataModel.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookStoreController {

    @Autowired
    private BookRepository repository;

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

    @PostMapping("/addbook")
    public String display(Model model, @ModelAttribute Book newBook) {
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
        return "viewBook";
    }
    
    @PostMapping("/searchByAuthor")
    public String authorSearch(Model model, @RequestParam(value = "author") String author) {
        model.addAttribute("books", repository.findByAuthor(author));
        return "viewBook";
    }
}
