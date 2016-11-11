package br.ufu.renova.preferences;

/**
 * Created by yassin on 11/10/16.
 */
public class PreferencesPresenter implements PreferencesContract.Presenter {

    private PreferencesContract.View mView;

    private PreferencesContract.AppPreferences mPreferences;

    public PreferencesPresenter(PreferencesContract.View view, PreferencesContract.AppPreferences preferences) {
        mView = view;
        mPreferences = preferences;
    }

    @Override
    public void start() {
        mView.setNotificationAdvance(mPreferences.getNotificationAdvance());
    }

    @Override
    public void logout() {
        mPreferences.setLogin("");
        mPreferences.setPassword("");
        mView.showLogin();
    }

    @Override
    public void setNotificationAdvance(int days) {
        mPreferences.setNotificationAdvance(days);
        mView.setNotificationAdvance(days);
    }

    @Override
    public void onNotificationPreferenceClick() {
        mView.showNumberPicker();
    }

    @Override
    public void onSharePreferenceClick() {
        mView.showShareApp();
    }

    @Override
    public void onRatePreferenceClick() {
        mView.showRateApp();
    }

    @Override
    public void onLogoutClick() {
        mView.showLogoutConfirmation();
    }
}
