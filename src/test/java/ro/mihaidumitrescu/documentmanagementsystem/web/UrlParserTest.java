package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.Assert;
import org.junit.Test;

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

    @Test
     public void testFindDocumentNamePathParameter_emptyLink() {
        Assert.assertEquals("Empty link", "", urlParser.findDocumentNamePathParameter(""));
    }

    @Test
    public void testFindDocumentNamePathParameter_slash() {
        Assert.assertEquals("Empty link", "",  urlParser.findDocumentNamePathParameter("/"));
    }

    @Test
    public void testFindDocumentNamePathParameter_invalidPath_1() {
        Assert.assertEquals("Invalid path does not detect name", "",  urlParser.findDocumentNamePathParameter("/some/stupid/name"));
    }

    @Test
    public void testFindDocumentNamePathParameter_invalidPath_2() {
        Assert.assertEquals("Invalid path does not detect name", "",  urlParser.findDocumentNamePathParameter("/some/bigger/stupid/name"));
    }

    @Test
    public void testFindDocumentNamePathParameter_configuredOnFullPath() {
        Assert.assertEquals("When configured at root (i.e. mapping URL does not get copied), first atom is documentName", "someName",  urlParser.findDocumentNamePathParameter("someName"));
    }

    @Test
    public void testFindDocumentNamePathParameter_configuredOnFullPathWithSlash() {
        Assert.assertEquals("When configured at root (i.e. mapping URL does not get copied), first atom is documentName", "someName",  urlParser.findDocumentNamePathParameter("/someName"));
    }

    @Test
    public void testFindDocumentNamePathParameter_configuredOnFullPathWithSlashAtEnd() {
        Assert.assertEquals("When configured at root (i.e. mapping URL does not get copied), first atom is documentName", "someName",  urlParser.findDocumentNamePathParameter("someName/"));
    }

    @Test
    public void testFindDocumentNamePathParameter_configuredOnFullPathWithSlashAtBeginningAndEnd() {
        Assert.assertEquals("When configured at root (i.e. mapping URL does not get copied), first atom is documentName", "someName",  urlParser.findDocumentNamePathParameter("/someName/"));
    }

    @Test
    public void testFindDocumentNamePathParameter_fullPath() {
        Assert.assertEquals("Full path ed documentName", "someName",  urlParser.findDocumentNamePathParameter(UrlParser.storagePath + "/someName"));
    }


    @Test
    public void testFindDocumentNamePathParameter_fullPathWithSlashAtEnd() {
        Assert.assertEquals("Full path ed documentName", "someName",  urlParser.findDocumentNamePathParameter(UrlParser.storagePath + "/someName/"));
    }
}