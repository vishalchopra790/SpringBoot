package com.vishal.controller;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Storage2")
public class Library {
    @Column(name="book_name")
    private String bookName;
    @Id
    @Column(name="id")
    private String id;
    @Column(name="isbn")
    private String isbn;
    @Column(name="aisle")
    private int aisle;
    @Column(name="author")
    private String author;

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public void setAisle(int aisle) {
        this.aisle = aisle;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getBookName() {
        return bookName;
    }

    public String getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getAisle() {
        return aisle;
    }

    public String getAuthor() {
        return author;
    }
}
