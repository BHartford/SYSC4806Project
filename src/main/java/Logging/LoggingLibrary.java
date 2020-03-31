package Logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Model.Book;
import Model.Receipt;
import Model.User;

public class LoggingLibrary {
    public static String getTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        String time = "[" + dtf.format(now) + "]: ";
        return time;
    }
    
    public static String newBookLog(Book book, String user) {
    	return getTime() + "[" + user + "] added a new book: " + book.toString();
    }
    
    public static String purchaseLog(Receipt receipt, String user) {
    	return getTime() + "[" + user + "] purchased "+ receipt.getItemCount() + " items:\n" + receipt.toString();
    }
    
    public static String errorLog(int statusCode, String message) {
    	return getTime() + "Error with code " + statusCode + " occured with the following message:\n\t\"" + message;
    }
    
    public static String newUserLog(User user) {
    	return getTime() + "New User: " + user.toString();
    }
}
