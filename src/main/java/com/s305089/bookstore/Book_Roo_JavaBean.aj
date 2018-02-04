// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.s305089.bookstore;

import com.s305089.bookstore.Author;
import com.s305089.bookstore.Book;
import com.s305089.bookstore.Category;
import java.util.Calendar;
import java.util.Set;

privileged aspect Book_Roo_JavaBean {
    
    public String Book.getTitle() {
        return this.title;
    }
    
    public void Book.setTitle(String title) {
        this.title = title;
    }
    
    public String Book.getIsbn() {
        return this.isbn;
    }
    
    public void Book.setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public float Book.getCost() {
        return this.cost;
    }
    
    public void Book.setCost(float cost) {
        this.cost = cost;
    }
    
    public int Book.getQuantity() {
        return this.quantity;
    }
    
    public void Book.setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Author Book.getAuthor() {
        return this.author;
    }
    
    public void Book.setAuthor(Author author) {
        this.author = author;
    }
    
    public Set<Category> Book.getCategory() {
        return this.category;
    }
    
    public void Book.setCategory(Set<Category> category) {
        this.category = category;
    }
    
    public Calendar Book.getTimeFactor() {
        return this.timeFactor;
    }
    
    public void Book.setTimeFactor(Calendar timeFactor) {
        this.timeFactor = timeFactor;
    }
    
}
