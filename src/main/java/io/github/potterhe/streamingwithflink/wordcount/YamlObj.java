package io.github.potterhe.streamingwithflink.wordcount;

import java.util.Map;

public class YamlObj {
    private String mykey;

    public void setMyapp(Map<String, String> myapp) {
        this.myapp = myapp;
    }

    private Map<String, String> myapp;

    public String getMykey() {
        return mykey;
    }

    public void setMykey(String s) {
        this.mykey = s;
    }

    public Map<String, String> getMyapp() {
        return myapp;
    }

}
