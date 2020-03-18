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
    		totalCost += b.getPrice() * quantity;
    		totalBooks += quantity;
    		bookList.add(b);
    	}
        model.addAttribute("books", bookList);
        model.addAttribute("quantities", quantities);
        model.addAttribute("totalCost", "$" + df.format(totalCost));
        model.addAttribute("totalBooks", (int) totalBooks);
        return "viewCart";
    }
    
    @GetMapping("/viewPurchaseHistory")
    public String showPurchaseHistory(Model model, @RequestParam(value = "user") String user) {
    	model.addAttribute("history", userRepository.findByUsername(user).get(0).getPurchaseHistory());
    	return "viewPurchaseHistory";
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
    	System.out.println(json);
        List<User> users = userRepository.findByUsername(jo.getString("username"));
        String[] cart = jo.getString("cart").split(",");
        String[] quantities = jo.getString("quantities").split(",");
        
        ArrayList<Long> bookIDList = new ArrayList<Long>();
        
        // Get list of books so we can grab them in bulk from the repository
        for (int i = 0; i < cart.length; i++) {
           Long rawID = Long.parseLong(cart[i]);
           for(int j=0; j < Integer.parseInt(quantities[i]); j++) { //Add one instance of the ID to the array for each copy purchased.
        	   bookIDList.add(rawID);
           }
        }
        //Create a Array of Longs. toArray() doesn't work here bc of primitive types. I wish there was a better way. Please find a way make this better. 
        Long[] bookLongArray = new Long[bookIDList.size()];
        for(int i = 0; i < bookLongArray.length; i++)
        {
        	bookLongArray[i]=bookIDList.get(i);
        }
        
        List<Book> books = bookRepository.findByIdIn(bookLongArray);
        users.get(0).addPurchase(books); //This only adds one of each book - doesn't look at quantities. Sorry I'm tired.
        System.out.println(users.get(0).getPurchaseHistory().toString());
        
        JSONObject resp = new JSONObject();
        resp.put("result", bookIDList.size());
    	return resp.toString();
    }
}
