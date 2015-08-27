/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alading;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Administrator
 */
public class HtmlParser {

    /**
     * 要分析的网页
     */
    String htmlUrl;
    /**
     * 分析结果
     */
    ArrayList<String> hrefList = new ArrayList();
    /**
     * 网页编码方式
     */
    String charSet;

    public HtmlParser(String htmlUrl) {
        // TODO 自动生成的构造函数存根
        this.htmlUrl = htmlUrl;
    }

    /**
     * 获取分析结果
     *
     * @throws IOException
     */
    public ArrayList<String> getHrefList() throws IOException {
        parser();
        return hrefList;
    }

    /**
     * 解析网页链接
     *
     * @return
     * @throws IOException
     */
    private void parser() throws IOException {
        URL url = new URL(htmlUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setDoOutput(true);
        String contenttype = connection.getContentType();
        charSet = getCharset(contenttype);
        InputStreamReader isr = new InputStreamReader(
                connection.getInputStream(), charSet);
        BufferedReader br = new BufferedReader(isr);
        String str = null, rs = null;
        while ((str = br.readLine()) != null) {
            rs = getHref(str);
            if (rs != null) {
                hrefList.add(rs);
            }
        }
    }

    /**
     * 获取网页编码方式
     *
     * @param str
     */
    private String getCharset(String str) {
        Pattern pattern = Pattern.compile("charset=.*");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0).split("charset=")[1];
        }
        return null;
    }

    /**
     * 从一行字符串中读取链接
     *
     * @return
     */
    private String getHref(String str) {
        Pattern pattern = Pattern.compile("<a href=.*</a>");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return null;
    }

    public static void main(String[] arg) throws IOException {
        HtmlParser a = new HtmlParser("http://changchun.fang.com/");
        ArrayList<String> hrefList = a.getHrefList();
        for (int i = 0; i < hrefList.size(); i++) {
            System.out.println(hrefList.get(i));
        }
    }
}
