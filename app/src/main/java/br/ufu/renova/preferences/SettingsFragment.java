package br.ufu.renova.preferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import br.ufu.renova.R;
import br.ufu.renova.login.LoginActivity;

/**
 * Created by yassin on 1/4/17.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements PreferencesContract.View, SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String NOTIFICATION_ADVANCE = "pref_notifications";
    private static final int DEFAULT_NOTIFICATION_ADVANCE = 2;

    private PreferencesContract.Presenter mPresenter;

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        Preference logout = findPreference("logout");
        logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                mPresenter.onLogoutClick();
                return true;
            }
        });
        setRetainInstance(true);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment f;
        if (preference instanceof NumberPickerPreference) {
            NumberPickerPreference numberPicker = (NumberPickerPreference) preference;
            f = NumberPickerPreferenceDialogFragmentCompat.newInstance(preference.getKey(),numberPicker.getMinValue(), numberPicker.getMaxValue());
            f.setTargetFragment(this, 0);
            f.show(getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void setPresenter(PreferencesContract.Presenter presenter) {
        mPresenter = presenter;
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
    public void updateNotificationPreferenceSummary() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        Preference notification = findPreference(getResources().getString(R.string.preference_notifications));
        int days = sharedPreferences.getInt(NOTIFICATION_ADVANCE, DEFAULT_NOTIFICATION_ADVANCE);
        String summary = getResources().getQuantityString(R.plurals.config_notifications, days, days);
        notification.setSummary(summary);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.preference_notifications))) {
            updateNotificationPreferenceSummary();
        }
    }
}
