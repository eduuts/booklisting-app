package edu.mybooklistingappv2;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by edu on 7/12/2017.
 */

public class BookClassLoader extends AsyncTaskLoader <List<BookClass>>{

    private static final String LOG_TAG = BookClassLoader.class.getName();

    private String mUrl;

    public BookClassLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BookClass> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<BookClass> books = API.getBookData(mUrl);
        return books;
    }
}