package dataModel;

import Logging.LoggingLibrary;
import dataModel.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class BookStoreController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "add_book";

    @GetMapping("/")
    public String landingPage(Model model){
        return index(model);
    }

    @GetMapping("/view")
    public String index(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "index";
    }

    @GetMapping("/viewbook")
    public String display(Model model, @RequestParam(value = "bookID") long bookID) {
        model.addAttribute("books", bookRepository.findById(bookID));
        return "viewbook";
    }

    @GetMapping("/cart")
    public String showCart(Model model, @RequestParam(value = "books") List<String> books) {
    	List<Long> bookIds = books.stream().map(Long::parseLong).collect(Collectors.toList());
        model.addAttribute("books", bookRepository.findByIdIn(bookIds.toArray(new Long[bookIds.size()])));
        return "viewCart";
    }

    @PostMapping("/addbook")
    public String display(Model model, @ModelAttribute Book newBook) {
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
        model.addAttribute("books", bookRepository.findByTitle(title));
        return "viewbook";
    }
    
    @PostMapping("/searchByAuthor")
    public String authorSearch(Model model, @RequestParam(value = "author") String author) {
        model.addAttribute("books", bookRepository.findByAuthor(author));
        return "viewbook";
    }
    
    @PostMapping(value="/checkLogin", consumes="application/json", produces="application/json")
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
    
    @PostMapping(value="/purchaseCart", consumes="application/json", produces="application/json")
    @ResponseBody
    public String purchaseCart(@RequestBody String json) {
    	JSONObject jo = new JSONObject(json);
        List<User> users = userRepository.findByUsername(jo.getString("username"));
        String[] cart = jo.getString("cart").split(",");
        Long[] result = new Long[cart.length];
        
        for (int i = 0; i < cart.length; i++) {
           result[i] = Long.parseLong(cart[i]);
        }
        
        List<Book> books = bookRepository.findByIdIn(result);
        users.get(0).addPurchase(books);
        System.out.println(users.get(0).getPurchaseHistory().toString());
        
        JSONObject resp = new JSONObject();
        resp.put("result", result.length);
    	return resp.toString();
    }
}
