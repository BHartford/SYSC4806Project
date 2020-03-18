package dataModel;

public enum ApplicationMsg {
    //ERROR
    BAD_BOOK_ID("ID %d is not recognized as a book on file.", 1),
    QUERY_NOT_FOUND("Query %s did not match any results on file.", 2),
    GENERIC_ERROR("Well that's embarrassing! We're investigating what went wrong.", 0),
    BAD_REQUEST("There was a error processing your request. Please check your request and try again.", 400),
    USER_NOT_AUTH("You don't appear to have the proper authentication to do that.", 401),
    REQUEST_REFUSED("We are unable to complete your request at this time. Please try again later.", 403),
    INVALID_PAGE("The requested page %s does not exist.", 404);

    private final String msg;
    private final Integer msgNumber;

    ApplicationMsg(String msg, Integer msgNumber) {
        this.msg = msg;
        this.msgNumber = msgNumber;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getMsgNumber() {
        return msgNumber;
    }
}
