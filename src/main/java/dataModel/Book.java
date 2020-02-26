package dataModel;

import javax.persistence.*;

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
	
	public Book(String title, String author, double d) {
		this(title, author, DEFAULT_YEAR, DEFAULT_DESCRIPTION, d, DEFAULT_QUANTITY);
	}
	
	public Book(String title, String author, int year, String description, double price, int quantity) {
		this.title = title;
		this.author = author;
		this.year = year;
		this.description = description;
		this.price = price;
		this.quantity = quantity;
	}

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
}
