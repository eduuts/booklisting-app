package edu.mybooklistingappv2;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edu on 7/12/2017.
 */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<BookClass>> {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?maxResults=30&orderBy=newest&q=";

    private static final int BOOK_LOADER_ID = 1;

    private BookClassAdapter mBookClassAdapter;

    private SearchView searchView;

    private ListView booksListView;

    private String API;

    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        searchView = (SearchView) findViewById(R.id.search_field);

        booksListView = (ListView) findViewById(R.id.listview);

        mBookClassAdapter = new BookClassAdapter(this, new ArrayList<BookClass>());

        booksListView.setAdapter(mBookClassAdapter);


        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);


        if (isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);
        } else {

            mEmptyStateTextView.setText("No internet");
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (isConnected()) {
                    booksListView.setVisibility(View.VISIBLE);
                    API = searchView.getQuery().toString();
                    API = API.replace(" ", "+");
                    Log.v(LOG_TAG, API);
                    getLoaderManager().restartLoader(BOOK_LOADER_ID, null, MainActivity.this);
                    searchView.clearFocus();
                } else {
                    booksListView.setVisibility(View.INVISIBLE);
                    mEmptyStateTextView.setVisibility(View.VISIBLE);
                    mEmptyStateTextView.setText("No internet");
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    @Override
    public Loader<List<BookClass>> onCreateLoader(int i, Bundle bundle) {
        String requestUrl = "";
        if (API != null && API != "") {
            requestUrl = BOOK_REQUEST_URL + API;
        } else {
            String defaultQuery = "Gabriel García Márquez";
            requestUrl = BOOK_REQUEST_URL + defaultQuery;
        }
        return new BookClassLoader(this, requestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<BookClass>> loader, List<BookClass> books) {
        mEmptyStateTextView.setText("No books found");

        mBookClassAdapter.clear();

        if (books != null && !books.isEmpty()) {
            mBookClassAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<BookClass>> loader) {
        mBookClassAdapter.clear();
    }

}