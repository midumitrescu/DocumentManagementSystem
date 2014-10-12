package ro.mihaidumitrescu.documentmanagementsystem.repository;

import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;

/**
 * Created by Mihai Dumitrescu on 11.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public interface Repository<T extends Document> {

    boolean exists(T item);
    T createDocument(Content content);
    void delete(T item);

    boolean exists(String documentName);
    T read(String documentName);
    void delete(String documentName);
}
