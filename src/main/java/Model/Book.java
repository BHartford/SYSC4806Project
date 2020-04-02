package Model;

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
	public static final double DEFAULT_RATING = 0;
	public static final int DEFAULT_NUMBER_OF_RATINGS = 0;

	@Id @GeneratedValue
	long id;
	private String title;
	private String author;
	private int year;
	private String description;
	private double price;
	private int quantity;
	private double rating;
	private int numberOfRatings;

	public Book() {}

	public Book(String title, String author, double price) {
		this(title, author, null, null, price, null, null, 0);
	}
	
	public Book(String title, String author, Integer year, String description, Double price, Integer quantity, Double rating, int numberOfRatings) {
		this.title = title;
		this.author = author;
		this.year = Optional.ofNullable(year).orElse(DEFAULT_YEAR);
		this.description = Optional.ofNullable(description).orElse(DEFAULT_DESCRIPTION);
		this.price = price;
		this.quantity = Optional.ofNullable(quantity).orElse(DEFAULT_QUANTITY);
		this.rating = Optional.ofNullable(rating).orElse(DEFAULT_RATING);
		this.numberOfRatings = Optional.ofNullable(numberOfRatings).orElse(DEFAULT_NUMBER_OF_RATINGS);
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
	
	public double getRating() {
		return rating;
	}
	
	public void setRating(double rating) {
		this.rating = rating;
	}
	
	public int getNumberOfRatings() {
		return numberOfRatings;
	}
	
	public void setNumberOfRatings(int numberOfRatings) {
		this.numberOfRatings = numberOfRatings;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id=" + id +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", price=" + price +
				", quantity=" + quantity +
				", year=" + year +
				", description='" + description + '\'' +
				", rating='" + rating + '\'' +
				", description='" + numberOfRatings + '\'' +
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
