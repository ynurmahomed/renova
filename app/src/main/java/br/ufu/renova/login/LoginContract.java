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

        void showErrorDialog(Exception ex);

        void showLoginEmptyToast();

        void showPasswordEmptyToast();

        String getLogin();

        String getPassword();
    }

    interface Presenter extends BasePresenter {
        void onLoginClick();
    }
}
