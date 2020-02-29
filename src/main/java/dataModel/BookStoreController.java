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
    public String display(Model model) {
        model.addAttribute("books", repository.findAll());
        return "addbook";
    }
}
