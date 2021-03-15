package com.efrem.halyot;

public class News {
    private String title;
    private String urlImage;
    private String urlNews;
    private String source;
    public News(){}
    public News(String title, String urlImage, String urlNews, String source) {
        this.title = title;
        this.urlImage = urlImage;
        this.urlNews = urlNews;
        this.source= source;
    }
    public void setTitle(String title) {this.title = title; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }
    public void setUrlNews(String urlNews) { this.urlNews = urlNews; }
    public void setSource(String source) {this.source= source;}
    public String getTitle() { return title; }
    public String getUrlImage() { return urlImage; }
    public String getUrlNews() { return urlNews; }
    public String getSource() { return source;}
}
