package com.renovapp.app;

import com.renovapp.app.scraper.Book;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by pablohenrique on 11/24/14.
 */
public class BooksListGlobal {
    private static BooksListGlobal instance;
    private int alertDays = 2;
    private List<Book> bookList = new ArrayList<Book>();

    private BooksListGlobal(){}

    public static BooksListGlobal getInstance(){
        if(instance == null)
            instance = new BooksListGlobal();
        return instance;
    }

    public void setBookList(List<Book> b){ this.bookList = b; }
    public List<Book> getBookList(){ return this.bookList; }

    public int getAlertDays(){ return this.alertDays; }
    public void setAlertDays(int days){ this.alertDays = days; }
}
