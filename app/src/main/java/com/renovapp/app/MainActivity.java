package com.renovapp.app;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.renovapp.app.scraper.Book;
import com.renovapp.app.scraper.HttpClient;
import com.renovapp.app.scraper.LoginException;

import java.io.IOException;


public class MainActivity extends ActionBarActivity {

    HttpClient biblioteca;
    ListView booksListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        booksListView = (ListView) findViewById(R.id.books_list_view);

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

    private class LoginTask extends AsyncTask<Void, Integer, HttpClient> {

        @Override
        protected HttpClient doInBackground(Void... params) {
            try {
                if (biblioteca == null) {
                    return new HttpClient("77009083697606", "7606");
                }
            } catch (IOException e) {
                Log.d("MainActivity", e.toString());
                e.printStackTrace();
            } catch (LoginException e) {
                Log.d("MainActivity", "Login Error");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(HttpClient biblioteca) {
            ArrayAdapter<Book> adapter;

            adapter = new ArrayAdapter<Book>(MainActivity.this, android.R.layout.simple_list_item_1, biblioteca.getBooks());

            booksListView.setAdapter(adapter);
        }
    }
}
