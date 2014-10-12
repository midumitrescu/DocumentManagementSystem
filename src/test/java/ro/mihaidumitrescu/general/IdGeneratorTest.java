package ro.mihaidumitrescu.general;

import org.junit.Assert;
import org.junit.Test;

import java.util.Set;
import java.util.TreeSet;

import static ro.mihaidumitrescu.application.ApplicationSettings.DOCUMENT_NAME__DEFAULT_LENGTH;

public class IdGeneratorTest {

    @Test
    public void testNextId_size20() {
        Assert.assertEquals( DOCUMENT_NAME__DEFAULT_LENGTH + " chars longs random", DOCUMENT_NAME__DEFAULT_LENGTH, IdGenerator.INSTANCE.next().length());
    }

    @Test
    public void testNextId_avoidCollision() {
        Set<String> uniqueValues = new TreeSet<String>();
        for(int i = 0; i < 1000; i++) {
            uniqueValues.add(IdGenerator.INSTANCE.next());
        }
        Assert.assertEquals("Create 1000 unique values", 1000, uniqueValues.size());
    }
}