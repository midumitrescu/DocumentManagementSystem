package ro.mihaidumitrescu.documentmanagementsystem.web;

import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.*;

public class SupportedMediaTypesTest {

    @Test
    public void testFromHeaderValue() {

        Assert.assertEquals(SupportedMediaTypes.Text, SupportedMediaTypes.fromHeaderValue(MediaType.TEXT_PLAIN));
        Assert.assertEquals(SupportedMediaTypes.Binary, SupportedMediaTypes.fromHeaderValue(MediaType.APPLICATION_OCTET_STREAM));
        Assert.assertEquals("Default value should be binary", SupportedMediaTypes.Binary, SupportedMediaTypes.fromHeaderValue(MediaType.APPLICATION_JSON));

    }
}