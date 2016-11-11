package br.ufu.renova.preferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.ufu.renova.R;
import br.ufu.renova.login.LoginActivity;

public class PreferencesFragment extends Fragment implements PreferencesContract.View {

    public static final CharSequence TITLE = "Configurações";

    private PreferencesContract.Presenter mPresenter;

    private int mNotificationAdvance;

    private TextView mNotificationPrefDescription;

    public PreferencesFragment() {
        // Required empty public constructor
    }

    public static PreferencesFragment newInstance() {
        return new PreferencesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_preferences, container, false);

        LinearLayout notificationPref = (LinearLayout) rootView.findViewById(R.id.notification_preference);
        mNotificationPrefDescription = (TextView) rootView.findViewById(R.id.notification_preference_value);
        mNotificationPrefDescription.setText(getNotificationSubtitle(mNotificationAdvance));

        notificationPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onNotificationPreferenceClick();
            }
        });

        LinearLayout sharePref = (LinearLayout) rootView.findViewById(R.id.share_preference);
        sharePref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onSharePreferenceClick();
            }
        });

        LinearLayout rateRenovapp = (LinearLayout) rootView.findViewById(R.id.rate_renovapp);
        rateRenovapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onRatePreferenceClick();
            }
        });

        LinearLayout logoutPref = (LinearLayout) rootView.findViewById(R.id.logout_preference);
        logoutPref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onLogoutClick();
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void showNumberPicker() {
        DialogFragment numberPickerDialog = NumberPickerDialogFragment.newInstance("Antecedência", 1, 7, mNotificationAdvance, R.plurals.dia, mHandler);
        numberPickerDialog.show(getActivity().getSupportFragmentManager(), "NumberPicker");
    }

    @Override
    public void showShareApp() {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Renove seus livros na biblioteca UFU com um toque - https://play.google.com/store/apps/details?id=br.ufu.renova";
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Compartilhar via"));
    }

    @Override
    public void showRateApp() {
        Uri marketUri = Uri.parse("market://details?id=br.ufu.renova");
        Intent marketIntent = new Intent(Intent.ACTION_VIEW).setData(marketUri);
        startActivity(marketIntent);
    }

    @Override
    public void showLogoutConfirmation() {
        new AlertDialog.Builder(getContext())
            .setTitle(getString(R.string.leave_dialog_title))
            .setMessage(getString(R.string.leave_dialog_message))
            .setPositiveButton(getString(R.string.leave_dialog_positive_text), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mPresenter.logout();
                }
            })
            .setNegativeButton(getString(R.string.dialog_negative_text), null)
            .show();
    }

    @Override
    public void showLogin() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void setNotificationAdvance(int days) {
        mNotificationAdvance = days;
        mNotificationPrefDescription.setText(getNotificationSubtitle(mNotificationAdvance));
    }

    @Override
    public void setPresenter(PreferencesContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private String getNotificationSubtitle(int numDays) {
        return getResources().getQuantityString(R.plurals.config_notifications, numDays, numDays);
    }

    private NumberPickerDialogFragment.NumberPickerDialogFragmentResultHandler mHandler = new NumberPickerDialogFragment.NumberPickerDialogFragmentResultHandler() {
        @Override
        public void handleNumberPickerDialogFragmentResult(int result) {
            mPresenter.setNotificationAdvance(result);
        }
    };
}
