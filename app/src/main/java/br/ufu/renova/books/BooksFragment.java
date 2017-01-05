package br.ufu.renova.books;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import br.ufu.renova.R;
import br.ufu.renova.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment implements BooksContract.View {

    public static final CharSequence TITLE = "Livros";

    private BooksAdapter mAdapter;

    private BooksContract.Presenter mPresenter;

    private RecyclerView mRecyclerView;

    private LinearLayout mEmptyBooksView;

    private ProgressBar mProgressBar;

    public BooksFragment() {
    }

    public static BooksFragment newInstance() {
        return new BooksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int shortAnimationDuration = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        ArrayList<Book> books = new ArrayList<>(0);

        mAdapter = new BooksAdapter(books, mBookItemListener, shortAnimationDuration);
        setRetainInstance(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.books_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mEmptyBooksView = (LinearLayout) rootView.findViewById(R.id.empty_books_list_message);
        mEmptyBooksView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onReloadClick();
            }
        });

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.books_progress_bar);

        return rootView;
    }

    @Override
    public void setPresenter(BooksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showBooksList(List<Book> books) {
        mProgressBar.setVisibility(View.GONE);
        mEmptyBooksView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.replaceData(books);
    }

    @Override
    public void showEmptyView() {
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mEmptyBooksView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {
        mEmptyBooksView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSessionExpiredToast() {
        Toast.makeText(getContext(), R.string.message_session_expired, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showScrapeErrorToast() {
        Toast.makeText(getContext(), R.string.message_unprocessable_response, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNoConnectionToast() {
        Toast.makeText(getContext(), R.string.message_no_connection, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showBookErrorToast(Book b) {
        Toast.makeText(getContext(), b.getState().MSG, Toast.LENGTH_SHORT).show();
    }

    private BookItemListener mBookItemListener = new BookItemListener() {
        @Override
        public void onBookClick(Book b) {
            mPresenter.onBookClick(b);
        }

        @Override
        public void onBookErrorIconClick(Book b) {
            mPresenter.onBookErrorIconClick(b);
        }
    };

    public interface BookItemListener {

        void onBookClick(Book b);

        void onBookErrorIconClick(Book b);
    }
}
