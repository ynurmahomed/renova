package br.ufu.renova;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import br.ufu.renova.scraper.Book;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link br.ufu.renova.BookListFragment.BookClickListener} interface
 * to handle interaction events.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BookListFragment extends Fragment implements BookListAdapter.ItemClickListener {

    private static final String ARG_BOOKS = "books";
    public static final CharSequence TITLE = "Livros";

    private Book[] mBooks;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private BookClickListener mBookClickListener;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param books Parameter 1.
     * @return A new instance of fragment BookListFragment.
     */
    public static BookListFragment newInstance(Object[] books) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_BOOKS, Arrays.copyOf(books, books.length, Book[].class));
        fragment.setArguments(args);
        return fragment;
    }
    public BookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBooks = (Book[]) getArguments().getSerializable(ARG_BOOKS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        //View adBanner = inflater.inflate(R.layout.banner, null);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.books_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new BookListAdapter(getActivity().getApplicationContext(), mBooks, this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mBookClickListener = (BookClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SettingsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mBookClickListener = null;
    }

    @Override
    public void onItemClick(View view) {
        int position = mRecyclerView.getChildPosition(view);
        if (mBookClickListener != null) {
            mBookClickListener.onBookClick(position);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface BookClickListener {
        public void onBookClick(int position);
    }

    public void notifyItemChanged(int position) {
        mAdapter.notifyItemChanged(position);
    }
}
