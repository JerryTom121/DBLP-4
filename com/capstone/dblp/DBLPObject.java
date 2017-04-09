/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.capstone.dblp;

import java.util.*;
import java.io.*;
/**
 *
 * @author jhyeh
 */
public class DBLPObject implements Serializable {
    private String key;
    private ArrayList authors;
    private int year;
    private String bookTitle;
    private String title;
    private ArrayList citation;

    public DBLPObject() {
        authors = new ArrayList();
        citation = new ArrayList();
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getKey() {
        return this.key;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setBookTitle(String str) {
        this.bookTitle = str;
    }

    public String getBookTitle() {
        return this.bookTitle;
    }

    public void setYear(int i) {
        this.year = i;
    }

    public int getYear() {
        return this.year;
    }

    public void addAuthor(String str) {
        authors.add(str);
    }

    public ArrayList getAuthors() {
        return this.authors;
    }

    public void addCitation(String str) {
        citation.add(str);
    }

    public ArrayList getCitations() {
        return this.citation;
    }
}
