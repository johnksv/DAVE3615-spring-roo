package com.s305089.bookstore.web;

import com.s305089.bookstore.Book;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.roo.addon.web.mvc.controller.scaffold.RooWebScaffold;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.roo.addon.web.mvc.controller.finder.RooWebFinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

@RequestMapping("/books")
@Controller
@RooWebScaffold(path = "books", formBackingObject = Book.class)
@RooWebFinder
public class BookController {

    private static final Logger logger = Logger.getLogger(BookController.class);

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) throws ServletException {
        binder.registerCustomEditor(byte[].class,
                new ByteArrayMultipartFileEditor());
    }

    @RequestMapping(method = RequestMethod.POST, produces = "text/html")
    public String create(@Valid Book book, BindingResult bindingResult, Model uiModel,
                         @RequestParam("image") MultipartFile multipartFile, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, book);
            return "books/create";
        }
        uiModel.asMap().clear();
        book.setContentType(multipartFile.getContentType());
        logger.info("image of type " + book.getContentType());
        logger.info("img byte length: " + book.getImage().length);
        book.persist();
        return "redirect:/books/" + encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
    }

    @RequestMapping(value = "/{id}/image", method = RequestMethod.GET)
    public String showImage(@PathVariable("id") Long id, HttpServletResponse response, Model model) throws IOException {
        Book book = Book.findBook(id);
        if (book != null) {
            byte[] image = book.getImage();
            if (image != null) {
                response.setContentType(book.getContentType());
                OutputStream out = response.getOutputStream();
                IOUtils.copy(new ByteArrayInputStream(image), out);
                out.flush();
            }
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.PUT, produces = "text/html")
    public String update(@Valid Book book, BindingResult bindingResult, Model uiModel, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, book);
            return "books/update";
        }
        uiModel.asMap().clear();
        book.merge();
        return "redirect:/books/" + encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
    }

}
