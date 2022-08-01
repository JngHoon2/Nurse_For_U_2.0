package com.nfu2.Main;

public class Notification_Adapter{
    private String title;
    private String content;
    private String date;
    private String writer;

    public Notification_Adapter(){}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getWriter() { return writer; }
    public void setWriter(String writer) { this.writer = writer; }

    public Notification_Adapter(String title, String content, String date, String writer) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.writer = writer;
    }

    public void Notification_Adapter(String title, String date, String writer) {
        this.title = title;
        this.date = date;
        this.writer = writer;
    }
}
