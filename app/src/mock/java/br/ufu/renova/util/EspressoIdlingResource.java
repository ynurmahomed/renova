package br.ufu.renova.util;

import android.support.test.espresso.idling.CountingIdlingResource;

/**
 * Esta classe guarda uma instância global de {@link CountingIdlingResource} para ser usado
 * durante os testes de tela.
 *
 * Created by yassin on 11/22/16.
 */
public class EspressoIdlingResource {

    private static final String NAME = "Global";

    private static CountingIdlingResource mCountingResource = new CountingIdlingResource(NAME);

    public static CountingIdlingResource getIdlingResource() {
        return mCountingResource;
    }
}
