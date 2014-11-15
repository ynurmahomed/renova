package com.renovapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


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
    private static final String ARG_NUM_DAYS = "num_days";

    public static final CharSequence TITLE = "Configurações";

    private int numDays;

    private String[] daysBeforeOptions = new String[]{"1","2","3","4","5","6","7"};

    private OnFragmentInteractionListener mListener;

    private TextView notificationPrefText;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param numDays quantos dias antes deve se enviar notificações
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(int numDays) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NUM_DAYS, numDays);
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
            numDays = getArguments().getInt(ARG_NUM_DAYS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        LinearLayout notificationPref = (LinearLayout) rootView.findViewById(R.id.notification_preference);

        notificationPrefText = (TextView) rootView.findViewById(R.id.notification_preference_value);
        notificationPrefText.setText(""+numDays);
        notificationPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotificationPreferenceClick();
            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onNotificationPreferenceClick() {
        final Context appContext = SettingsFragment.this.getActivity();
        final int[] numDays = new int[1];

        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle("Notificar");
        builder.setItems(daysBeforeOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                numDays[0] = which + 1;
                notificationPrefText.setText(""+ numDays[0]);
                if (mListener != null) {
                    mListener.onNotificationDateSelect(numDays[0]);
                }
            }
        });
        builder.show();
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
        public void onNotificationDateSelect(int numDays);
    }

}
