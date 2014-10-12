package ro.mihaidumitrescu.documentmanagementsystem.repository;

import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;

public interface Repository<T extends Document> {

    boolean exists(T item);
    T create(Content content);
    Document delete(T item);
    void update(T item);

    boolean exists(String documentName);
    T read(String documentName);
    Document delete(String documentName);
}
