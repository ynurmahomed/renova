package br.ufu.renova.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.ufu.renova.AppActivity;
import br.ufu.renova.R;
import br.ufu.renova.notification.NotificationServiceScheduleReceiver;
import br.ufu.renova.preferences.AppPreferences;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.scraper.UFUHttpClient;
import br.ufu.renova.scraper.IHttpClient;
import br.ufu.renova.scraper.LoginException;

import java.io.IOException;


public class LoginActivity extends Activity implements LoginContract.View {

    private LoginContract.Presenter mPresenter;

    private EditText mLoginEditText;

    private EditText mPasswordEditText;

    private ProgressDialog mLoginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mLoginEditText = (EditText) findViewById(R.id.login_edit_text);
        mPasswordEditText = (EditText) findViewById(R.id.password_edit_text);

        Button loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onLoginClick();
            }
        });

        mLoginProgress = new ProgressDialog(this);
        mLoginProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mLoginProgress.setMessage(getString(R.string.message_login_progress));
        mLoginProgress.setCancelable(false);

        SharedPreferences mPreferences = getPreferences(Context.MODE_PRIVATE);
        AppPreferences appPreferences = new AppPreferences(mPreferences);
        IHttpClient mHttpClient = UFUHttpClient.getInstance();
        setPresenter(new LoginPresenter(this, appPreferences, mHttpClient));
    }

    @Override
    protected void onPause() {
        mLoginProgress.dismiss();
        super.onPause();
    }

    @Override
    public void showProgressDialog() {
        mLoginProgress.show();
    }

    @Override
    public void hideProgressDialog() {
        mLoginProgress.hide();
    }

    @Override
    public void startNotificationService() {
        Intent intent = new Intent(LoginActivity.this, NotificationServiceScheduleReceiver.class);
        sendBroadcast(intent);
    }

    @Override
    public void showBooksView() {
        Intent intent = new Intent(LoginActivity.this, AppActivity.class);
        startActivity(intent);
    }

    @Override
    public void showErrorDialog(Exception ex) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        try {

            throw ex;

        } catch (IOException e) {
            builder
                .setTitle(R.string.title_connection_error)
                .setMessage(R.string.message_connection_error);

        } catch (LoginException e) {
            builder
                .setTitle(R.string.title_incorrect_login)
                .setMessage(R.string.message_incorrect_login);

        } catch (Exception e) {
            builder
                .setTitle(R.string.title_generic_error)
                .setMessage(R.string.message_unprocessable_response);
            Log.e(this.getClass().getName(), "", e);
        }

        builder
            .setPositiveButton("OK", null)
            .show();
    }

    @Override
    public void showLoginEmptyToast() {
        Toast.makeText(LoginActivity.this, R.string.message_login_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPasswordEmptyToast() {
        Toast.makeText(LoginActivity.this, R.string.message_password_empty, Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getLogin() {
        return mLoginEditText.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mPasswordEditText.getText().toString().trim();
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.mPresenter = presenter;
    }
}