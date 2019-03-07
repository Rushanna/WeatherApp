package sakhapova.rushanna.com.myweatherapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import sakhapova.rushanna.com.myweatherapp.adapter.WeatherAdapter;
import sakhapova.rushanna.com.myweatherapp.forecast.api.ApiFactory;
import sakhapova.rushanna.com.myweatherapp.forecast.api.ApiInterface;
import sakhapova.rushanna.com.myweatherapp.forecast.WeatherData;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private WeatherAdapter weatherAdapter;
    private LocationManager locationManager;
    private final ApiInterface api = ApiFactory.createApi();
    private SwipeRefreshLayout swipeRefreshLayout;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.weather_rv);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        recyclerView.setLayoutManager(layoutManager);

        weatherAdapter = new WeatherAdapter();
        recyclerView.setAdapter(weatherAdapter);

        swipeRefreshLayout = findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(
                 Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        checkAndRequestGeoPermission();
    }

    private void showMessageOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, REQUEST_CODE_LOCATION_PERMISSION);
    }

    private void checkAndRequestGeoPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        if (permissionCheck != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            setupLocationManager();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PERMISSION_GRANTED) {
                setupLocationManager();
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    showMessageOK(getString(R.string.Message),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                            REQUEST_CODE_LOCATION_PERMISSION);
                                    openApplicationSettings();
                                }
                            });
                } else {
                    checkAndRequestGeoPermission();
                }
            }
        }
    }


    @Override
    protected void onStop() {
        if (locationManager != null) {
            locationManager.removeUpdates(locationListener);
        }

        super.onStop();
    }

    @SuppressLint("MissingPermission")
    private void setupLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (locationManager != null) {
            String provider = locationManager.getBestProvider(new Criteria(), true);

            if (provider != null) {
                loadData(Constants.AUTO_IP_QUERY);
                locationManager.requestLocationUpdates(provider,
                        10000,
                        10,
                        locationListener);
            }
        }
    }

    private void loadData(String query) {
        swipeRefreshLayout.setRefreshing(true);
        api.getForecast(Constants.API_KEY, query, 10).enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    weatherAdapter.setWeatherData(response.body());
                    weatherAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "Unknown error.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getApplicationContext(), "Unable to load data. Check Internet connection.", Toast.LENGTH_LONG).show();
            }
        });
    }


    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            loadData(location.getLatitude() + "," + location.getLongitude());
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };

    @Override
    public void onRefresh() {
        loadData(Constants.AUTO_IP_QUERY);
    }

}

