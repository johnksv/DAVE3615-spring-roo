// WARNING: DO NOT EDIT THIS FILE. THIS FILE IS MANAGED BY SPRING ROO.
// You may push code into the target .java compilation unit if you wish to edit any member(s).

package com.s305089.bookstore;

import com.s305089.bookstore.Author;
import com.s305089.bookstore.Book;
import com.s305089.bookstore.Category;
import java.util.Set;

privileged aspect Author_Roo_JavaBean {
    
    public String Author.getName() {
        return this.name;
    }
    
    public void Author.setName(String name) {
        this.name = name;
    }
    
    public int Author.getAge() {
        return this.age;
    }
    
    public void Author.setAge(int age) {
        this.age = age;
    }
    
    public double Author.getRatings() {
        return this.ratings;
    }
    
    public void Author.setRatings(double ratings) {
        this.ratings = ratings;
    }
    
    public Set<Category> Author.getCategory() {
        return this.category;
    }
    
    public void Author.setCategory(Set<Category> category) {
        this.category = category;
    }
    
    public Set<Book> Author.getBook() {
        return this.book;
    }
    
    public void Author.setBook(Set<Book> book) {
        this.book = book;
    }
    
}
