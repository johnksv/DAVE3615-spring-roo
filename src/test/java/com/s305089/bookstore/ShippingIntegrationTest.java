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

@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath*:/META-INF/spring/applicationContext*.xml")
@Transactional
@RooIntegrationTest(entity = Shipping.class)
public class ShippingIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    ShippingDataOnDemand dod;

	@Test
    public void testCountShippings() {
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", dod.getRandomShipping());
        long count = Shipping.countShippings();
        Assert.assertTrue("Counter for 'Shipping' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindShipping() {
        Shipping obj = dod.getRandomShipping();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to provide an identifier", id);
        obj = Shipping.findShipping(id);
        Assert.assertNotNull("Find method for 'Shipping' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Shipping' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllShippings() {
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", dod.getRandomShipping());
        long count = Shipping.countShippings();
        Assert.assertTrue("Too expensive to perform a find all test for 'Shipping', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Shipping> result = Shipping.findAllShippings();
        Assert.assertNotNull("Find all method for 'Shipping' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Shipping' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindShippingEntries() {
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", dod.getRandomShipping());
        long count = Shipping.countShippings();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Shipping> result = Shipping.findShippingEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Shipping' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Shipping' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Shipping obj = dod.getRandomShipping();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to provide an identifier", id);
        obj = Shipping.findShipping(id);
        Assert.assertNotNull("Find method for 'Shipping' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyShipping(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Shipping' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Shipping obj = dod.getRandomShipping();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to provide an identifier", id);
        obj = Shipping.findShipping(id);
        boolean modified =  dod.modifyShipping(obj);
        Integer currentVersion = obj.getVersion();
        Shipping merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Shipping' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", dod.getRandomShipping());
        Shipping obj = dod.getNewTransientShipping(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Shipping' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Shipping' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Shipping' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Shipping obj = dod.getRandomShipping();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Shipping' failed to provide an identifier", id);
        obj = Shipping.findShipping(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Shipping' with identifier '" + id + "'", Shipping.findShipping(id));
    }
}
