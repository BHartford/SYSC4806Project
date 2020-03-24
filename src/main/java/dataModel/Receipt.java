package dataModel;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "receipt")
public class Receipt {

    private static DecimalFormat df = new DecimalFormat("0.00");

    @Id
    @GeneratedValue
    long id;

    @CreationTimestamp
    private Timestamp date;

    @OneToOne
    private User user;

    @ManyToMany
    private List<Book> items;

    public Receipt() {
    }

    public Receipt(User user) {
        this(user, new ArrayList<Book>());
    }

    public Receipt(User user, List<Book> items) {
        this.user = user;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public Timestamp getDate() {
        return date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Book> getItems() {
        return items;
    }

    public void addItems(Book... books) {
        for (Book b : books) {
            items.add(b);
        }
    }

    public int getItemCount() {
        return items.size();
    }

    public double getTotal() {
        double price = 0;

        for (Book b : items) {
            price += b.getPrice();
        }

        return price;
    }
}
