package br.ufu.renova.login;

import br.ufu.renova.model.User;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.datasource.ILibraryDataSource;
import br.ufu.renova.scraper.LoginException;

import java.io.IOException;

/**
 * Created by yassin on 11/7/16.
 */
public class LoginPresenter implements LoginContract.Presenter {

    private LoginContract.View mView;

    private PreferencesContract.AppPreferences mPreferences;

    private ILibraryDataSource mDataSource;

    public LoginPresenter(LoginContract.View loginView, PreferencesContract.AppPreferences preferences, ILibraryDataSource dataSource) {
        mView = loginView;
        mPreferences = preferences;
        mDataSource = dataSource;
    }

    @Override
    public void start() {
        if (mPreferences.isUserSaved()) {
            User user = mPreferences.getUser();
            login(user.getUsername(), user.getPassword());
        }
    }
    
    @Override
    public void onLoginClick() {
        String username = mView.getUsername();
        String password = mView.getPassword();

        if (username.isEmpty()) {
            mView.showLoginEmptyToast();
            return;
        } else if (password.isEmpty()){
            mView.showPasswordEmptyToast();
            return;
        }

        login(username, password);
    }

    private void login(final String username, final String password) {
        mView.showProgressDialog();
        mDataSource.login(username, password, new ILibraryDataSource.LoginCallback() {
            @Override
            public void onComplete(User user) {
                if (!mPreferences.isUserSaved()) {
                    mPreferences.setUser(user);
                }
                if (mPreferences.isFirstRun()) {
                    mPreferences.setFirstRun(false);
                    mView.startNotificationService();
                }
                mView.hideProgressDialog();
                mView.showBooksView();
            }

            @Override
            public void onError(Exception e) {
                mView.hideProgressDialog();
                try {
                    throw e;
                } catch (IOException ex) {
                    mView.showConnectionErrorDialog();
                } catch (LoginException ex) {
                    mView.showLoginErrorDialog();
                } catch (Exception ex) {
                    mView.showErrorDialog();
                }
            }
        });
    }
}
