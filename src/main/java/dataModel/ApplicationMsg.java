package dataModel;

public enum ApplicationMsg {
    //ERROR
    BAD_BOOK_ID("ID %d is not recognized as a book on file.", 1),
    SEARCH_NOT_FOUND("Query '%s' did not match any results on file.", 2),
    INVALID_PAGE("The requested page could not be found.", 3);

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
