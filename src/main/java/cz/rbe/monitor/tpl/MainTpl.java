package cz.rbe.monitor.tpl;

/**
 * This application is (should be?) so simple so that contains only one page with hardcoded markup
 * and does not use a template engine.
 * @author Radek Beran
 */
public class MainTpl {

    public static final String t1 = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<meta http-equiv=\"Refresh\" content=\"5\">\n" +
            "<title>Web Monitor</title>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n";

    public static final String t2 =
            "\n" +
            "</body>\n" +
            "\n" +
            "</html>";
}
