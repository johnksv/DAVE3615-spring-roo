package com.s305089.bookstore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Component
@Configurable
@RooDataOnDemand(entity = Author.class)
public class AuthorDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Author> data;

	public Author getNewTransientAuthor(int index) {
        Author obj = new Author();
        setAge(obj, index);
        setName(obj, index);
        setRatings(obj, index);
        return obj;
    }

	public void setAge(Author obj, int index) {
        int age = index;
        if (age < 0) {
            age = 0;
        }
        obj.setAge(age);
    }

	public void setName(Author obj, int index) {
        String name = "name_" + index;
        obj.setName(name);
    }

	public void setRatings(Author obj, int index) {
        double ratings = new Integer(index).doubleValue();
        obj.setRatings(ratings);
    }

	public Author getSpecificAuthor(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Author obj = data.get(index);
        Long id = obj.getId();
        return Author.findAuthor(id);
    }

	public Author getRandomAuthor() {
        init();
        Author obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Author.findAuthor(id);
    }

	public boolean modifyAuthor(Author obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Author.findAuthorEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Author' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Author>();
        for (int i = 0; i < 10; i++) {
            Author obj = getNewTransientAuthor(i);
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
