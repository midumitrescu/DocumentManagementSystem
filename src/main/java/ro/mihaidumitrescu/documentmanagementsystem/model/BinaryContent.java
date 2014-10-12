package ro.mihaidumitrescu.documentmanagementsystem.model;

/**
 * Created by Mihai Dumitrescu on 12.10.2014.
 *
 * @author <a href="mailto:dumitrescu.mihai2002@yahoo.com">Mihai Dumitrescu</a>
 */
public class BinaryContent implements Content<byte[]> {

    private final byte[] content;

    public BinaryContent(byte[] content) {
        this.content = content;
    }

    @Override
    public byte[] getContent() {
        return content;
    }
}
