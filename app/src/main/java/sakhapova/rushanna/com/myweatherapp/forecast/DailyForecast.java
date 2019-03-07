package sakhapova.rushanna.com.myweatherapp.forecast;

import com.google.gson.annotations.SerializedName;

public class DailyForecast {


    @SerializedName("avgtemp_c")
    private float averageTemperature;

    private Condition condition;



    public float getAverageTemperature() {
        return averageTemperature;
    }

    public Condition getCondition() {
        return condition;
    }
}
