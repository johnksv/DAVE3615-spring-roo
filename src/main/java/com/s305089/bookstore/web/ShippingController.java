package com.s305089.bookstore.web;
import com.s305089.bookstore.Shipping;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/shippings")
@Controller
@RooWebScaffold(path = "shippings", formBackingObject = Shipping.class)
public class ShippingController {
}
