package br.ufu.renova;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.ufu.renova.preferences.AppPreferences;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.scraper.DecoratedUFUHttpClientMock;
import br.ufu.renova.scraper.IHttpClient;
import br.ufu.renova.scraper.UFUHttpClientMock;
import br.ufu.renova.util.EspressoIdlingResource;

/**
 * Created by yassin on 11/15/16.
 */
public class Injection {

    public static PreferencesContract.AppPreferences provideAppPreferences(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new AppPreferences(defaultSharedPreferences);
    }

    public static IHttpClient provideHttpClient() {
        UFUHttpClientMock httpClient = UFUHttpClientMock.getInstance();
        return new DecoratedUFUHttpClientMock(httpClient, EspressoIdlingResource.getIdlingResource());
    }
}
