package br.ufu.renova.preferences;

import br.ufu.renova.BasePresenter;
import br.ufu.renova.BaseView;
import br.ufu.renova.model.User;

/**
 * Created by yassin on 11/10/16.
 */
public interface PreferencesContract {

    interface View extends BaseView<Presenter> {

        void showLogoutConfirmation();

        void showLogin();

        void updateNotificationPreferenceSummary();
    }

    interface Presenter extends BasePresenter {

        void onLogoutClick();

        void logout();
    }

    interface AppPreferences {

        User getUser();

        void setUser(User u);

        boolean isUserSaved();

        void setFirstRun(boolean firstRun);

        boolean isFirstRun();

        int getNotificationAdvance(int defaultNotificationAdvance);
    }

}
