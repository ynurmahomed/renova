package com.renovapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.renovapp.app.scraper.HttpClient;
import com.renovapp.app.scraper.LoginException;

import java.io.IOException;


public class LoginActivity extends Activity implements View.OnClickListener {

    final static String EXTRA_LIBRARY_CLIENT = "EXTRA_LIBRARY_CLIENT";

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressDialog loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        loginEditText = (EditText) findViewById(R.id.login_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        loginButton = (Button) findViewById(R.id.login_button);

        loginProgress = new ProgressDialog(this);
        loginProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        loginProgress.setMessage(getString(R.string.message_login_progress));
        loginProgress.setCancelable(false);

        loginButton.setOnClickListener(this);
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

                return new HttpClient(login, password);

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

            Intent intent = new Intent(LoginActivity.this, BooksActivity.class);
            intent.putExtra(EXTRA_LIBRARY_CLIENT, library);
            startActivity(intent);
        }
    }
}
