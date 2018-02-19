package edu.mybooklistingappv2;

/**
 * Created by edu on 7/12/2017.
 */

public class BookClass {

    private String mTitle;

    private String mAuthor;

    private String mDate;

    public BookClass(String title, String author, String date) {
        mTitle = title;
        mAuthor = author;
        mDate = date;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

}

