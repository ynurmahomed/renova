package br.ufu.renova.preferences;

import br.ufu.renova.BasePresenter;
import br.ufu.renova.BaseView;

/**
 * Created by yassin on 11/10/16.
 */
public interface PreferencesContract {

    interface View extends BaseView<Presenter> {

        void showNumberPicker();

        void showShareApp();

        void showRateApp();

        void showLogoutConfirmation();

        void showLogin();

        void setNotificationAdvance(int days);
    }

    interface Presenter extends BasePresenter {

        void setNotificationAdvance(int days);

        void onNotificationPreferenceClick();

        void onSharePreferenceClick();

        void onRatePreferenceClick();

        void onLogoutClick();

        void logout();
    }

    interface AppPreferences {

        String getLogin();

        void setLogin(String login);

        String getPassword();

        void setPassword(String password);

        int getNotificationAdvance();

        void setNotificationAdvance(int days);

        void setFirstRun(boolean firstRun);

        boolean isFirstRun();
    }

}
