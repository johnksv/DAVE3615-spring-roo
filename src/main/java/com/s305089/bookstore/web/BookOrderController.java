package com.s305089.bookstore.web;
import com.s305089.bookstore.Book;
import com.s305089.bookstore.BookOrder;
import com.s305089.bookstore.Shipping;
import java.io.UnsupportedEncodingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;

@RequestMapping("/bookorders")
@Controller
@RooWebScaffold(path = "bookorders", formBackingObject = BookOrder.class)
public class BookOrderController {

	@RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid BookOrder bookOrder, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bookOrder);
            return "bookorders/create";
        }
        uiModel.asMap().clear();
        bookOrder.persist();
        return "redirect:/bookorders/" + encodeUrlPathSegment(bookOrder.getId().toString(), httpServletRequest);
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new BookOrder());
        return "bookorders/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("bookorder", BookOrder.findBookOrder(id));
        uiModel.addAttribute("itemId", id);
        return "bookorders/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("bookorders", BookOrder.findBookOrderEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) BookOrder.countBookOrders() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("bookorders", BookOrder.findAllBookOrders(sortFieldName, sortOrder));
        }
        return "bookorders/list";
    }

	@RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid BookOrder bookOrder, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, bookOrder);
            return "bookorders/update";
        }
        uiModel.asMap().clear();
        bookOrder.merge();
        return "redirect:/bookorders/" + encodeUrlPathSegment(bookOrder.getId().toString(), httpServletRequest);
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, BookOrder.findBookOrder(id));
        return "bookorders/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        BookOrder bookOrder = BookOrder.findBookOrder(id);
        bookOrder.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/bookorders";
    }

	void populateEditForm(Model uiModel, BookOrder bookOrder) {
        uiModel.addAttribute("bookOrder", bookOrder);
        uiModel.addAttribute("books", Book.findAllBooks());
        uiModel.addAttribute("shippings", Shipping.findAllShippings());
    }

	String encodeUrlPathSegment(String pathSegment, HttpServletRequest httpServletRequest) {
        String enc = httpServletRequest.getCharacterEncoding();
        if (enc == null) {
            enc = WebUtils.DEFAULT_CHARACTER_ENCODING;
        }
        try {
            pathSegment = UriUtils.encodePathSegment(pathSegment, enc);
        } catch (UnsupportedEncodingException uee) {}
        return pathSegment;
    }
}
