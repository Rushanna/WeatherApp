package sakhapova.rushanna.com.myweatherapp.forecast;

import com.google.gson.annotations.SerializedName;

public class Condition {

    private String text;

    @SerializedName("icon")
    private String iconUrl;


    public String getText() {
        return text;
    }

    public String getIconUrl() {
        return iconUrl.replace("//", "https://");
    }
}
