package br.ufu.renova;

import android.animation.Animator;
import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import br.ufu.renova.scraper.Book;
import br.ufu.renova.scraper.BookReservedException;
import br.ufu.renova.scraper.HttpClient;
import br.ufu.renova.scraper.RenewDateException;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BookListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BookListFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class BookListFragment extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LIBRARY = "library";
    public static final CharSequence TITLE = "Livros";

    // TODO: Rename and change types of parameters
    private HttpClient library;
    private ListView booksListView;
    private BookListAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    private int mShortAnimationDuration;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param library Parameter 1.
     * @return A new instance of fragment BookListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BookListFragment newInstance(HttpClient library) {
        BookListFragment fragment = new BookListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIBRARY, library);
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
            library = (HttpClient) getArguments().getSerializable(ARG_LIBRARY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_book_list, container, false);
        View adBanner = inflater.inflate(R.layout.banner, null);

        booksListView = (ListView) rootView.findViewById(R.id.books_list_view);
        booksListView.setEmptyView(rootView.findViewById(R.id.empty_books_list_message));

        mAdapter = new BookListAdapter(getActivity().getApplicationContext(), R.layout.book_list_item, library.getBooks());

        booksListView.addFooterView(adBanner);
        booksListView.setAdapter(mAdapter);

        booksListView.setOnItemClickListener(this);

        mShortAnimationDuration = getResources().getInteger(
                android.R.integer.config_shortAnimTime);

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SettingsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
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
            final LinearLayout progress = (LinearLayout) item.view.findViewById(R.id.book_renew_activity_circle);
            TextView renew_date = (TextView) item.view.findViewById(R.id.book_renew_date_text_view);
            mAdapter.notifyDataSetChanged();
            crossfade(progress, renew_date);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Book b = library.getBooks().get(position);

        RenewTaskItem item = new RenewTaskItem();
        item.book = b;
        item.view = view;

        LinearLayout progress = (LinearLayout) item.view.findViewById(R.id.book_renew_activity_circle);
        final TextView renew_date = (TextView) item.view.findViewById(R.id.book_renew_date_text_view);
        ImageView errorIcon = (ImageView) item.view.findViewById(R.id.book_renew_warning);

        errorIcon.setVisibility(View.INVISIBLE);
        crossfade(renew_date, progress);
        new RenewTask().execute(item);
    }

    public void crossfade(final View x, View y) {

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

}
