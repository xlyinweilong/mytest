package alading;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import org.htmlparser.util.ParserException;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 * 蜘蛛
 *
 * @author yin.weilong
 */
public class Spider {

    //URL入口
    String startUrl = "";
    //已经分析获得URL链接
    public static Set<String> searchedUrlList = new HashSet();
    //需解析的链接列表
    Queue linklist = new LinkedList();

    public Spider(String startUrl) {
        this.startUrl = startUrl;
        linklist.add(startUrl);
        search(linklist);
        System.out.println(searchedUrlList.size());
        File file = new File("d:/123");
        try {
            FileUtils.writeLines(file, searchedUrlList);
        } catch (IOException ex) {
            Logger.getLogger(Spider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void search(Queue queue) {
        String url = "";
        while (!queue.isEmpty()) {
            url = queue.peek().toString();
            try {
                //判断是否已经分析过
                //分析页面
                processHtml(url);
            } catch (Exception ex) {
            }
            queue.remove();
        }
    }

    /**
     * 解析HTML
     *
     * @param url
     * @throws ParserException
     * @throws Exception
     */
    public void processHtml(String url) throws ParserException, Exception {
//        System.out.println(searchedUrlList.size());
        if (searchedUrlList.size() < 10000) {
            linklist.addAll(ClientTools.findLinkFromString(ClientTools.getHtml(url)));
        }
    }

    /**
     * 检查该链接是否已经被扫描
     *
     * @param set
     * @param url
     * @return
     */
    public boolean isSearched(Set set, String url) {
        return set.contains(url);
    }

    public static void main(String[] args) {
        Spider ph = new Spider("http://changchun.fang.com/");
    }
}
