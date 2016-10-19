package br.ufu.renova;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import br.ufu.renova.scraper.Book;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by yassin on 01/11/14.
 */
public class BookListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Book[] mDataset;
    private ItemClickListener mItemClickListener;
    private DateFormat mDateFormat = new SimpleDateFormat("dd 'de' MMMM", new Locale("pt", "BR"));
    private Context mContext;
    private int mShortAnimationDuration;

    private ScaleAnimation mScaleIn = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private ScaleAnimation mScaleOut = new ScaleAnimation(1.0f, 0.0f, 1.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    private AlphaAnimation mFadeIn = new AlphaAnimation(0.0f, 1.0f);

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView authorsTextView;
        public TextView renewDateTextView;
        public TextView callNumberTextView;
        public ImageView errorIconImageView;
        public LinearLayout loader;

        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.book_title_text_view);
            authorsTextView = (TextView) itemView.findViewById(R.id.book_authors_text_view);
            renewDateTextView = (TextView) itemView.findViewById(R.id.book_renew_date_text_view);
            callNumberTextView = (TextView) itemView.findViewById(R.id.book_call_number_text_view);
            errorIconImageView = (ImageView) itemView.findViewById(R.id.book_renew_warning);
            loader = (LinearLayout) itemView.findViewById(R.id.book_renew_activity_circle);
        }
    }

    public BookListAdapter(Context context, Book[] books, ItemClickListener onClickListener) {
        mContext = context;
        mDataset = books;
        mItemClickListener = onClickListener;
        mShortAnimationDuration = mContext.getResources().getInteger(android.R.integer.config_shortAnimTime);
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_list_item, parent, false);

        final ViewHolder vh = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                crossfade(vh.renewDateTextView, vh.loader);
                if (vh.errorIconImageView.getVisibility() == View.VISIBLE) {
                    zoomOut(vh.errorIconImageView);
                }
                mItemClickListener.onItemClick(v);
            }
        });

        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            final Book book = mDataset[position];
            ViewHolder vh = (ViewHolder) holder;

            vh.titleTextView.setText(book.getTitle());
            vh.authorsTextView.setText(book.getAuthors());
            vh.callNumberTextView.setText(book.getCallNumber());
            vh.renewDateTextView.setText(mDateFormat.format(book.getExpiration()));

            if (vh.loader.getVisibility() == View.VISIBLE) {
                crossfade(vh.loader, vh.renewDateTextView);
            }

            if (book.getState().isErrorState) {
                zoomIn(vh.errorIconImageView);
            }

            vh.errorIconImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, book.getState().msg, Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public interface ItemClickListener {
        void onItemClick(View view);
    }

    private void crossfade(final View x, View y) {

        y.setAlpha(0);
        y.setVisibility(View.VISIBLE);

        y.animate()
                .alpha(1)
                .setDuration(mShortAnimationDuration)
                .setListener(null);

        x.animate()
                .alpha(0)
                .setDuration(mShortAnimationDuration)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        x.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    private void zoomIn(View v) {
        AnimationSet anim = new AnimationSet(true);
        anim.addAnimation(mFadeIn);
        anim.addAnimation(mScaleIn);
        anim.setDuration(mShortAnimationDuration);
        v.startAnimation(anim);
        v.setVisibility(View.VISIBLE);
    }

    private void zoomOut(View v) {
        AnimationSet anim = new AnimationSet(true);
        anim.addAnimation(mScaleOut);
        anim.setDuration(mShortAnimationDuration);
        v.startAnimation(anim);
        v.setVisibility(View.INVISIBLE);
    }
}
