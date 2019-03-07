package sakhapova.rushanna.com.myweatherapp.forecast;

import com.google.gson.annotations.SerializedName;

public class LocationData {

    @SerializedName("name")
    private String name;

    @SerializedName("country")
    private String country;

    public LocationData(){

    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}

