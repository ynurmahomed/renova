package br.ufu.renova;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import br.ufu.renova.preferences.AppPreferences;
import br.ufu.renova.preferences.PreferencesContract;
import br.ufu.renova.scraper.ILibraryDataSource;
import br.ufu.renova.scraper.UFULibraryDataSource;

/**
 * Classe para injeção de depenências manualmente.
 *
 * @author yassin on 11/22/16.
 */
public class Injection {

    public static PreferencesContract.AppPreferences provideAppPreferences(Context context) {
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return new AppPreferences(defaultSharedPreferences);
    }

    public static ILibraryDataSource provideDataSource() {
        return UFULibraryDataSource.getInstance();
    }

    /**
     * @return Intervalo de repetição do serviço de notificação em milisegundos.
     */
    public static long provideNotificationRepeatTime() {
        return 1000 * 60 * 60 * 24;
    }
}
