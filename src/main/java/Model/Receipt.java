package Model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "receipt")
public class Receipt {

    @Id
    @GeneratedValue
    long id;

    @Temporal(TemporalType.DATE)
    @CreationTimestamp
    private Date date;

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

    public Date getDate() {
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

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + user +
                ", items=" + items +
                '}';
    }
}
