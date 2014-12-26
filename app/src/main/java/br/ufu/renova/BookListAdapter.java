package br.ufu.renova;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.ufu.renova.R;
import br.ufu.renova.scraper.Book;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by yassin on 01/11/14.
 */
public class BookListAdapter extends ArrayAdapter<Book> {

    public BookListAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Boolean[] titleTextViewExpanded = {false};
        final Boolean[] authorsTextViewExpanded = {false};

        if (convertView == null) {
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.book_list_item, null);
        }

        final Book b = getItem(position);

        if (b != null) {
            final TextView titleTextView = (TextView) convertView.findViewById(R.id.book_title_text_view);
            final TextView authorsTextView = (TextView) convertView.findViewById(R.id.book_authors_text_view);
            TextView renewDateTextView = (TextView) convertView.findViewById(R.id.book_renew_date_text_view);
            ImageView errorIcon = (ImageView) convertView.findViewById(R.id.book_renew_warning);

            if (titleTextView != null) {
                titleTextView.setText(b.getTitle());
                titleTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        titleTextView.setSingleLine(!titleTextViewExpanded[0]);
                        titleTextViewExpanded[0] = !titleTextViewExpanded[0];
                    }
                });
            }

            if (authorsTextView != null) {
                authorsTextView.setText(b.getAuthors());
                authorsTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        authorsTextView.setSingleLine(!authorsTextViewExpanded[0]);
                        authorsTextViewExpanded[0] = !authorsTextViewExpanded[0];
                    }
                });
            }

            if (renewDateTextView != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM", new Locale("pt", "BR"));
                renewDateTextView.setText(dateFormat.format(b.getExpiration()));
            }

            if (errorIcon != null && b.getState().isErrorState) {
                errorIcon.setVisibility(View.VISIBLE);

            }
        }

        return convertView;
    }
}
