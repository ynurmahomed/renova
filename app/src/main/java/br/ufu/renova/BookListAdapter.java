package br.ufu.renova;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
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

    private int mShortAnimationDuration;

    public BookListAdapter(Context context, int resource, List<Book> objects) {
        super(context, resource, objects);
        mShortAnimationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
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
            TextView callNumberTextView = (TextView) convertView.findViewById(R.id.book_call_number_text_view);
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

            if (callNumberTextView != null) {
                callNumberTextView.setText(b.getCallNumber());
            }

            if (renewDateTextView != null) {
                DateFormat dateFormat = new SimpleDateFormat("dd 'de' MMMM", new Locale("pt", "BR"));
                renewDateTextView.setText(dateFormat.format(b.getExpiration()));
            }

            if (errorIcon != null && b.getState().isErrorState) {
                
                zoomIn(errorIcon);
                errorIcon.setVisibility(View.VISIBLE);

                errorIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    Toast.makeText(getContext(), b.getState().msg, Toast.LENGTH_SHORT).show();
                    }
                });

            }

            if (errorIcon != null && !b.getState().isErrorState) {
                errorIcon.setVisibility(View.INVISIBLE);
            }
        }

        return convertView;
    }

    private void zoomIn(View v) {
        ScaleAnimation scale = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        AlphaAnimation alpha = new AlphaAnimation(0.0f, 1.0f);

        AnimationSet anim = new AnimationSet(true);

        anim.addAnimation(alpha);
        anim.addAnimation(scale);
        anim.setDuration(mShortAnimationDuration);

        v.startAnimation(anim);
    }
}
