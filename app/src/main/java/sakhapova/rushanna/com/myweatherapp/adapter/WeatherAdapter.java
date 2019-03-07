package sakhapova.rushanna.com.myweatherapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import sakhapova.rushanna.com.myweatherapp.R;
import sakhapova.rushanna.com.myweatherapp.forecast.DailyForecast;
import sakhapova.rushanna.com.myweatherapp.forecast.DailyForecastContainer;
import sakhapova.rushanna.com.myweatherapp.forecast.HourlyForecast;
import sakhapova.rushanna.com.myweatherapp.forecast.LocationData;
import sakhapova.rushanna.com.myweatherapp.forecast.WeatherData;

public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final SimpleDateFormat DAY_SDF = new SimpleDateFormat("EEE, MMMM d", Locale.getDefault());

    private static final int VIEW_TYPE_NOW = 1;
    private static final int VIEW_TYPE_DAILY = 2;

    private WeatherData weatherData;

    public synchronized void setWeatherData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == VIEW_TYPE_NOW) {
            View view;
            view = layoutInflater.inflate(R.layout.view_item_current, parent, false);

            return new CurrentViewHolder(view);
        } else {
            View view = layoutInflater.inflate(R.layout.view_item_day, parent, false);

            return new DayViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder( RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);

        Context context;
        context = holder.itemView.getContext();

        if (viewType == VIEW_TYPE_NOW) {
            CurrentViewHolder currentViewHolder = (CurrentViewHolder) holder;

            HourlyForecast currentData = weatherData.getCurrentData();
            LocationData locationData = weatherData.getLocation();

            String temperature = context.getString(R.string.temperature_format, (int) currentData.getTemperature());
            String feelsLike = context.getString(R.string.temperature_feels_like_format, (int) currentData.getTemperatureFeelsLike());
            String nameTv = (locationData.getCountry() + ", " + locationData.getName());

            currentViewHolder.temperatureTv.setText(temperature);
            currentViewHolder.feelsLikeTv.setText(feelsLike);
            currentViewHolder.conditionTv.setText(currentData.getCondition().getText());
            currentViewHolder.nameTv.setText(nameTv);
            currentViewHolder.dataAndTimeTv.setText(DAY_SDF.format(new Date()));

            Picasso.with(context).load(currentData.getCondition().getIconUrl()).into(currentViewHolder.conditionIv);
        } else {
            DayViewHolder dayViewHolder = (DayViewHolder) holder;

            DailyForecastContainer dailyForecastContainer = weatherData.getForecast().getForecasts().get(position - 1);
            DailyForecast dailyForecast = dailyForecastContainer.getDailyForecast();

            dayViewHolder.dateTv.setText(DAY_SDF.format(dailyForecastContainer.getDate()));
            String temperature = context.getString(R.string.temperature_format, (int) dailyForecast.getAverageTemperature());
            dayViewHolder.temperatureTv.setText(temperature);

            Picasso.with(context).load(dailyForecast.getCondition().getIconUrl()).into(dayViewHolder.conditionIv);
        }
    }

    @Override
    public int getItemCount() {
        if (weatherData == null) {
            return 0;
        } else {
            return weatherData.getForecast().getForecasts().size() + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_NOW;

            default:
                return VIEW_TYPE_DAILY;
        }
    }

    class CurrentViewHolder extends RecyclerView.ViewHolder {

        private final TextView conditionTv;
        private final ImageView conditionIv;
        private final TextView feelsLikeTv;
        private final TextView temperatureTv;
        private final TextView nameTv;
        private final TextView dataAndTimeTv;


        public CurrentViewHolder(View itemView) {
            super(itemView);

            conditionTv = itemView.findViewById(R.id.condition_tv);
            conditionIv = itemView.findViewById(R.id.condition_iv);
            feelsLikeTv = itemView.findViewById(R.id.feels_like_tv);
            temperatureTv = itemView.findViewById(R.id.temperature_tv);
            nameTv = itemView.findViewById(R.id.city_country_tv);
            dataAndTimeTv = itemView.findViewById(R.id.data_time_tv);

        }
    }

    class DayViewHolder extends RecyclerView.ViewHolder {

        private final TextView dateTv;
        private final ImageView conditionIv;
        private final TextView temperatureTv;

       public DayViewHolder(View itemView) {
            super(itemView);

            dateTv = itemView.findViewById(R.id.date_tv);
            conditionIv = itemView.findViewById(R.id.condition_iv);
            temperatureTv = itemView.findViewById(R.id.temperature_tv);
        }

    }
}
