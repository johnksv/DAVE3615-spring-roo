package com.s305089.bookstore;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Author {

    /**
     */
    @NotNull
    private String name;

    /**
     */
    @NotNull
    @Min(0)
    private int age;

    /**
     */
    private double ratings;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> category = new HashSet<Category>();

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "author")
    private Set<Book> book = new HashSet<Book>();
}
