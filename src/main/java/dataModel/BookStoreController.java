package dataModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import Logging.LoggingLibrary;

@Controller
@SessionAttributes("newBook")
public class BookStoreController {
	@Value("${kafka.logging}")
	private boolean kafkaLogging;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String ADD_BOOK_TOPIC = "add_book";
    private static final String PURCHASE_TOPIC = "purchase";

    @GetMapping("/")
    public String landingPage(Model model) {
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
    public String showCart(Model model, @RequestParam(value = "books") List<String> books, @RequestParam(value = "quantities") List<String> quantities) {

        double totalCost = 0;
        double totalBooks = 0;
        int quantity;
        ArrayList<Book> bookList = new ArrayList<Book>();
        DecimalFormat df = new DecimalFormat("0.00");

        for (int i = 0; i < books.size(); i++) {
            quantity = Integer.parseInt(quantities.get(i));
            Book b = bookRepository.findById(Long.parseLong(books.get(i)));
            if (b != null) {
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
    public String addBookToRepo(Model model, @ModelAttribute("newBook") Book newBook, @ModelAttribute("user") String user) {
        if (kafkaLogging) kafkaTemplate.send(ADD_BOOK_TOPIC, LoggingLibrary.newBookLog(newBook, user));
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
        JSONObject jo = new JSONObject(json);
        List<User> users = userRepository.findByUsername(jo.getString("username"));
        JSONObject resp = new JSONObject();
        
        try {
            Boolean correctPass = users.get(0).validPassword(jo.getString("password"));
            System.out.println("." + users.get(0).getPassword() + ".");
            System.out.println("." + jo.getString("password") + ".");
            System.out.println(users.get(0).validPassword(jo.getString("password")));
            resp.put("userID", users.get(0).getId());
            resp.put("result", correctPass);
            resp.put("type", users.get(0).getTypeOfUserString());
        } catch (IndexOutOfBoundsException e) {
            resp.put("result", false);
        }
        
        return resp.toString();
    }

    @PostMapping(value = "/purchaseCart", consumes = "application/json", produces = "application/json")
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
            for (int j = 0; j < Integer.parseInt(quantities[i]); j++) {
                bookIDList.add(rawID);
            }
        }

        //Init new Receipt for user
        Receipt receipt = new Receipt(user);
        //Add to purchase history and reduce inventory
        for (long id : bookIDList) {
            Book b = bookRepository.findById(id);
            b.setQuantity(b.getQuantity() - 1);
            bookRepository.save(b);
            receipt.addItems(b);
        }
        
        receiptRepository.save(receipt);
        if (kafkaLogging) kafkaTemplate.send(PURCHASE_TOPIC, LoggingLibrary.purchaseLog(receipt, user.getUsername()));

        JSONObject resp = new JSONObject();
        resp.put("result", bookIDList.size());
        resp.put("user", user.getId());
        return resp.toString();
    }

    @GetMapping("/viewTransaction")
    public String viewTransaction(Model model, @RequestParam(value = "receipt") String receiptId) {
        Receipt receipt = receiptRepository.findById(Long.parseLong(receiptId));
        model.addAttribute("receipt", receipt);
        return "viewPurchase";
    }

    @GetMapping("/viewReceiptHistory")
    public String viewReceiptHistory(Model model, @RequestParam(value = "user") String userID) {
        User user = userRepository.findById(Long.parseLong(userID));
        List<Receipt> receipts = receiptRepository.findByUser(user);
        model.addAttribute("receiptHistory", receipts);
        return "viewReceiptHistory";
    }
}