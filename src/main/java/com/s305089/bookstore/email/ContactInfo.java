package com.s305089.bookstore.email;
import org.springframework.roo.addon.javabean.RooJavaBean;
import org.springframework.roo.addon.jpa.activerecord.RooJpaActiveRecord;
import org.springframework.roo.addon.tostring.RooToString;

import javax.validation.constraints.NotNull;

@RooJavaBean
@RooToString
@RooJpaActiveRecord
public class ContactInfo {

    /**
     */
    @NotNull
    private String fromName;

    /**
     */
    @NotNull
    private String message;

    /**
     */
    @NotNull
    private String subject;

    /**
     */
    @NotNull
    private String fromEmail;
}
