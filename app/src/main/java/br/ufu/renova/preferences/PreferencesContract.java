package br.ufu.renova.preferences;

import br.ufu.renova.BasePresenter;
import br.ufu.renova.BaseView;
import br.ufu.renova.model.User;

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

        User getUser();

        void setUser(User u);

        boolean isUserSaved();

        int getNotificationAdvance();

        void setNotificationAdvance(int days);

        void setFirstRun(boolean firstRun);

        boolean isFirstRun();
    }

}
