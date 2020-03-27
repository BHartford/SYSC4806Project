package dataModel;
import java.util.Optional;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Review {
	public static final String DEFAULT_REVIEW = "No Review Provided";
	
	@Id @GeneratedValue
	long id;
	private User user;
	private Book book;
	private double rating;
	private String review;
	
	public Review(User user, Book book, double rating) {
		this(user, book, rating, null);
	}
	
	public Review(User user, Book book, double rating, String review) {
		this.user = user;
		this.book = book;
		this.rating = rating;
		this.review = Optional.ofNullable(review).orElse(DEFAULT_REVIEW);
	}

	public long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}
	
	

}
