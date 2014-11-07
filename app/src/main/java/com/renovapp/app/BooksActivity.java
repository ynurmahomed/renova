package com.renovapp.app;

import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.*;
import com.renovapp.app.admob.Interstitial;
import com.renovapp.app.scraper.*;

import java.io.IOException;


public class BooksActivity extends ActionBarActivity {

    HttpClient library;
    ListView booksListView;

    private boolean interstitialControl = false;
    private Interstitial interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books);

        Intent intent = getIntent();

        library = (HttpClient) intent.getSerializableExtra(LoginActivity.EXTRA_LIBRARY_CLIENT);

        booksListView = (ListView) findViewById(R.id.books_list_view);

        booksListView.setEmptyView(findViewById(R.id.empty_books_list_message));

        booksListView.setAdapter(new BookListAdapter(this, R.layout.book_list_item, library.getBooks()));

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RenewTaskItem item = new RenewTaskItem();
                item.book = BooksActivity.this.library.getBooks().get(position);
                item.view = view;

                LinearLayout progress = (LinearLayout) item.view.findViewById(R.id.book_renew_activity_circle);
                TextView renew_date = (TextView) item.view.findViewById(R.id.book_renew_date_text_view);

                renew_date.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);

                new RenewTask().execute(item);
            }
        });

        this.interstitial = new Interstitial(BooksActivity.this).getInterstitial();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.books_menu, menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public void onResume(){
        Log.d("TESTE","AQUI");
        if(this.interstitialControl)
            this.interstitial.getInterstitial();
        this.interstitialControl = (this.interstitialControl) ? false : true;
        super.onResume();
    }

    private class RenewTaskItem {
        View view;
        Book book;
    }

    private class RenewTask extends AsyncTask<RenewTaskItem, Void, Void> {

        private RenewTaskItem item;

        @Override
        protected Void doInBackground(RenewTaskItem... params) {
            item = params[0];

            try {
                library.renew(item.book);
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

        @Override
        protected void onPostExecute(Void nothing) {
            LinearLayout progress = (LinearLayout) item.view.findViewById(R.id.book_renew_activity_circle);
            TextView renew_date = (TextView) item.view.findViewById(R.id.book_renew_date_text_view);

            renew_date.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }
    }
}
