package ro.mihaidumitrescu.general;

import javafx.application.Application;
import org.junit.Assert;
import org.junit.Test;
import ro.mihaidumitrescu.application.ApplicationSettings;

import java.util.Random;

import static org.junit.Assert.*;
import static ro.mihaidumitrescu.application.ApplicationSettings.documentNameLength;

public class IdGeneratorTest {

    @Test
    public void testNextId_size20() {
        Assert.assertEquals( documentNameLength + " chars longs random", documentNameLength, IdGenerator.INSTANCE.next().length());
    }
}