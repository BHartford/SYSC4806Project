package dataModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class User {

    private static final int SELLER = 0;
    private static final int BUYER = 1;

    @Id
    @GeneratedValue
    long id;
    private String username;
    private String password;
    private int typeOfUser;
    @OneToMany
    private List<Book> purchaseHistory;

    public User() {}

    public User(String username, String password, int typeOfUser){
        this.username = username;
        this.password = password;
        this.typeOfUser = typeOfUser;
        this.purchaseHistory = new ArrayList<Book>();
    }

    public User(String username, String password) {
        this(username, password, BUYER);
    }

    public long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTypeOfUser() {
        return this.typeOfUser;
    }

    public void setTypeOfUser(int typeOfUser) {
        this.typeOfUser = typeOfUser;
    }

    public String getTypeOfUserString() {
        return ((this.typeOfUser == BUYER) ? "Buyer" : "Seller");
    }

    public List<Book> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void addPurchase(Book purchase) {
        purchaseHistory.add(purchase);
    }

    public void addPurchase(List<Book> purchase) {
        for (Book b : purchase){
            purchaseHistory.add(b);
        }
    }

    @Override
    public String toString() {
        return "\nusername: " + username + "\ntype of user: "  + this.getTypeOfUserString();
    }
}
