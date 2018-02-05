package com.s305089.bookstore;
import java.util.Iterator;
import java.util.List;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.roo.addon.test.RooIntegrationTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@Configurable
@RooIntegrationTest(entity = BookOrder.class)
public class BookOrderIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    BookOrderDataOnDemand dod;

	@Test
    public void testCountBookOrders() {
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", dod.getRandomBookOrder());
        long count = BookOrder.countBookOrders();
        Assert.assertTrue("Counter for 'BookOrder' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindBookOrder() {
        BookOrder obj = dod.getRandomBookOrder();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to provide an identifier", id);
        obj = BookOrder.findBookOrder(id);
        Assert.assertNotNull("Find method for 'BookOrder' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'BookOrder' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllBookOrders() {
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", dod.getRandomBookOrder());
        long count = BookOrder.countBookOrders();
        Assert.assertTrue("Too expensive to perform a find all test for 'BookOrder', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<BookOrder> result = BookOrder.findAllBookOrders();
        Assert.assertNotNull("Find all method for 'BookOrder' illegally returned null", result);
        Assert.assertTrue("Find all method for 'BookOrder' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindBookOrderEntries() {
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", dod.getRandomBookOrder());
        long count = BookOrder.countBookOrders();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<BookOrder> result = BookOrder.findBookOrderEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'BookOrder' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'BookOrder' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        BookOrder obj = dod.getRandomBookOrder();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to provide an identifier", id);
        obj = BookOrder.findBookOrder(id);
        Assert.assertNotNull("Find method for 'BookOrder' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyBookOrder(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'BookOrder' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        BookOrder obj = dod.getRandomBookOrder();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to provide an identifier", id);
        obj = BookOrder.findBookOrder(id);
        boolean modified =  dod.modifyBookOrder(obj);
        Integer currentVersion = obj.getVersion();
        BookOrder merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'BookOrder' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", dod.getRandomBookOrder());
        BookOrder obj = dod.getNewTransientBookOrder(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'BookOrder' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'BookOrder' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        BookOrder obj = dod.getRandomBookOrder();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'BookOrder' failed to provide an identifier", id);
        obj = BookOrder.findBookOrder(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'BookOrder' with identifier '" + id + "'", BookOrder.findBookOrder(id));
    }
}
