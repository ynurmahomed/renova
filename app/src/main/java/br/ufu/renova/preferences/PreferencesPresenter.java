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
        mView.updateNotificationPreferenceSummary();
    }

    @Override
    public void logout() {
        mPreferences.setUser(null);
        mView.showLogin();
    }

    @Override
    public void onLogoutClick() {
        mView.showLogoutConfirmation();
    }
}
