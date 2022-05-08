package com.example.vizora;

public class NewsThing {
    private String name;
    private String date;
    private String info;

    public NewsThing(String name, String date, String info) {
        this.name = name;
        this.date = date;
        this.info = info;
    }

    public String getName() {return name;}
    public String getDate() {return date;}
    public String getInfo() {return info;}
}
