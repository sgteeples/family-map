package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

/** Responsible for handling requests to the server not directed to a specific API. Note
 * that unlike the other handlers it does not inherit from Handler. That's because it does
 * some fundamentally different tasks like handling files, returning the 404 page, etc. The
 * lack of consistency is a little annoying, but I find it worthwhile for readability
 */
public class DefaultHandler implements HttpHandler {

    private final String webDirPath = System.getProperty("user.home") +
            "/AndroidStudioProjects/family_map/server/web";
    private final String html404Path = webDirPath + "/HTML/404.html";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("get")) {

                String requestPath = exchange.getRequestURI().getPath();

                if (requestPath.equals("/")) {
                    requestPath = "/index.html";
                }

                File desiredFile = new File(webDirPath + requestPath);

                if (!desiredFile.isFile()) {
                    desiredFile = new File(html404Path);
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                } else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                }

                String fileContents = readFileIntoString(desiredFile);
                writeStringToResponseBody(fileContents, exchange);

            } else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
        } catch (IOException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_SERVER_ERROR, 0);
            System.out.println(e.getMessage());
        } finally {
            exchange.getResponseBody().close();
        }
    }

    private String readFileIntoString(File file) throws IOException {
        FileInputStream fs = new FileInputStream(file);
        String fileContents = readString(fs);
        fs.close();
        return fileContents;
    }

    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeStringToResponseBody(String str, HttpExchange exchange) throws IOException {
        OutputStream os = exchange.getResponseBody();
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}