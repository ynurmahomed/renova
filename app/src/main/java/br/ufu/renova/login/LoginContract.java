package br.ufu.renova.login;

import br.ufu.renova.BasePresenter;
import br.ufu.renova.BaseView;

/**
 * Created by yassin on 11/7/16.
 */
public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void showProgressDialog();

        void hideProgressDialog();

        void startNotificationService();

        void showBooksView();

        void showLoginErrorDialog();

        void showConnectionErrorDialog();

        void showErrorDialog();

        void showLoginEmptyToast();

        void showPasswordEmptyToast();

        String getUsername();

        String getPassword();
    }

    interface Presenter extends BasePresenter {
        void onLoginClick();
    }
}
