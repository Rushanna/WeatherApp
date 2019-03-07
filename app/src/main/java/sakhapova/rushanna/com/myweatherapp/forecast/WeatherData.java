package sakhapova.rushanna.com.myweatherapp.forecast;

import com.google.gson.annotations.SerializedName;

public class WeatherData {

    @SerializedName("location")
    private LocationData location;

    @SerializedName("current")
    private HourlyForecast currentData;

    @SerializedName("forecast")
    private Forecast forecast;


    public HourlyForecast getCurrentData() {
        return currentData;
    }

    public Forecast getForecast() {
        return forecast;
    }

    public LocationData getLocation(){
        return location;
    }
}

