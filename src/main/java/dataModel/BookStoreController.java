package dataModel;

import Logging.LoggingLibrary;
import dataModel.BookRepository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<Book> listedBooks = (List<Book>) bookRepository.findAll();
        listedBooks.removeIf(book -> book.getQuantity() == 0);
        model.addAttribute("books", listedBooks);
        return "index";
    }

    @GetMapping("/viewbook")
    public String display(Model model, @RequestParam(value = "bookID") long bookID) {
        model.addAttribute("books", bookRepository.findById(bookID));
        return "viewbook";
    }

    @GetMapping("/cart")
    public String showCart(Model model,  @RequestParam(value="books") List<String> books, @RequestParam(value="quantities") List<String>quantities) {
    	
    	double totalCost = 0;
    	double totalBooks = 0;
    	int quantity;
    	ArrayList<Book> bookList = new ArrayList<Book>();
    	DecimalFormat df = new DecimalFormat("0.00");
    	
    	for(int i=0; i< books.size(); i++) {
    	    quantity = Integer.parseInt(quantities.get(i));
    		Book b = bookRepository.findById(Long.parseLong(books.get(i)));
    		if(b != null) {
	    		totalCost += b.getPrice() * quantity;
	    		totalBooks += quantity;
	    		bookList.add(b);
    		}
    	}
        model.addAttribute("books", bookList);
        model.addAttribute("quantities", quantities);
        model.addAttribute("totalCost", "$" + df.format(totalCost));
        model.addAttribute("totalBooks", (int) totalBooks);
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
        System.out.println("Response for login: " + resp.toString());
        return resp.toString();
    }
    
    @PostMapping(value="/purchaseCart", consumes="application/json", produces="application/json")
    @ResponseBody
    public String purchaseCart(Model model, @RequestBody String json) {
    	JSONObject jo = new JSONObject(json);
    	System.out.println(json);
        User user = userRepository.findByUsername(jo.getString("username")).get(0);
        String[] cart = jo.getString("cart").split(",");
        String[] quantities = jo.getString("quantities").split(",");
        ArrayList<Long> bookIDList = new ArrayList<Long>();
        
        //Add one instance of the ID to bookListID for each copy purchased.
        for (int i = 0; i < cart.length; i++) {
           Long rawID = Long.parseLong(cart[i]);
           for(int j=0; j < Integer.parseInt(quantities[i]); j++) { 
        	   bookIDList.add(rawID);
           }
        } 
        //Add to purchase history and reduce inventory
        for(long id: bookIDList)
        {
        	Book b = bookRepository.findById(id);
        	b.setQuantity(b.getQuantity() - 1);
        	bookRepository.save(b);
        	user.addPurchase(b);
        }
        
        userRepository.save(user);
        JSONObject resp = new JSONObject();
        resp.put("result", bookIDList.size());
        resp.put("user", user.getId());
    	return resp.toString();
    }
    
    @GetMapping("/viewPurchaseHistory")
    public String viewPurchaseHistory1(Model model, @RequestParam(value = "user") String userID) {
	    User user = userRepository.findById(Long.parseLong(userID));
	    model.addAttribute("purchaseHistory", user.getPurchaseHistory());
        return "viewPurchaseHistory";
    }
}
