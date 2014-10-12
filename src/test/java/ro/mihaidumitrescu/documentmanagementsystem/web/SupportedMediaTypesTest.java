package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

public class SupportedMediaTypesTest {

    @Test
    public void testFromHeaderValue() {

        Assert.assertEquals(SupportedMediaTypes.TEXT, SupportedMediaTypes.fromHeaderValue(MediaType.TEXT_PLAIN));
        Assert.assertEquals(SupportedMediaTypes.BINARY, SupportedMediaTypes.fromHeaderValue(MediaType.APPLICATION_OCTET_STREAM));
        Assert.assertEquals("Default value should be binary", SupportedMediaTypes.BINARY, SupportedMediaTypes.fromHeaderValue(MediaType.APPLICATION_JSON));

    }
}