package br.ufu.renova.login;

import android.os.AsyncTask;
import android.util.Log;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.scraper.IHttpClient;
import br.ufu.renova.scraper.LoginException;
import br.ufu.renova.scraper.ScrapeException;

import java.io.IOException;

/**
 * Created by yassin on 11/7/16.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    private PreferencesContract.AppPreferences mPreferences;

    private IHttpClient mHttpClient;

    public LoginPresenter(LoginContract.View loginView, PreferencesContract.AppPreferences preferences, IHttpClient httpClient) {
        mView = loginView;
        mPreferences = preferences;
        mHttpClient = httpClient;
    }

    @Override
    public void start() {
        String login = mPreferences.getLogin();
        String password = mPreferences.getPassword();
        if (!login.isEmpty() && !password.isEmpty()) {
            login(login, password);
        }
    }
    
    @Override
    public void onLoginClick() {
        String login = mView.getLogin();
        String password = mView.getPassword();
        login(login, password);
    }

    private void login(String login, String password) {
        if (login.isEmpty()) {
            mView.showLoginEmptyToast();
        } else if (password.isEmpty()) {
            mView.showPasswordEmptyToast();
        }

        new LoginTask().execute(login, password);
    }

    private void saveLogin(String login, String password) {
        String prefLogin = mPreferences.getLogin();
        String prefPassword = mPreferences.getPassword();
        if (prefLogin.isEmpty() || prefPassword.isEmpty()) {
            mPreferences.setLogin(login);
            mPreferences.setPassword(password);
        }
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            mView.showProgressDialog();
        }

        @Override
        protected Void doInBackground(String... params) {
            String login = params[0];
            String password = params[1];

            try {
                mHttpClient.login(login, password);
                saveLogin(login, password);
            } catch (IOException | ScrapeException | LoginException e) {
                this.e = e;
                Log.e(this.getClass().getName(), "", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void nothing) {

            mView.hideProgressDialog();

            if (this.e != null) {
                mView.showErrorDialog(this.e);
                return;
            }

            if (mPreferences.isFirstRun()) {
                mPreferences.setFirstRun(false);
                mView.startNotificationService();
            }

            mView.showBooksView();
        }
    }
}
