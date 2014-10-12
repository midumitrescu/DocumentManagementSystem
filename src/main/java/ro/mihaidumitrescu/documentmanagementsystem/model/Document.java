package ro.mihaidumitrescu.documentmanagementsystem.model;

/**
 * Created by Mihai Dumitrescu on 11.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public class Document {

    private final String name;
    private final Content content;

    public Document(String name, Content content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public Content getContent() {
        return content;
    }
}
