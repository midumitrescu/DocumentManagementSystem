package ro.mihaidumitrescu.general;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testHasText_empty() {
        Assert.assertFalse(StringUtils.hasText(""));
    }

    @Test
    public void testHasText_null() {
        Assert.assertFalse(StringUtils.hasText(null));
    }

    @Test
    public void testHasText_justSpaces() {
        Assert.assertFalse(StringUtils.hasText("   "));
    }

    @Test
    public void testHasText_singleLetter() {
        Assert.assertTrue(StringUtils.hasText("a"));
    }

    @Test
    public void testHasText_singleLetterWithSpaces() {
        Assert.assertTrue(StringUtils.hasText("   a   "));
    }
}