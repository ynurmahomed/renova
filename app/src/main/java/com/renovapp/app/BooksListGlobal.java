package com.renovapp.app;

import com.renovapp.app.scraper.Book;

import java.util.HashMap;

/**
 * Created by pablohenrique on 11/24/14.
 */
public class BooksListGlobal {
    private static BooksListGlobal instance;
    private HashMap<String, Book> bookHashMap = new HashMap<String, Book>();

    private BooksListGlobal(){}

    public static BooksListGlobal getInstance(){
        if(instance == null)
            instance = new BooksListGlobal();
        return instance;
    }

    public void setBook(Book b){
        this.bookHashMap.put(b.getBarcode(), b);
    }

    public HashMap<String, Book> getAllBooks(){
        return this.bookHashMap;
    }

    public Book getBook(String key){
        return this.bookHashMap.get(key);
    }
}
