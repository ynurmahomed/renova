package com.renovapp.app.scraper;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yassin on 30/10/14.
 */
public class Book implements Serializable {
    public enum State {
        INITIAL("", false),
        RENEWED("", false),
        INVALID_RENEW_DATE("O livro não pode ser renovado na data atual.", true),
        REQUESTED("O livro foi solicitado.", true),
        RENEW_LIMIT_REACHED("O limite de renovações do livro foi alcançado.", true);

        public final String msg;
        public final boolean isErrorState;

        State(String msg, boolean isErrorState) {
            this.msg = msg;
            this.isErrorState = isErrorState;
        }
    };
    private State state = State.INITIAL;
    private String barcode;
    private String title;
    private String authors;
    private Date expiration;
    private String callNumber;
    private String note;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String toString() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return title + " - " + authors + " - " + dateFormat.format(expiration);
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
