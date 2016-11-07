package br.ufu.renova.login;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import br.ufu.renova.scraper.HttpClient;
import br.ufu.renova.scraper.IHttpClient;
import br.ufu.renova.scraper.LoginException;
import br.ufu.renova.scraper.ScrapeException;

import java.io.IOException;

/**
 * Created by yassin on 11/7/16.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private static final String PREFERENCE_LOGIN = "pref_login";

    private static final String PREFERENCE_PASSWORD = "pref_password";

    private  static final String PREFERENCE_FIRST_RUN = "pref_first_run";

    private LoginContract.View mLoginView;

    private SharedPreferences mPreferences;

    public LoginPresenter(LoginContract.View loginView, SharedPreferences preferences) {
        this.mLoginView = loginView;
        this.mPreferences = preferences;
    }

    @Override
    public void start() {
        String login = mPreferences.getString(PREFERENCE_LOGIN, "");
        String password = mPreferences.getString(PREFERENCE_PASSWORD, "");
        if (!login.isEmpty() && !password.isEmpty()) {
            login(login, password);
        }
    }

    @Override
    public void login(String login, String password) {
        if (login.isEmpty()) {
            mLoginView.showLoginEmptyToast();
        } else if (password.isEmpty()) {
            mLoginView.showPasswordEmptyToast();
        }

        new LoginTask().execute(login, password);
    }

    private void saveLogin(String login, String password) {
        SharedPreferences.Editor editor = mPreferences.edit();
        String prefLogin = mPreferences.getString(PREFERENCE_LOGIN, "");
        String prefPassword = mPreferences.getString(PREFERENCE_PASSWORD, "");
        if (prefLogin.isEmpty() || prefPassword.isEmpty()) {
            editor.putString(PREFERENCE_LOGIN, login);
            editor.putString(PREFERENCE_PASSWORD, password);
            editor.commit();
        }
    }

    private class LoginTask extends AsyncTask<String, Void, IHttpClient> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            mLoginView.showProgressDialog();
        }

        @Override
        protected IHttpClient doInBackground(String... params) {
            String login = params[0];
            String password = params[1];

            try {

                IHttpClient library = new HttpClient(login, password);
                saveLogin(login, password);


                return library;

            } catch (IOException | ScrapeException | LoginException e) {
                this.e = e;
                Log.e(this.getClass().getName(), "", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(IHttpClient library) {

            mLoginView.hideProgressDialog();

            if (this.e != null) {
                mLoginView.showErrorDialog(this.e);
                return;
            }

            Boolean firstRun = mPreferences.getBoolean(PREFERENCE_FIRST_RUN, true);

            if (firstRun) {
                SharedPreferences.Editor editor = mPreferences.edit();
                editor.putBoolean(PREFERENCE_FIRST_RUN, false);
                editor.commit();
                mLoginView.startNotificationService();
            }

            mLoginView.showBooksView(library);
        }
    }
}
