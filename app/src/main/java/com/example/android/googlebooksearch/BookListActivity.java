package com.example.android.googlebooksearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Constant value for the book loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int BOOK_LOADER_ID = 1;

    /**
     * Adapter for the list of books
     */
    private BookAdapter mAdapter;

    private TextView mEmptyStateTextView;

    private ProgressBar mProgressBarView;

    private String mBook_Request_Url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            mBook_Request_Url = bundle.getString("url");
            //The key argument here must match that used in the other activity
        }


        // Find a reference to the {@link ListView} in the layout
        ListView mbookListView = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty);

        mProgressBarView = (ProgressBar) findViewById(R.id.progress);

        mbookListView.setEmptyView(mEmptyStateTextView);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (isConnected) {
            // Create a new adapter that takes an empty list of books as input
            mAdapter = new BookAdapter(this, new ArrayList<Book>());

            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
            mbookListView.setAdapter(mAdapter);

            // Set an item click listener on the ListView, which sends an intent to a web browser
            // to open a website with more information about the selected book.
            mbookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    // Find the current book that was clicked on
                    Book currentBook = mAdapter.getItem(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri bookUri = Uri.parse(currentBook.getUrl());

                    // Create a new intent to view the book URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                    // Send the intent to launch a new activity
                    startActivity(websiteIntent);
                }

            });

            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            Log.i(LOG_TAG,"TEST:  Loader initialized");
            loaderManager.initLoader(BOOK_LOADER_ID, null, this);

        } else {
            // Set progress bar view visability to gone
            mProgressBarView.setVisibility(View.GONE);

            // Set empty state text to display "No internet connection."
            mEmptyStateTextView.setText(R.string.no_internet);

        }

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() executed");
        // Create a new loader for the given URL
        return new BookLoader(this, mBook_Request_Url);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        // Clear the adapter of previous book data
        mAdapter.clear();

        // Set progress bar view visability to gone
        mProgressBarView.setVisibility(View.GONE);

        // Set empty state text to display "No books found."
        mEmptyStateTextView.setText(R.string.no_books);

        Log.i(LOG_TAG, "TEST: onLoadFinished() executed");

        // If there is a valid list of {@link Book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.i(LOG_TAG, "TEST: onLoaderReset() executed");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }



}


