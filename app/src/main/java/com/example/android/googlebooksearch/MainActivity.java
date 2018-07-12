package com.example.android.googlebooksearch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = MainActivity.class.getName();

    private static final String GOOGLE_BOOKS_URL_START = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final String GOOGLE_BOOKS_URL_END = "&prettyPrint=false";

    private String mBook_Request_Url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        Button buttonID = (Button) findViewById(R.id.button);
        // Set a click listener on that Button
        buttonID.setOnClickListener(new View.OnClickListener() {
            // The code in this method will be executed when the numbers category is clicked on.
            @Override
            public void onClick(View view) {

                StringBuilder sb = new StringBuilder();

                EditText search_input_field = (EditText) findViewById(R.id.book_search_input);
                String searchParam = search_input_field.getText().toString();

                EditText title_input_field = (EditText) findViewById(R.id.title_search_input);
                String titleParam = title_input_field.getText().toString();

                EditText author_input_field = (EditText) findViewById(R.id.author_search_input);
                String authorParam = author_input_field.getText().toString();


                if (searchParam.isEmpty() && titleParam.isEmpty() && authorParam.isEmpty()) {
                    mBook_Request_Url = null;
                } else {

                    sb.append(GOOGLE_BOOKS_URL_START);

                    if (!authorParam.isEmpty()) {
                        String authorSearch = "inauthor:" + authorParam.replace(" ","+");
                        sb.append(authorSearch);
                    }
                    if (!titleParam.isEmpty()) {
                        String titleSearch = "+intitle:" + titleParam.replace(" ","+");
                        sb.append(titleSearch);
                    }
                    if (!searchParam.isEmpty()) {
                    String searchFor = "+" + searchParam.replace(" ","+");
                    sb.append(searchFor);
                    }



                    sb.append(GOOGLE_BOOKS_URL_END);

                    mBook_Request_Url = sb.toString();
                }
                if (mBook_Request_Url != null) {
                    // Create a new intent to open the {@link NumbersActivity}
                    Intent bookIntent = new Intent(MainActivity.this, BookListActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", mBook_Request_Url);

                    bookIntent.putExtras(bundle);
                    // Start the new activity
                    startActivity(bookIntent);
                }
            }
        });

    }

}


