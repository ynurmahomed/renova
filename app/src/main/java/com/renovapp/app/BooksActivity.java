package com.renovapp.app;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.renovapp.app.scraper.*;

import java.io.IOException;


public class BooksActivity extends ActionBarActivity {

    HttpClient library;
    ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        library = (HttpClient) intent.getSerializableExtra(LoginActivity.EXTRA_LIBRARY_CLIENT);

        booksListView = (ListView) findViewById(R.id.books_list_view);

        booksListView.setAdapter(new BookListAdapter(this, R.layout.book_list_item, library.getBooks()));

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new RenewTask().execute(BooksActivity.this.library.getBooks().get(position));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class RenewTask extends AsyncTask<Book, Void, Void> {

        @Override
        protected Void doInBackground(Book... params) {
            Book b = params[0];

            try {
                library.renew(b);
            } catch (IOException e) {
                Log.d("RenewTask", e.toString());
                e.printStackTrace();
            } catch (BookReservedException e) {
                e.printStackTrace();
            } catch (RenewDateException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
