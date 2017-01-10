package br.ufu.renova;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.ufu.renova.preferences.AppPreferences;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.scraper.DecoratedUFULibraryDataSourceMock;
import br.ufu.renova.scraper.ILibraryDataSource;
import br.ufu.renova.scraper.UFULibraryDataSourceMock;
import br.ufu.renova.util.EspressoIdlingResource;

/**
 * Classe para injeção de depenências manualmente.
 *
 * @author yassin on 11/15/16.
 */
public class Injection {

    public static PreferencesContract.AppPreferences provideAppPreferences(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new AppPreferences(defaultSharedPreferences);
    }

    public static ILibraryDataSource provideDataSource() {
        UFULibraryDataSourceMock dataSource = UFULibraryDataSourceMock.getInstance();
        return new DecoratedUFULibraryDataSourceMock(dataSource, EspressoIdlingResource.getIdlingResource());
    }

    /**
     * @return Intervalo de repetição do serviço de notificação em milisegundos.
     */
    public static long provideNotificationRepeatTime() {
        return 1000 * 60;
    }
}
