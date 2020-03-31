package Model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {

    public static final int SELLER = 0;
    public static final int BUYER = 1;

    @Id
    @GeneratedValue
    long id;
    @Column(unique = true)
    private String username;
    private String password;
    private int typeOfUser;

    public User() {
    }

    public User(String username, String password, int typeOfUser) {
        this.username = username;
        this.password = password;
        this.typeOfUser = typeOfUser;
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

    public boolean validPassword(String attemptedPassword) {
        return attemptedPassword.equals(password);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", typeOfUser=" + this.getTypeOfUserString() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return typeOfUser == user.typeOfUser &&
                username.equals(user.username) &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, typeOfUser);
    }
}
