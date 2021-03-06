package com.example.android.googlebooksearch;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

/**
 * Helper methods related to requesting and receiving book data from Google Books.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Book> extractBooks(Context context, String webUrl) {

        Log.i(LOG_TAG, "TEST: extractBooks() has been called");

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> books = new ArrayList<>();

        // Try to parse the JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the JSON_RESPONSE string and
            // build up a list of Book objects with the corresponding data.
            JSONObject baseJsonObject = new JSONObject(fetchBookData(webUrl));
            JSONArray bookArray = baseJsonObject.getJSONArray("items");
            int numberOfBooks = bookArray.length();
            if (bookArray != null && numberOfBooks > 0) {
                for (int i = 0; i < numberOfBooks; i++) {
                    JSONObject bookObject = bookArray.getJSONObject(i);
                    JSONObject volumeInfo = bookObject.getJSONObject("volumeInfo");
                    if (volumeInfo != null && volumeInfo.length() > 0) {

                        // Get the book title from the JSONobject volumeInfo
                        String title = volumeInfo.getString("title");

                        // Set a default drawable for the book cover in case there isn't a link in the JSON response
                        Drawable thumbnail = context.getResources().getDrawable(R.drawable.small_sample_book_cover);

                        // Try to get the book cover URL from "imageLinks" in the volumeInfo and
                        // Catch the error if the URL isn't there or is bad and Log the problem in the error message logs.
                         try {
                             JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                             String thumbnailUrl = imageLinks.getString("smallThumbnail");
                             thumbnail = LoadImageFromWebOperations(thumbnailUrl);
                         } catch (Exception e){
                             Log.e("QueryUtils", "Problem getting thumbnail in the book JSON results", e);
                         }

                        // Set a default (blank) description for the book in case there isn't one in the JSON response
                        String description = "";

                        // Try to get the book description from the volumeInfo and
                        // Catch the error if the description isn't there and Log the problem in the error message logs.
                        try{
                            description = volumeInfo.getString("description");
                        } catch (Exception e){
                            Log.e("QueryUtils", "No Description in the book JSON results", e);
                        }

                        // Set a default (blank) for the authors in case there isn't one in the JSON response
                        String authors = "";
                        // Try to get the author(s) from the volumeInfo and
                        // Catch the error if the author(s) are't there and Log the problem in the error message logs.
                        try{
                            JSONArray authorsArray = volumeInfo.getJSONArray("authors");
                            StringBuilder authorsBuilder = new StringBuilder();
                            if (authorsArray != null && authorsArray.length() > 0) {
                                for (int j = 0; j < authorsArray.length(); j++) {
                                    if (j == 0) {
                                        authorsBuilder.append(authorsArray.getString(j));
                                    } else {
                                        String nextAuthor = ", " + authorsArray.getString(j);
                                        authorsBuilder.append(nextAuthor);
                                    }
                                }
                            }
                            authors = authorsBuilder.toString();
                        } catch (Exception e){
                            Log.e("QueryUtils", "No Authors found in the book JSON results", e);
                        }

                        // Get the URL of the book in the Google Book store
                        String bookUrl = volumeInfo.getString("canonicalVolumeLink");

                        float rating = -1.0f;
                        // Try to get the book description from the volumeInfo and
                        // Catch the error if the description isn't there and Log the problem in the error message logs.
                        try{
                            rating = (float) volumeInfo.getDouble("averageRating");
                        } catch (Exception e){
                            Log.e("QueryUtils", "No averageRating in the book JSON results", e);
                        }

                        books.add(new Book(title, thumbnail, description, authors, bookUrl, rating));
                    }
                }
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }

        // Return the list of books
        return books;
    }

    /**
     * Query the Google Books dataset and get a JSON file response
     */
    private static String fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return jsonResponse;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
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

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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


    /**
     * get an image from the submitted URL (@param url)
     * @return a Drawable
     */
    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }


}
