package com.s305089.bookstore;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.persistence.ManyToOne;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.ManyToMany;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class BookOrder {

    /**
     */
    @ManyToOne
    private Shipping shipping;

    /**
     */
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Book> books = new HashSet<Book>();
}
