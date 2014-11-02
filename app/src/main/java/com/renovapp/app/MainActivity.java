package com.renovapp.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.renovapp.app.scraper.*;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    HttpClient biblioteca;
    ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksListView = (ListView) findViewById(R.id.books_list_view);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new RenewTask().execute(biblioteca.getBooks().get(position));
            }
        });

        new LoginTask().execute();
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

    private class LoginTask extends AsyncTask<Void, Void, HttpClient> {

        @Override
        protected HttpClient doInBackground(Void... params) {
            try {
                if (biblioteca == null) {
                    return new HttpClient("77009083697606", "7606");
                }
            } catch (IOException e) {
                Log.d("LoginTask", e.toString());
                e.printStackTrace();
            } catch (LoginException e) {
                Log.d("LoginTask", "Login Error");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(HttpClient biblioteca) {
            ArrayAdapter<Book> adapter;

            adapter = new BookListAdapter(MainActivity.this, R.layout.book_list_item, biblioteca.getBooks());

            MainActivity.this.biblioteca = biblioteca;

            booksListView.setAdapter(adapter);
        }
    }

    private class RenewTask extends AsyncTask<Book, Void, Void> {

        @Override
        protected Void doInBackground(Book... params) {
            Book b = params[0];

            try {
                biblioteca.renew(b);
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
