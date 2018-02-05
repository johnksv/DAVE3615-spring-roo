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

@Configurable
@Component
@RooDataOnDemand(entity = Shipping.class)
public class ShippingDataOnDemand {

	private Random rnd = new SecureRandom();

	private List<Shipping> data;

	public Shipping getNewTransientShipping(int index) {
        Shipping obj = new Shipping();
        setAddress(obj, index);
        setName(obj, index);
        setPostalCode(obj, index);
        return obj;
    }

	public void setAddress(Shipping obj, int index) {
        String Address = "Address_" + index;
        obj.setAddress(Address);
    }

	public void setName(Shipping obj, int index) {
        String Name = "Name_" + index;
        obj.setName(Name);
    }

	public void setPostalCode(Shipping obj, int index) {
        int postalCode = index;
        obj.setPostalCode(postalCode);
    }

	public Shipping getSpecificShipping(int index) {
        init();
        if (index < 0) {
            index = 0;
        }
        if (index > (data.size() - 1)) {
            index = data.size() - 1;
        }
        Shipping obj = data.get(index);
        Long id = obj.getId();
        return Shipping.findShipping(id);
    }

	public Shipping getRandomShipping() {
        init();
        Shipping obj = data.get(rnd.nextInt(data.size()));
        Long id = obj.getId();
        return Shipping.findShipping(id);
    }

	public boolean modifyShipping(Shipping obj) {
        return false;
    }

	public void init() {
        int from = 0;
        int to = 10;
        data = Shipping.findShippingEntries(from, to);
        if (data == null) {
            throw new IllegalStateException("Find entries implementation for 'Shipping' illegally returned null");
        }
        if (!data.isEmpty()) {
            return;
        }
        
        data = new ArrayList<Shipping>();
        for (int i = 0; i < 10; i++) {
            Shipping obj = getNewTransientShipping(i);
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
