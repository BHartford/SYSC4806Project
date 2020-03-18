package dataModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@SessionAttributes("newBook")
public class BookStoreController {

    @Autowired
    private BookRepository repository;

    @GetMapping("/")
    public String landingPage(Model model) {
        return index(model);
    }

    @GetMapping("/view")
    public String index(Model model) {
        model.addAttribute("books", repository.findAll());
        return "index";
    }

    @GetMapping("/viewbook")
    public String display(Model model, @RequestParam(value = "bookID") long bookID) {
        Book b = null;

        try {
            b = repository.findById(bookID);
        } catch (Exception e) {
            //TODO Log this
            //Requires a valid IDNumber
        }

        if (b != null) {
            model.addAttribute("books", repository.findById(bookID));
            return "viewbook";
        } else {
            String errorMessage = String.format(ApplicationMsg.BAD_BOOK_ID.getMsg(), bookID);
            return isNotFound(model, errorMessage);
        }
    }

    public String isNotFound(Model model, String errorMsg) {
        //TODO Log This -- a non existing search was preformed
        model.addAttribute("errorMsg", errorMsg);
        return index(model);
    }

    @PostMapping("/addbook")
    public String addBookToRepo(Model model, @ModelAttribute("newBook") Book newBook) {
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
        List<Book> books = repository.findByTitle(title);

        if (books.size() != 0) {
            model.addAttribute("books", books);
            return "viewbook";
        } else {
            String errorMessage = String.format(ApplicationMsg.QUERY_NOT_FOUND.getMsg(), title);
            return isNotFound(model, errorMessage);
        }
    }

    @PostMapping("/searchByAuthor")
    public String authorSearch(Model model, @RequestParam(value = "author") String author) {
        List<Book> books = repository.findByAuthor(author);

        if (books.size() != 0) {
            model.addAttribute("books", books);
            return "viewbook";
        } else {
            String errorMessage = String.format(ApplicationMsg.QUERY_NOT_FOUND.getMsg(), author);
            return isNotFound(model, errorMessage);
        }
    }

    //TODO Add advanced error handling / validation
}
