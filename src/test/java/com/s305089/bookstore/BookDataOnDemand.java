package com.s305089.bookstore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = Book.class)
public class BookDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Book> data;

	public Book getNewTransientBook(int index) {
        Book obj = new Book();
        setAuthor(obj, index);
        setContentType(obj, index);
        setCost(obj, index);
        setImage(obj, index);
        setIsbn(obj, index);
        setQuantity(obj, index);
        setTimeFactor(obj, index);
        setTitle(obj, index);
        return obj;
    }

	public void setAuthor(Book obj, int index) {
        Author author = null;
        obj.setAuthor(author);
    }

	public void setContentType(Book obj, int index) {
        String contentType = "contentType_" + index;
        obj.setContentType(contentType);
    }

	public void setCost(Book obj, int index) {
        float cost = new Integer(index).floatValue();
        obj.setCost(cost);
    }

	public void setImage(Book obj, int index) {
        byte[] image = String.valueOf(index).getBytes();
        obj.setImage(image);
    }

	public void setIsbn(Book obj, int index) {
        String isbn = "isbn_" + index;
        obj.setIsbn(isbn);
    }

	public void setQuantity(Book obj, int index) {
        int quantity = index;
        if (quantity < 0) {
            quantity = 0;
        }
        obj.setQuantity(quantity);
    }

	public void setTimeFactor(Book obj, int index) {
        Calendar timeFactor = Calendar.getInstance();
        obj.setTimeFactor(timeFactor);
    }

	public void setTitle(Book obj, int index) {
        String title = "title_" + index;
        obj.setTitle(title);
    }

	public Book getSpecificBook(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Book obj = data.get(index);
        Long id = obj.getId();
        return Book.findBook(id);
    }

	public Book getRandomBook() {
        init();
        Book obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Book.findBook(id);
    }

	public boolean modifyBook(Book obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Book.findBookEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Book' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Book>();
        for (int i = 0; i < 10; i++) {
            Book obj = getNewTransientBook(i);
            try {
                obj.persist();
            } catch (final ConstraintViolationException e) {
                final StringBuilder msg = new StringBuilder();
                for (Iterator<ConstraintViolation<?>> iter = e.getConstraintViolations().iterator(); iter.hasNext();) {
                    final ConstraintViolation<?> cv = iter.next();
                    msg.append("[").append(cv.getRootBean().getClass().getName()).append(".").append(cv.getPropertyPath()).append(": ").append(cv.getMessage()).append(" (invalid value = ").append(cv.getInvalidValue()).append(")").append("]");
                }
                throw new IllegalStateException(msg.toString(), e);
            }
            obj.flush();
            data.add(obj);
        }
    }
}
