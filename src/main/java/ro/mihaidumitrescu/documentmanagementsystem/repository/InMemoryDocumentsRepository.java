package ro.mihaidumitrescu.documentmanagementsystem.repository;

import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;
import ro.mihaidumitrescu.general.IdGenerator;
import ro.mihaidumitrescu.general.StringUtils;

import java.util.Collections;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;

public enum InMemoryDocumentsRepository implements Repository<Document> {

    INSTANCE;
    public static final int initialRepositoryCapacity = 128;

    private final Map<String, Document> internalRepository = synchronizedHashMap();

    private Map<String, Document> synchronizedHashMap() {
        return Collections.synchronizedMap(new HashMap<String, Document>(initialRepositoryCapacity));
    }

    @Override
    public boolean exists(Document item) {
        return item != null && exists(item.getName());
    }

    @Override
    public Document create(Content content) {
        IdGenerator idGenerator = IdGenerator.INSTANCE;
        return appendDocumentToRepository(content, idGenerator);
    }

    private Document appendDocumentToRepository(Content content, IdGenerator idGenerator) {
        Document created;
        synchronized (internalRepository) {
            String documentId = idGenerator.nextFreeRandom(internalRepository.keySet());
            created = new Document(documentId, content);
            internalRepository.put(documentId, created);
        }
        return created;
    }

    @Override
    public Document delete(Document item) {
        if(item == null) {
            return null;
        }
        return delete(item.getName());
    }

    @Override
    public void update(Document item) {
        if(item == null) {
            return;
        }
        synchronized (item.getName()) {
            if(!exists(item)) {
                throw new ConcurrentModificationException("Document " + item + " cannot be updated, as it was deleted previously");
            }
           internalRepository.put(item.getName(), item);
        }
    }

    @Override
    public boolean exists(String documentName) {
        return StringUtils.hasText(documentName) && internalRepository.containsKey(documentName);
    }

    @Override
    public Document read(String documentName) {
        return internalRepository.get(documentName);
    }

    @Override
    public Document delete(String documentName) {
         if(!StringUtils.hasText(documentName)) {
             return null;
         }
        return internalRepository.remove(documentName);
    }
}