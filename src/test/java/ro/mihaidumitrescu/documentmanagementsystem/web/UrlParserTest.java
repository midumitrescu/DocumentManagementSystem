package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class UrlParserTest {

    private final UrlParser urlParser = new UrlParser();

    @Test
    public void testHasDeepPath_1() {
        Assert.assertTrue(urlParser.hasDeepPath("/somelink"));
    }

    @Test
     public void testHasDeepPath_2() {
        Assert.assertTrue(urlParser.hasDeepPath(UrlParser.storagePath + "/somelink"));
    }

    @Test
    public void testHasDeepPath_3() {
        Assert.assertTrue(urlParser.hasDeepPath(UrlParser.storagePath + "/somelink/followed/by/some/more"));
    }

    @Test
     public void testHasDeepPath_4() {
        Assert.assertTrue(urlParser.hasDeepPath("a"));
    }

    @Test
    public void testHasDeepPath_5() {
        Assert.assertTrue(urlParser.hasDeepPath("/a"));
    }

    @Test
    public void testHasDeepPath_emptyUrl() {
        Assert.assertFalse(urlParser.hasDeepPath(""));
    }

    @Test
    public void testHasDeepPath_Slash() {
        Assert.assertFalse(urlParser.hasDeepPath("/"));
    }

    @Test
    public void testHasDeepPath_StorageUrl() {
        Assert.assertFalse(urlParser.hasDeepPath(UrlParser.storagePath));
    }

    @Test
    public void testHasDeepPath_WithSlash() {
        Assert.assertFalse(urlParser.hasDeepPath(UrlParser.storagePath + "/"));
    }
}