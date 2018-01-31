package com.s305089.bookstore;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;
import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class Shipping {

    /**
     */
    @NotNull
    private String Name;

    /**
     */
    @NotNull
    private String Address;

    /**
     */
    private int postalCode;
}
