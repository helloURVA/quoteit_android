package it.qoute.zaingz.com.quoteit_android;

/**
 * Created by Zain on 5/31/2015.
 */
public class QuotePojo {

    private String quote, author, userName, userID;
    private long time;

    public QuotePojo(String quote, String author, String userID,String username, long time) {
        this.quote = quote;
        this.author = author;
        this.userID = userID;
        this.time = time;
        this.userName = username;

    }


    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}


