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
@RooIntegrationTest(entity = Category.class)
public class CategoryIntegrationTest {

    @Test
    public void testMarkerMethod() {
    }

	@Autowired
    CategoryDataOnDemand dod;

	@Test
    public void testCountCategorys() {
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", dod.getRandomCategory());
        long count = Category.countCategorys();
        Assert.assertTrue("Counter for 'Category' incorrectly reported there were no entries", count > 0);
    }

	@Test
    public void testFindCategory() {
        Category obj = dod.getRandomCategory();
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Category' failed to provide an identifier", id);
        obj = Category.findCategory(id);
        Assert.assertNotNull("Find method for 'Category' illegally returned null for id '" + id + "'", obj);
        Assert.assertEquals("Find method for 'Category' returned the incorrect identifier", id, obj.getId());
    }

	@Test
    public void testFindAllCategorys() {
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", dod.getRandomCategory());
        long count = Category.countCategorys();
        Assert.assertTrue("Too expensive to perform a find all test for 'Category', as there are " + count + " entries; set the findAllMaximum to exceed this value or set findAll=false on the integration test annotation to disable the test", count < 250);
        List<Category> result = Category.findAllCategorys();
        Assert.assertNotNull("Find all method for 'Category' illegally returned null", result);
        Assert.assertTrue("Find all method for 'Category' failed to return any data", result.size() > 0);
    }

	@Test
    public void testFindCategoryEntries() {
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", dod.getRandomCategory());
        long count = Category.countCategorys();
        if (count > 20) count = 20;
        int firstResult = 0;
        int maxResults = (int) count;
        List<Category> result = Category.findCategoryEntries(firstResult, maxResults);
        Assert.assertNotNull("Find entries method for 'Category' illegally returned null", result);
        Assert.assertEquals("Find entries method for 'Category' returned an incorrect number of entries", count, result.size());
    }

	@Test
    public void testFlush() {
        Category obj = dod.getRandomCategory();
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Category' failed to provide an identifier", id);
        obj = Category.findCategory(id);
        Assert.assertNotNull("Find method for 'Category' illegally returned null for id '" + id + "'", obj);
        boolean modified =  dod.modifyCategory(obj);
        Integer currentVersion = obj.getVersion();
        obj.flush();
        Assert.assertTrue("Version for 'Category' failed to increment on flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testMergeUpdate() {
        Category obj = dod.getRandomCategory();
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Category' failed to provide an identifier", id);
        obj = Category.findCategory(id);
        boolean modified =  dod.modifyCategory(obj);
        Integer currentVersion = obj.getVersion();
        Category merged = obj.merge();
        obj.flush();
        Assert.assertEquals("Identifier of merged object not the same as identifier of original object", merged.getId(), id);
        Assert.assertTrue("Version for 'Category' failed to increment on merge and flush directive", (currentVersion != null && obj.getVersion() > currentVersion) || !modified);
    }

	@Test
    public void testPersist() {
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", dod.getRandomCategory());
        Category obj = dod.getNewTransientCategory(Integer.MAX_VALUE);
        Assert.assertNotNull("Data on demand for 'Category' failed to provide a new transient entity", obj);
        Assert.assertNull("Expected 'Category' identifier to be null", obj.getId());
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
        Assert.assertNotNull("Expected 'Category' identifier to no longer be null", obj.getId());
    }

	@Test
    public void testRemove() {
        Category obj = dod.getRandomCategory();
        Assert.assertNotNull("Data on demand for 'Category' failed to initialize correctly", obj);
        Long id = obj.getId();
        Assert.assertNotNull("Data on demand for 'Category' failed to provide an identifier", id);
        obj = Category.findCategory(id);
        obj.remove();
        obj.flush();
        Assert.assertNull("Failed to remove 'Category' with identifier '" + id + "'", Category.findCategory(id));
    }
}
