package com.renovapp.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.renovapp.app.scraper.HttpClient;
import com.renovapp.app.scraper.LoginException;

import java.io.IOException;


public class LoginActivity extends Activity implements View.OnClickListener {

    final static String EXTRA_LIBRARY_CLIENT = "EXTRA_LIBRARY_CLIENT";

    private EditText loginEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private ProgressDialog loginProgress;

    private AdView adView;
    private static final String AD_UNIT_ID = "ca-app-pub-6713098943014804/6078729371";
    //private static final String AD_UNIT_ID = "ca-app-pub-6713098943014804/4601996177";

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

        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        LinearLayout layout = (LinearLayout) findViewById(R.id.login_form);
        RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        lay.addRule(RelativeLayout.ALIGN_BOTTOM, RelativeLayout.TRUE);
        lay.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);

        //adView.setLayoutParams(lay);
        layout.addView(adView,lay);

        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device.
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice("E0DC64EC0BFF17A4F00640C5294B0128")
                .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);

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

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        loginProgress.dismiss();
        super.onPause();
    }

    /** Called before the activity is destroyed. */
    @Override
    public void onDestroy() {
        // Destroy the AdView.
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
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
