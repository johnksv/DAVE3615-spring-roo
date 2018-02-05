package com.s305089.bookstore;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.Calendar;

import org.springframework.format.annotation.DateTimeFormat;

@RooJavaBean
@RooToString
@RooJpaActiveRecord(finders = { "findBooksByAuthor", "findBooksByCategory", "findBooksByTitleEquals", "findBooksByIsbnEquals" })
public class Book {

    /**
     */
    @NotNull
    private String title;

    /**
     */
    @NotNull
    private String isbn;

    /**
     */
    @Min(0)
    private float cost;

    /**
     */
    @Min(0)
    private int quantity;

    /**
     */
    @ManyToOne
    private Author author;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> category = new HashSet<Category>();

    /**
     */
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Calendar timeFactor;

    /**
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(length = 10 * 1024 * 1024) //10 MB limit. Also so we don't get exception if we update/change image.
    private byte[] image;

    /**
     */
    private String contentType;
}
