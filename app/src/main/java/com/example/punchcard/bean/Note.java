package com.example.punchcard.bean;

public class Note {

    private String id;
    private String punchcard_id;
    private String content;
    private String time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPunchcard_id() {
        return punchcard_id;
    }

    public void setPunchcard_id(String punchcard_id) {
        this.punchcard_id = punchcard_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
