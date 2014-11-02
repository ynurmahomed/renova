package com.renovapp.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.renovapp.app.scraper.Book;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by yassin on 01/11/14.
 */
public class BookListAdapter extends ArrayAdapter<Book> {

    public BookListAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.book_list_item, null);
        }

        Book b = getItem(position);

        if (b != null) {
            TextView titleTextView = (TextView) convertView.findViewById(R.id.book_title_text_view);
            TextView authorsTextView = (TextView) convertView.findViewById(R.id.book_authors_text_view);
            TextView renewDateTextView = (TextView) convertView.findViewById(R.id.book_renew_date_text_view);

            if (titleTextView != null) {
                titleTextView.setText(b.getTitle());
            }

            if (authorsTextView != null) {
                authorsTextView.setText(b.getAuthors());
            }

            if (renewDateTextView != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM");
                renewDateTextView.setText(dateFormat.format(b.getExpiration()));
            }
        }

        return convertView;
    }
}
