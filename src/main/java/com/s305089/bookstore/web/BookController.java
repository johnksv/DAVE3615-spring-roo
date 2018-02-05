package com.s305089.bookstore.web;

import com.s305089.bookstore.Author;
import com.s305089.bookstore.Book;
import com.s305089.bookstore.Category;
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
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.support.ByteArrayMultipartFileEditor;
import org.springframework.web.util.UriUtils;
import org.springframework.web.util.WebUtils;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Set;

@RequestMapping("/books")
@Controller
@RooWebScaffold(path = "books", formBackingObject = Book.class)
@RooWebFinder
public class BookController {

    private static final Logger logger = Logger.getLogger(BookController.class);

    //Thank you to raginggoblin for example
    //https://raginggoblin.wordpress.com/2013/05/05/spring-roo-8-uploading-images/

    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(byte[].class, new ByteArrayMultipartFileEditor());
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
        logger.info("image of type " + book.getContentType() + ", image size: " + multipartFile.getSize());
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

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "text/html")
    public String update(@Valid Book book, BindingResult bindingResult, Model uiModel,
                         @RequestParam("image") MultipartFile multipartFile, HttpServletRequest httpServletRequest) {
        if (bindingResult.hasErrors()) {
            populateEditForm(uiModel, book);
            return "books/update";
        }
        uiModel.asMap().clear();

        byte[] image = book.getImage();
        if (image.length != 0) {
            logger.info("new image uploaded");
            book.setContentType(multipartFile.getContentType());
        } else {
            logger.info("No new image provided, use old");
            Book dbBook = Book.findBook(book.getId());
            book.setContentType(dbBook.getContentType());
            book.setImage(dbBook.getImage());
        }

        book.merge();
        return "redirect:/books/" + encodeUrlPathSegment(book.getId().toString(), httpServletRequest);
    }


	@RequestMapping(params = { "find=ByAuthor", "form" }, method = RequestMethod.GET)
    public String findBooksByAuthorForm(Model uiModel) {
        uiModel.addAttribute("authors", Author.findAllAuthors());
        return "books/findBooksByAuthor";
    }

	@RequestMapping(params = "find=ByAuthor", method = RequestMethod.GET)
    public String findBooksByAuthor(@RequestParam("author") Author author, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("books", Book.findBooksByAuthor(author, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Book.countFindBooksByAuthor(author) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("books", Book.findBooksByAuthor(author, sortFieldName, sortOrder).getResultList());
        }
        return "books/list";
    }

	@RequestMapping(params = { "find=ByCategory", "form" }, method = RequestMethod.GET)
    public String findBooksByCategoryForm(Model uiModel) {
        uiModel.addAttribute("categorys", Category.findAllCategorys());
        return "books/findBooksByCategory";
    }

	@RequestMapping(params = "find=ByCategory", method = RequestMethod.GET)
    public String findBooksByCategory(@RequestParam("category") Set<Category> category, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("books", Book.findBooksByCategory(category, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Book.countFindBooksByCategory(category) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("books", Book.findBooksByCategory(category, sortFieldName, sortOrder).getResultList());
        }
        return "books/list";
    }

	@RequestMapping(params = { "find=ByIsbnEquals", "form" }, method = RequestMethod.GET)
    public String findBooksByIsbnEqualsForm(Model uiModel) {
        return "books/findBooksByIsbnEquals";
    }

	@RequestMapping(params = "find=ByIsbnEquals", method = RequestMethod.GET)
    public String findBooksByIsbnEquals(@RequestParam("isbn") String isbn, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("books", Book.findBooksByIsbnEquals(isbn, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Book.countFindBooksByIsbnEquals(isbn) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("books", Book.findBooksByIsbnEquals(isbn, sortFieldName, sortOrder).getResultList());
        }
        return "books/list";
    }

	@RequestMapping(params = { "find=ByTitleEquals", "form" }, method = RequestMethod.GET)
    public String findBooksByTitleEqualsForm(Model uiModel) {
        return "books/findBooksByTitleEquals";
    }

	@RequestMapping(params = "find=ByTitleEquals", method = RequestMethod.GET)
    public String findBooksByTitleEquals(@RequestParam("title") String title, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("books", Book.findBooksByTitleEquals(title, sortFieldName, sortOrder).setFirstResult(firstResult).setMaxResults(sizeNo).getResultList());
            float nrOfPages = (float) Book.countFindBooksByTitleEquals(title) / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("books", Book.findBooksByTitleEquals(title, sortFieldName, sortOrder).getResultList());
        }
        return "books/list";
    }

	@RequestMapping(params = "form", produces = "text/html")
    public String createForm(Model uiModel) {
        populateEditForm(uiModel, new Book());
        return "books/create";
    }

	@RequestMapping(value = "/{id}", produces = "text/html")
    public String show(@PathVariable("id") Long id, Model uiModel) {
        uiModel.addAttribute("book", Book.findBook(id));
        uiModel.addAttribute("itemId", id);
        return "books/show";
    }

	@RequestMapping(produces = "text/html")
    public String list(@RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, @RequestParam(value = "sortFieldName", required = false) String sortFieldName, @RequestParam(value = "sortOrder", required = false) String sortOrder, Model uiModel) {
        if (page != null || size != null) {
            int sizeNo = size == null ? 10 : size.intValue();
            final int firstResult = page == null ? 0 : (page.intValue() - 1) * sizeNo;
            uiModel.addAttribute("books", Book.findBookEntries(firstResult, sizeNo, sortFieldName, sortOrder));
            float nrOfPages = (float) Book.countBooks() / sizeNo;
            uiModel.addAttribute("maxPages", (int) ((nrOfPages > (int) nrOfPages || nrOfPages == 0.0) ? nrOfPages + 1 : nrOfPages));
        } else {
            uiModel.addAttribute("books", Book.findAllBooks(sortFieldName, sortOrder));
        }
        return "books/list";
    }

	@RequestMapping(value = "/{id}", params = "form", produces = "text/html")
    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
        populateEditForm(uiModel, Book.findBook(id));
        return "books/update";
    }

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "text/html")
    public String delete(@PathVariable("id") Long id, @RequestParam(value = "page", required = false) Integer page, @RequestParam(value = "size", required = false) Integer size, Model uiModel) {
        Book book = Book.findBook(id);
        book.remove();
        uiModel.asMap().clear();
        uiModel.addAttribute("page", (page == null) ? "1" : page.toString());
        uiModel.addAttribute("size", (size == null) ? "10" : size.toString());
        return "redirect:/books";
    }

	void populateEditForm(Model uiModel, Book book) {
        uiModel.addAttribute("book", book);
        uiModel.addAttribute("authors", Author.findAllAuthors());
        uiModel.addAttribute("categorys", Category.findAllCategorys());
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
