package com.example.npod;
//Constructor class to store fetched details
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Post {
    private String date;
    private  String explanation;
    private String hdurl;
    private String media_type;
    private  String serviceVersion;
    private String title;
    private String url;

    @SerializedName("body")
    private List<Post> text;

    public String getDate() {
        return date;
    }

    public String getExplanation() {
        return explanation;
    }

    public String getHdurl() {
        return hdurl;
    }

    public String getMedia_type() {
        return media_type;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public List<Post> getText() {
        return text;
    }
}
