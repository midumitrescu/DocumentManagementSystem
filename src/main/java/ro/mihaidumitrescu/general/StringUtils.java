package ro.mihaidumitrescu.general;

public class StringUtils {

    public static boolean hasText(String string) {
      return string != null && string.trim().length() > 0;
    }
}
