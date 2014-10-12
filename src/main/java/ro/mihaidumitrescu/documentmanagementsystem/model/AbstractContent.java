package ro.mihaidumitrescu.documentmanagementsystem.model;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Mihai Dumitrescu on 12.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public abstract class AbstractContent<T> implements Content<T> {

    protected InputStream streamContent() {
        return new BufferedInputStream(getContent());
    }

    protected abstract InputStream getContent();
}
