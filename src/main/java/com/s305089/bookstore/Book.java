package com.s305089.bookstore;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

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
    private float cost;

    /**
     */
    private int quantity;

    /**
     */
    @ManyToOne
    private Author author;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Category> category = new HashSet<Category>();
}
