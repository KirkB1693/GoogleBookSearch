package com.example.android.googlebooksearch;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter<Book> {
    private static final String LOG_TAG = BookAdapter.class.getSimpleName();

    /**
     * This is a custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param books   A List of Book objects to display in a list
     */
    public BookAdapter(Activity context, ArrayList<Book> books) {
        super(context, 0, books);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.book_list, parent, false);
        }

        // Get the {@link Book} object located at this position in the list
        Book currentBook = getItem(position);

        // Find the TextView in the quake_list.xml layout with the ID version_name
        TextView book_Title = (TextView) listItemView.findViewById(R.id.book_title);

        // Get the book title from the current Book object and
        // set this text on the quake_magnitude TextView
        String title = currentBook.getTitle();
        book_Title.setText(title);

        // Find the ImageView in the book_list.xml layout with the ID version_number
        ImageView book_Thumbnail = (ImageView) listItemView.findViewById(R.id.book_thumbnail);
        // Get the thumbnail from the current Book object and set this drawable on the book_thumbnail ImageView
        book_Thumbnail.setImageDrawable(currentBook.getThumbnail());

        float rating = currentBook.getRating();
        RatingBar book_Rating = (RatingBar) listItemView.findViewById(R.id.rating_bar);
        TextView no_Book_Rating = (TextView) listItemView.findViewById(R.id.no_rating_bar);
        if (rating < 0) {
            book_Rating.setVisibility(View.INVISIBLE);
            no_Book_Rating.setVisibility(View.VISIBLE);
        } else {
            book_Rating.setRating(currentBook.getRating());
        }



        // Find the TextView in the book_list.xml layout with the ID version_number
        TextView book_Authors = (TextView) listItemView.findViewById(R.id.book_author);
        // Get the author(s) from the current Book object and set this text on the book_Authors TextView
        book_Authors.setText(currentBook.getAuthor());

        // Find the TextView in the book_list.xml layout with the ID version_number
        TextView book_Description = (TextView) listItemView.findViewById(R.id.book_description);
        // Get the book description from the current Book object and then set this text on the book_Description TextView
        book_Description.setText(currentBook.getDescription());

        /*
        // Return the whole list item layout (containing 3 TextViews and an ImageView)
        // so that it can be shown in the ListView
        */
        return listItemView;
    }
}
