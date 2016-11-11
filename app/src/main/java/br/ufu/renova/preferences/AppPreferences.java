package br.ufu.renova.preferences;

import android.content.SharedPreferences;

/**
 * Created by yassin on 11/10/16.
 */
public class AppPreferences implements PreferencesContract.AppPreferences {

    private static final String LOGIN = "pref_login";

    private static final String PASSWORD = "pref_password";

    private static final String FIRST_RUN = "pref_first_run";

    private static final String NOTIFICATION_ADVANCE = "pref_notifications";

    private static final int DEFAULT_NOTIFICATION_ADVANCE = 2;

    private SharedPreferences mPreferences;

    public AppPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }

    @Override
    public String getLogin() {
        return mPreferences.getString(LOGIN, "");
    }

    @Override
    public void setLogin(String login) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(LOGIN, login);
        editor.commit();
    }

    @Override
    public String getPassword() {
        return mPreferences.getString(PASSWORD, "");
    }

    @Override
    public void setPassword(String password) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(PASSWORD, password);
        editor.commit();
    }

    @Override
    public int getNotificationAdvance() {
        return mPreferences.getInt(NOTIFICATION_ADVANCE, DEFAULT_NOTIFICATION_ADVANCE);
    }

    @Override
    public void setNotificationAdvance(int days) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(NOTIFICATION_ADVANCE, days);
        editor.commit();
    }
}
