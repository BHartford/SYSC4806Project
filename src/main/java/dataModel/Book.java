package dataModel;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Book {
	public static final int DEFAULT_YEAR = 0;
	public static final int DEFAULT_QUANTITY = 1;
	public static final String DEFAULT_DESCRIPTION = "No Description Provided";

	@Id @GeneratedValue
	long id;
	private String title;
	private String author;
	private int year;
	private String description;
	private double price;
	private int quantity;

	public Book() {}

	public Book(String title, String author, double price) {
		this(title, author, null, null, price, null);
	}
	
	public Book(String title, String author, Integer year, String description, Double price, Integer quantity) {
		this.title = title;
		this.author = author;
		this.year = Optional.ofNullable(year).orElse(DEFAULT_YEAR);
		this.description = Optional.ofNullable(description).orElse(DEFAULT_DESCRIPTION);
		this.price = price;
		this.quantity = Optional.ofNullable(quantity).orElse(DEFAULT_QUANTITY);
	}

	public long getId() { return id; }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", price=" + price +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book  book = (Book) o;
		return year == book.year &&
				Double.compare(book.price, price) == 0 &&
				title.equals(book.title) &&
				author.equals(book.author);
	}

	@Override
	public int hashCode() {
		return Objects.hash(title, author, year, price);
	}
}
