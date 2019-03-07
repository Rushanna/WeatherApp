package sakhapova.rushanna.com.myweatherapp.forecast.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import sakhapova.rushanna.com.myweatherapp.forecast.WeatherData;

public interface ApiInterface {

    @GET("forecast.json")
    Call<WeatherData> getForecast(
            @Query("key") String apiKey,
            @Query("q") String query,
            @Query("days") int days
    );

}
