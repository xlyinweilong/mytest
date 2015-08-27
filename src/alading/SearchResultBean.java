/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package alading;

/**
 *
 * @author Administrator
 */
public class SearchResultBean {

    String url = "";
    String title = "";
    String keywords = "";
    int count_key_words = 0;

    public int getCount_key_words() {
        return count_key_words;
    }

    public void setCount_key_words(int count_key_words) {
        this.count_key_words = count_key_words;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
