package com.s305089.bookstore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.dod.RooDataOnDemand;
import org.springframework.stereotype.Component;

@Configurable
@Component
@RooDataOnDemand(entity = BookOrder.class)
public class BookOrderDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<BookOrder> data;

	@Autowired
    ShippingDataOnDemand shippingDataOnDemand;

	public BookOrder getNewTransientBookOrder(int index) {
        BookOrder obj = new BookOrder();
        return obj;
    }

	public BookOrder getSpecificBookOrder(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        BookOrder obj = data.get(index);
        Long id = obj.getId();
        return BookOrder.findBookOrder(id);
    }

	public BookOrder getRandomBookOrder() {
        init();
        BookOrder obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return BookOrder.findBookOrder(id);
    }

	public boolean modifyBookOrder(BookOrder obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = BookOrder.findBookOrderEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'BookOrder' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<BookOrder>();
        for (int i = 0; i < 10; i++) {
            BookOrder obj = getNewTransientBookOrder(i);
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
