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
@RooDataOnDemand(entity = Category.class)
public class CategoryDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Category> data;

	public Category getNewTransientCategory(int index) {
        Category obj = new Category();
        setName(obj, index);
        return obj;
    }

	public void setName(Category obj, int index) {
        String Name = "Name_" + index;
        obj.setName(Name);
    }

	public Category getSpecificCategory(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Category obj = data.get(index);
        Long id = obj.getId();
        return Category.findCategory(id);
    }

	public Category getRandomCategory() {
        init();
        Category obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Category.findCategory(id);
    }

	public boolean modifyCategory(Category obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Category.findCategoryEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Category' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Category>();
        for (int i = 0; i < 10; i++) {
            Category obj = getNewTransientCategory(i);
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
