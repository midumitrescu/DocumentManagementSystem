package ro.mihaidumitrescu.documentmanagementsystem.model;

import java.io.BufferedInputStream;
import java.io.InputStream;

public abstract class AbstractContent<T> implements Content<T> {

    protected InputStream streamContent() {
        return new BufferedInputStream(getContent());
    }

    protected abstract InputStream getContent();
}
