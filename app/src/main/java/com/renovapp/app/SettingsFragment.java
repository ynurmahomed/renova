package com.renovapp.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ShareActionProvider;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.renovapp.app.SettingsFragment.SettingsFragmentListener} interface
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class SettingsFragment extends Fragment {
    private static final String ARG_NUM_DAYS = "num_days";

    public static final CharSequence TITLE = "Configurações";

    private int numDays;

    private SettingsFragmentListener mListener;

    private TextView notificationPrefText;

    private ShareActionProvider mShareActionProvider;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param numDays quantos dias antes deve se enviar notificações
     * @return A new instance of fragment SettingsFragment.
     */
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
        notificationPrefText.setText(getNotificationSubtitle(numDays));
        notificationPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onNotificationPreferenceClick();
                }
            }
        });
//*
        LinearLayout sharePref = (LinearLayout) rootView.findViewById(R.id.share_preference);
        sharePref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShareClick();
            }
        });
//*/
        LinearLayout logoutPref = (LinearLayout) rootView.findViewById(R.id.logout_preference);
        logoutPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLogoutPreferenceClick();
            }
        });

        return rootView;
    }

    public void onShareClick() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Renove seus livros na UFU com um toque - https://play.google.com/store/apps/details?id=br.ufu.renova";
        //sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Renove seus livros na UFU com um toque");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Compartilhar via"));
    }

    public void onLogoutPreferenceClick() {
        if (mListener != null) {
            mListener.onLogout();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (SettingsFragmentListener) activity;
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
    public interface SettingsFragmentListener {
        public void onNotificationPreferenceClick();
        public void onLogout();
    }

    public void setNumDays(int days) {
        numDays = days;
        notificationPrefText.setText(getNotificationSubtitle(numDays));
    }

    private String getNotificationSubtitle(int numDays) {
        return getResources().getQuantityString(R.plurals.config_notifications, numDays, numDays);
    }

}
