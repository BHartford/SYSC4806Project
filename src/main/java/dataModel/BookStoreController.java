package dataModel;

import Logging.LoggingLibrary;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
@SessionAttributes("newBook")
public class BookStoreController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final String TOPIC = "add_book";

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

    @GetMapping("/public/signup")
    public String signup(Model model) {
        return "signup";
    }

    @PostMapping(value = "/public/checkSignup", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String checkSignup(@RequestBody String json) {
        JSONObject jo = new JSONObject(json);
        String username = jo.getString("username");
        Boolean doesUsernameExist = userRepository.findByUsername(username).isEmpty();
        JSONObject resp = new JSONObject();
        resp.put("result", doesUsernameExist);
        return resp.toString();
    }

    @PostMapping("/public/signup")
    public String signup(Model model, @ModelAttribute("username") String username, @ModelAttribute("password") String password, @ModelAttribute("type") String type) {
        int userType;
        if ("buyer".equals(type)){
            userType = User.BUYER;
        } else {
            userType = User.SELLER;
        }
        User user = new User(username, password, userType);
        userRepository.save(user);
        return index(model);
    }

    @GetMapping("/public/viewbook")
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

    @GetMapping("/private/cart")
    public String showCart(Model model, @RequestParam(value = "books") List<String> books, @RequestParam(value = "quantities") List<String> quantities, @RequestParam(value = "userId") String userId) {
        if (!StringUtils.isEmpty(userId)){
            User user = userRepository.findById(Long.parseLong(userId));
            if (Objects.isNull(user)){
                return index(model);
            }
        } else {
            return index(model);
        }
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

    @PostMapping("/private/addbook")
    public String addBookToRepo(Model model, @ModelAttribute("newBook") Book newBook) {
        //kafkaTemplate.send(TOPIC, LoggingLibrary.getTime() + newBook.toString());
        bookRepository.save(newBook);
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("newBook", null);
        return "index";
    }

    @GetMapping("/private/addbook")
    public String directToAddBook(Model model, @RequestParam(value = "userId") String userId) {
        if (!StringUtils.isEmpty(userId)){
            User user = userRepository.findById(Long.parseLong(userId));
            if (Objects.isNull(user)){
                return index(model);
            }
        } else {
            return index(model);
        }
        model.addAttribute("newBook", new Book());
        return "addbook";
    }

    @PostMapping("/public/searchByTitle")
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

    @PostMapping("/public/searchByAuthor")
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

    @PostMapping(value = "/public/checkLogin", consumes = "application/json", produces = "application/json")
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
            resp.put("userID", users.get(0).getId());
            resp.put("result", correctPass);
            resp.put("type", users.get(0).getTypeOfUserString());
        } catch (IndexOutOfBoundsException e) {
            resp.put("result", false);
        }
        System.out.println("Response for login: " + resp.toString());
        return resp.toString();
    }

    @PostMapping(value = "/private/purchaseCart", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public String purchaseCart(Model model, @RequestBody String json) {
        JSONObject jo = new JSONObject(json);
        System.out.println(json);
        User user = userRepository.findByUsername(jo.getString("username")).get(0);
        if (Objects.isNull(user)){
            return index(model);
        }
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
        JSONObject resp = new JSONObject();
        resp.put("result", bookIDList.size());
        resp.put("user", user.getId());
        return resp.toString();
    }

    @GetMapping("/private/viewTransaction")
    public String viewTransaction(Model model, @RequestParam(value = "receipt") String receiptId) {
        Receipt receipt = receiptRepository.findById(Long.parseLong(receiptId));
        model.addAttribute("receipt", receipt);
        return "viewPurchase";
    }

    @GetMapping("/private/viewReceiptHistory")
    public String viewReceiptHistory(Model model, @RequestParam(value = "user") String userID) {
        if (!StringUtils.isEmpty(userID)){
            User user = userRepository.findById(Long.parseLong(userID));
            if (Objects.isNull(user)){
                return index(model);
            }
        } else {
            return index(model);
        }

        User user = userRepository.findById(Long.parseLong(userID));
        List<Receipt> receipts = receiptRepository.findByUser(user);
        model.addAttribute("receiptHistory", receipts);
        return "viewReceiptHistory";
    }
}