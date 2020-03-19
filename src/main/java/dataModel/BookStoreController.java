package dataModel;

import Logging.LoggingLibrary;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("newBook")
public class BookStoreController {

    private static final String TOPIC = "add_book";
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/")
    public String landingPage(Model model) {
        return index(model);
    }

    @GetMapping("/view")
    public String index(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "index";
    }

    @GetMapping("/viewbook")
    public String display(Model model, @RequestParam(value = "bookID") long bookID) {
        Book b = null;

        try {
            b = bookRepository.findById(bookID);
        } catch (Exception e) {
            //TODO Log this
            //Requires a valid IDNumber
        }

        if (b != null) {
            model.addAttribute("books", bookRepository.findById(bookID));
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

    @GetMapping("/cart")
    public String showCart(Model model, @RequestParam(value = "books") List<String> books) {
        List<Long> bookIds = books.stream().map(Long::parseLong).collect(Collectors.toList());
        model.addAttribute("books", bookRepository.findByIdIn(bookIds.toArray(new Long[bookIds.size()])));
        return "viewCart";
    }

    @PostMapping("/addbook")
    public String addBookToRepo(Model model, @ModelAttribute("newBook") Book newBook) {
        kafkaTemplate.send(TOPIC, LoggingLibrary.getTime() + newBook.toString());
        bookRepository.save(newBook);
        model.addAttribute("books", bookRepository.findAll());
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
        List<Book> books = bookRepository.findByTitle(title);

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
        List<Book> books = bookRepository.findByAuthor(author);

        if (books.size() != 0) {
            model.addAttribute("books", books);
            return "viewbook";
        } else {
            String errorMessage = String.format(ApplicationMsg.QUERY_NOT_FOUND.getMsg(), author);
            return isNotFound(model, errorMessage);
        }
    }

    @PostMapping(value = "/checkLogin", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String loginCheck(@RequestBody String json) {
        System.out.println(json);
        JSONObject jo = new JSONObject(json);
        List<User> users = userRepository.findByUsername(jo.getString("username"));
        JSONObject resp = new JSONObject();
        try {
            Boolean correctPass = users.get(0).validPassword(jo.getString("password"));
            System.out.println("." + users.get(0).getPassword() + ".");
            System.out.println("." + jo.getString("password") + ".");
            System.out.println(users.get(0).validPassword(jo.getString("password")));
            resp.put("result", correctPass);
            resp.put("type", users.get(0).getTypeOfUserString());
        } catch (IndexOutOfBoundsException e) {
            resp.put("result", false);
        }
        System.out.println(resp.toString());
        return resp.toString();
    }
}
