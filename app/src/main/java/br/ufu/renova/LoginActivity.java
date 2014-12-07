package br.ufu.renova;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import br.ufu.renova.R;
import br.ufu.renova.notification.NotificationServiceScheduleReceiver;
import br.ufu.renova.scraper.HttpClient;
import br.ufu.renova.scraper.LoginException;

import java.io.IOException;


public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressDialog loginProgress;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginEditText = (EditText) findViewById(R.id.login_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        loginProgress = new ProgressDialog(this);
        loginProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loginProgress.setMessage(getString(R.string.message_login_progress));
        loginProgress.setCancelable(false);

        // Verifica se o login e o password já estão gravados no preferences e tenta fazer
        // login automático.
        prefs = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String login = prefs.getString(getString(R.string.preference_login), "");
        String password = prefs.getString(getString(R.string.preference_password), "");
        if (!(login.isEmpty() || password.isEmpty())) {
            loginEditText.setText(login);
            passwordEditText.setText(password);
            loginButton.performClick();
        }
    }

    @Override
    protected void onPause() {
        loginProgress.dismiss();
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        String login = loginEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        Toast toast = null;
        if (login.isEmpty()) {
            toast = Toast.makeText(this, R.string.message_login_empty, Toast.LENGTH_SHORT);
        } else if (password.isEmpty()) {
            toast = Toast.makeText(this, R.string.message_password_empty, Toast.LENGTH_SHORT);
        }
        if (toast != null) {
            toast.show();
            return;
        }



        new LoginTask().execute(login, password);
    }

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
            e.printStackTrace();
        }

        builder
            .setPositiveButton("OK", null)
            .show();
    }

    private class LoginTask extends AsyncTask<String,Void,HttpClient> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            LoginActivity.this.loginProgress.show();
        }

        @Override
        protected HttpClient doInBackground(String... params) {
            String login = params[0];
            String password = params[1];

            try {

                HttpClient library = new HttpClient(login, password);

                // Grava o login e o password no shared preferences se não estiver gravado.
                SharedPreferences.Editor editor = prefs.edit();
                String prefLogin = prefs.getString(getString(R.string.preference_login), "");
                String prefPassword = prefs.getString(getString(R.string.preference_password), "");
                if (prefLogin.isEmpty() || prefPassword.isEmpty()) {
                    editor.putString(getString(R.string.preference_login), login);
                    editor.putString(getString(R.string.preference_password), password);
                    editor.commit();
                }

                return library;

            } catch (IOException e) {
                this.e = e;
                Log.d("LoginTask", e.toString());
                e.printStackTrace();
            } catch (LoginException e) {
                this.e = e;
                Log.d("LoginTask", "Login Error");
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(HttpClient library) {

            LoginActivity.this.loginProgress.hide();

            if (this.e != null) {
                LoginActivity.this.showErrorDialog(this.e);
                return;
            }

            Boolean firstRun = prefs.getBoolean(getString(R.string.preference_first_run), true);

            if (firstRun) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(getString(R.string.preference_first_run), false);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, NotificationServiceScheduleReceiver.class);
                sendBroadcast(intent);
            }

            Intent intent = new Intent(LoginActivity.this, AppActivity.class);
            intent.putExtra(AppActivity.EXTRA_LIBRARY_CLIENT, library);
            startActivity(intent);
        }
    }
}