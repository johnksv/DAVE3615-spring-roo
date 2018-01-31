package com.s305089.bookstore.web;
import com.s305089.bookstore.BookOrder;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/bookorders")
@Controller
@RooWebScaffold(path = "bookorders", formBackingObject = BookOrder.class)
public class BookOrderController {
}
