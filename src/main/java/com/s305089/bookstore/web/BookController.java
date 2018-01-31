package com.s305089.bookstore.web;
import com.s305089.bookstore.Book;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/books")
@Controller
@RooWebScaffold(path = "books", formBackingObject = Book.class)
public class BookController {
}
