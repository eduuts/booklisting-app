package edu.mybooklistingappv2;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edu on 7/12/2017.
 */

public class API {

    public static final String LOG_TAG = API.class.getName();

    private API() {
    }

    public static List<BookClass> getBookData(String requestUrl) {

        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot make the specified request.", e);
        }

        List<BookClass> books = extractFeatureFromJson(jsonResponse);
        return books;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Cannot build the specified URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";


        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Cannot retreieve the Book JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {

                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<BookClass> extractFeatureFromJson(String bookJSON) {

        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }

        List<BookClass> books = new ArrayList<>();

        try {

            JSONObject baseJsonResponse = new JSONObject(bookJSON);


            if (baseJsonResponse.has("items")) {
                JSONArray booksArray = baseJsonResponse.getJSONArray("items");
                for (int i = 0; i < booksArray.length(); i++) {
                    String date;
                    String title;
                    JSONObject currentBook = booksArray.getJSONObject(i);

                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");
                    if (volumeInfo.has("title")) {
                        title = volumeInfo.getString("title");
                    } else title = "Title N/A";


                    String author;
                    if (volumeInfo.has("authors")) {
                        JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                        author = authorsArray.getString(0);
                    } else author = "Author N/A";
                    if (volumeInfo.has("PublishedDate")) {
                        JSONArray dateArray = volumeInfo.getJSONArray("date");
                        date = volumeInfo.getString("publishedDate");
                    } else date = "Published date N/A";


                    BookClass book = new BookClass(title, author, date);

                    books.add(book);
                }
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return books;
    }
}



