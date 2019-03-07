package sakhapova.rushanna.com.myweatherapp.forecast;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class DailyForecastContainer {

    @SerializedName("date_epoch")
    private long dateTs;

    @Nullable
    private transient Date date;

    @SerializedName("day")
    private DailyForecast dailyForecast;



    public synchronized Date getDate() {
        if (date == null) {
            if (dateTs == 0) {
                date = new Date();
            } else {
                date = new Date(dateTs * 1000);
            }
        }

        return date;
    }

    public DailyForecast getDailyForecast() {
        return dailyForecast;
    }

}
