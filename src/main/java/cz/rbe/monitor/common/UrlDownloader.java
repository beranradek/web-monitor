package cz.rbe.monitor.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

/**
 * URL downloader.
 * @author Radek Beran
 */
public class UrlDownloader {

    private static int BUFFER_SIZE = 1024;

    /**
     * Downloads content from given URL.
     * @param url
     * @param connectTimeout connect timeout value in milliseconds
     * @param readTimeout connect timeout value in milliseconds
     */
    public static byte[] downloadBytes(String url, int connectTimeout, int readTimeout) {
        InputStream is = null;
        ByteArrayOutputStream os = null;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            try {
                URLConnection uc = new URL(url).openConnection();
                uc.setConnectTimeout(connectTimeout);
                uc.setReadTimeout(readTimeout);
                is = new BufferedInputStream(uc.getInputStream());
                os = new ByteArrayOutputStream();
                int l;
                while ((l = is.read(buffer)) != -1) {
                    os.write(buffer, 0, l);
                }
            } finally {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.flush();
                    os.close();
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        if (os == null) {
            return new byte[0];
        }
        return os.toByteArray();
    }

    /**
     * Downloads content from given URL.
     * @param url
     * @param connectTimeout connect timeout value in milliseconds
     * @param readTimeout connect timeout value in milliseconds
     */
    public static String downloadString(String url, int connectTimeout, int readTimeout) {
        byte[] bytes = downloadBytes(url, connectTimeout, readTimeout);
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
