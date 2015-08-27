package alading;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author Administrator
 */
public class ClientTools {

    public static CloseableHttpClient httpclient = HttpClients.createDefault();

    public static List<String> nostartList = new ArrayList<>();

    public static List<String> searchedList = new ArrayList<>();

    public static String[] nostarHead = {"client.", "home.", "esf.", "zu.", "fangjia.", "news.", "bbs.", "world.", "fdc.", "wap.", "shop.", "gongzhang.", "office."};

    public static String[] nostarContains = {"bbs", "vip", "feedback", "ask", "photo"};

    public static String[] starContains = {"fang.com"};

    static {
        nostartList.add("www.zufang.com");
        nostartList.add("m.fang.com");
    }

    public static String getHtml(String url) throws Exception {
        HttpGet httpget = new HttpGet(url);
        ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(
                    final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };
        String responseBody = httpclient.execute(httpget, responseHandler);
        return responseBody.trim();
    }

    public static Set<String> findLinkFromString(String body) {
        Set<String> set = new HashSet<>();
        String[] hrefs = body.split("href");
        for (int i = 1; i < hrefs.length; i++) {
            try {
                String link = hrefs[i].substring(hrefs[i].indexOf("\"") + 1, hrefs[i].indexOf("\"", 2)).trim();
                if (!link.endsWith("/")) {
                    link += "/";
                }
                if (!link.isEmpty() && !"#".equals(link)) {
                    boolean breakFlag = false;
                    for (String start : starContains) {
                        if (!link.contains(start)) {
                            breakFlag = true;
                            break;
                        }
                    }
                    if (breakFlag) {
                        continue;
                    }
                    for (String start : nostarContains) {
                        if (link.contains(start)) {
                            breakFlag = true;
                            break;
                        }
                    }
                    if (breakFlag) {
                        continue;
                    }
                    String linkBase = link.split("/")[2];
//                    System.out.println(linkBase);
                    for (String head : nostarHead) {
                        if (linkBase.startsWith(head)) {
                            breakFlag = true;
                            break;
                        }
                    }
                    if (breakFlag) {
                        continue;
                    }
                    if (!nostartList.contains(linkBase)) {
                        if (Spider.searchedUrlList.contains("http://" + linkBase)) {
                            continue;
                        }
                        Spider.searchedUrlList.add("http://" + linkBase);
//                        System.out.println("http://" + linkBase);
                        set.add("http://" + linkBase);
                    }
                }
            } catch (Exception e) {
            }
        }
        return set;
    }

    public static void main(String args[]) throws Exception {
        ClientTools.findLinkFromString(ClientTools.getHtml("http://changchun.fang.com/"));
    }

}
