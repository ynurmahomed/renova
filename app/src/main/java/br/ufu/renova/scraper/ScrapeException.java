package br.ufu.renova.scraper;

/**
 * Exceção genérica para erro ao processar alguma resposta do servidor.
 *
 * Created by yassin on 10/27/16.
 */
public class ScrapeException extends Exception {

    ScrapeException(Throwable cause) {
        super(cause);
    }
}
