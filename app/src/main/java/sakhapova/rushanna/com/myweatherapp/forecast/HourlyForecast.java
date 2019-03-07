package sakhapova.rushanna.com.myweatherapp.forecast;

import com.google.gson.annotations.SerializedName;

public class HourlyForecast {


    @SerializedName("temp_c")
    private float temperature;

    @SerializedName("feelslike_c")
    private float temperatureFeelsLike;

    private Condition condition;

    public HourlyForecast() {
    }


    public float getTemperature() {
        return temperature;
    }

    public float getTemperatureFeelsLike() {
        return temperatureFeelsLike;
    }

    public Condition getCondition() {
        return condition;
    }
}
