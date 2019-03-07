package sakhapova.rushanna.com.myweatherapp.forecast.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sakhapova.rushanna.com.myweatherapp.Constants;

public class ApiFactory {

    public static ApiInterface createApi() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(ApiInterface.class);
    }
}
