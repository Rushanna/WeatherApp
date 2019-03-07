package sakhapova.rushanna.com.myweatherapp.forecast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

    @SerializedName("forecastday")
    private List<DailyForecastContainer> forecasts;


    public List<DailyForecastContainer> getForecasts() {
        return forecasts;
    }
}
