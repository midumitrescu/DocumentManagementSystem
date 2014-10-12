package ro.mihaidumitrescu.documentmanagementsystem.model;

/**
 * Created by Mihai Dumitrescu on 12.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public class StringContent implements Content<String> {

    private final String content;

    public StringContent(String content) {
        this.content = content;
    }

    @Override
    public String getContent() {
        return content;
    }
}
