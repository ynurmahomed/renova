package br.ufu.renova.preferences;

import android.content.SharedPreferences;
import br.ufu.renova.model.User;

/**
 * Created by yassin on 11/10/16.
 */
public class AppPreferences implements PreferencesContract.AppPreferences {

    private static final String USERNAME = "pref_username";

    private static final String PASSWORD = "pref_password";

    private static final String FIRST_RUN = "pref_first_run";

    private static final String NOTIFICATION_ADVANCE = "pref_notifications";

    private static final int DEFAULT_NOTIFICATION_ADVANCE = 2;

    private SharedPreferences mPreferences;

    public AppPreferences(SharedPreferences mPreferences) {
        this.mPreferences = mPreferences;
    }

    @Override
    public User getUser() {
        return new User(
            mPreferences.getString(USERNAME, ""),
            mPreferences.getString(PASSWORD, "")
        );
    }

    @Override
    public void setUser(User u) {
        if (u == null) {
            u = new User("","");
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putString(USERNAME, u.getUsername());
        editor.putString(PASSWORD, u.getPassword());
        editor.apply();
    }

    @Override
    public boolean isUserSaved() {
        User user = getUser();
        return !user.getUsername().isEmpty() && !user.getPassword().isEmpty();
    }

    @Override
    public int getNotificationAdvance() {
        return mPreferences.getInt(NOTIFICATION_ADVANCE, DEFAULT_NOTIFICATION_ADVANCE);
    }

    @Override
    public void setNotificationAdvance(int days) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(NOTIFICATION_ADVANCE, days);
        editor.apply();
    }

    @Override
    public void setFirstRun(boolean firstRun) {
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putBoolean(FIRST_RUN, firstRun);
        editor.apply();
    }

    @Override
    public boolean isFirstRun() {
        return mPreferences.getBoolean(FIRST_RUN, true);
    }
}
