package ro.mihaidumitrescu.documentmanagementsystem.repository;

import ro.mihaidumitrescu.documentmanagementsystem.model.Content;
import ro.mihaidumitrescu.documentmanagementsystem.model.Document;
import ro.mihaidumitrescu.general.IdGenerator;
import ro.mihaidumitrescu.general.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mihai Dumitrescu on 11.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public enum DocumentsRepository implements Repository<Document> {

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
    public Document createDocument(Content content) {
        IdGenerator idGenerator = IdGenerator.INSTANCE;
        Document created;

        created = appendDocumentToRepository(content, idGenerator);
        return created;
    }

    private Document appendDocumentToRepository(Content content, IdGenerator idGenerator) {
        Document created;
        synchronized (internalRepository) {
            String documentId = idGenerator.next(internalRepository.keySet());
            created = new Document(documentId, content);
            internalRepository.put(documentId, created);
        }
        return created;
    }

    @Override
    public void delete(Document item) {
        if(item == null) {
            return;
        }
        delete(item.getName());
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
    public void delete(String documentName) {
         if(!StringUtils.hasText(documentName)) {
             return;
         }
        internalRepository.remove(documentName);
    }
}
