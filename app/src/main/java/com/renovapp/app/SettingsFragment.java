package com.renovapp.app;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.renovapp.app.scraper.HttpClient;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SettingsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LIBRARY = "library";
    public static final CharSequence TITLE = "Configurações";

    // TODO: Rename and change types of parameters
    private HttpClient library;
    private Spinner daysBeforeSpinner;
    private Integer[] daysBeforeOptions = new Integer[]{1,2,3,4,5,6,7,};

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param library Parameter 1.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(HttpClient library) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_LIBRARY, library);
        fragment.setArguments(args);
        return fragment;
    }
    public SettingsFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

//        daysBeforeSpinner = (Spinner) rootView.findViewById(R.id.days_before_spinner);
//
//        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(getActivity().getApplicationContext(),
//                android.R.layout.simple_spinner_item, daysBeforeOptions);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        daysBeforeSpinner.setAdapter(adapter);

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
                    + " must implement OnFragmentInteractionListener");
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

}