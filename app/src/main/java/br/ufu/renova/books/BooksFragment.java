package br.ufu.renova.books;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import br.ufu.renova.R;
import br.ufu.renova.scraper.Book;

import java.util.ArrayList;
import java.util.List;

public class BooksFragment extends Fragment implements BooksContract.View {

    public static final CharSequence TITLE = "Livros";

    private BooksAdapter mAdapter;

    private BooksContract.Presenter mPresenter;


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

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.books_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void setPresenter(BooksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showBooksList(List<Book> books) {
        mAdapter.replaceData(books);
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
