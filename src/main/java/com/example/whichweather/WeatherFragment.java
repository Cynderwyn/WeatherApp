package com.example.whichweather;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class WeatherFragment extends Fragment {

    Handler handler;

    //Globala deklarationer för weather layout (Main layout)
    private ImageView weatherIcon;
    private TextView cityText;
    private TextView conditionDescription;
    private TextView currentTimeDate;
    private TextView temperature;
    private TextView humidity;
    private TextView pressure;
    private TextView windSpeed;

    //Globala deklarationer för forecast layout
    private ImageView forecastIcon1;
    private ImageView forecastIcon2;
    private ImageView forecastIcon3;
    private TextView forecast1;
    private TextView forecast2;
    private TextView forecast3;
    private TextView forecastTemp1;
    private TextView forecastTemp2;
    private TextView forecastTemp3;

    //Globala deklarationer för de olika layout objekten
    private RelativeLayout mainLayout;
    private RelativeLayout forecastLayout;
    private RelativeLayout startLayout;

    public WeatherFragment() {
        handler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.test_activity, container, false);

        //Layout views
        mainLayout = (RelativeLayout) rootView.findViewById(R.id.mainContent);
        mainLayout.setVisibility(View.GONE);
        forecastLayout = (RelativeLayout) rootView.findViewById(R.id.forecastLayout);
        forecastLayout.setVisibility(View.GONE);
        startLayout = (RelativeLayout) rootView.findViewById(R.id.startLayout);
        startLayout.setVisibility(View.VISIBLE);

        //Current weather
        temperature = (TextView) rootView.findViewById(R.id.temperature);
        humidity = (TextView) rootView.findViewById(R.id.humidity);
        pressure = (TextView) rootView.findViewById(R.id.pressure);
        currentTimeDate = (TextView) rootView.findViewById(R.id.currentTimeDate);
        windSpeed = (TextView) rootView.findViewById(R.id.windSpeed);
        weatherIcon = (ImageView) rootView.findViewById(R.id.weatherIcon);
        cityText = (TextView) rootView.findViewById(R.id.cityText);
        conditionDescription = (TextView) rootView.findViewById(R.id.conditionDescription);

        //Forecast weather
        forecast1 = (TextView) rootView.findViewById(R.id.forecast1);
        forecast2 = (TextView) rootView.findViewById(R.id.forecast2);
        forecast3 = (TextView) rootView.findViewById(R.id.forecast3);
        forecastIcon1 = (ImageView) rootView.findViewById(R.id.forecastIcon1);
        forecastIcon2 = (ImageView) rootView.findViewById(R.id.forecastIcon2);
        forecastIcon3 = (ImageView) rootView.findViewById(R.id.forecastIcon3);
        forecastTemp1 = (TextView) rootView.findViewById(R.id.forecastTemperature1);
        forecastTemp2 = (TextView) rootView.findViewById(R.id.forecastTemperature2);
        forecastTemp3 = (TextView) rootView.findViewById(R.id.forecastTemperature3);

        return rootView;
    }

    //Hämtar väderdata från OpenWeatherMap med angiven stad
    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final String jsonObject = WeatherHttpClient.getWeatherData(getActivity(), city);

                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderWeather(jsonObject);
                        }
                    });
                }
            }
        }.start();
    }

    //Hämtar forecastdata från OpenWeatherMap med angiven stad
    private void updateForecastData(final String city) {
        new Thread() {
            public void run() {
                final String jsonObject = WeatherHttpClient.getForecastData(getActivity(), city);

                if (jsonObject == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    getActivity().getString(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            renderForecast(jsonObject);
                        }
                    });

                }
            }
        }.start();
    }

    //Skriver ut och fyller de olika objekten för det aktuella vädret i min layout
    private void renderWeather(String json) {

        try {
            Weather weather = new Weather();
            weather = JSONWeatherParser.getWeather(json);

            setWeatherIcon(weather.currentCondition.getIcon(), weatherIcon);

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            conditionDescription.setText(weather.currentCondition.getCondition() + " (" + weather.currentCondition.getDescription() + ")");
            temperature.setText("" + Math.round((weather.temperature.getTemperature() - 273.15)) + "\u00B0" + "C");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + " hPa");
            windSpeed.setText("Wind: " + weather.wind.getSpeed() + " m/s " + weather.wind.getDegrees() + "\u00B0");
            currentTimeDate.setText("" + weather.currentCondition.getTimeDate());

            mainLayout.setVisibility(View.VISIBLE);
            forecastLayout.setVisibility(View.VISIBLE);
            startLayout.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e("SimpleWeather", "Field not present in JSON received");
        }
    }

    //Skriver ut och fyller de olika objekten för det kommande vädret i min layout
    private void renderForecast(String json) {
        try {
            ArrayList<Forecast> forecasts= JSONForecastParser.getForecast(json);

            forecast1.setText(forecasts.get(0).forecastCondition.getTimeDate());
            setWeatherIcon(forecasts.get(0).forecastCondition.getIcon(), forecastIcon1);
            forecastTemp1.setText("\u2193 " + Math.round((forecasts.get(0).temperature.getMinTemp() - 273.15)) + "\u00B0" + " \u2191 " + Math.round((forecasts.get(0).temperature.getMaxTemp() - 273.15)) + "\u00B0" + "C");
            forecast2.setText(forecasts.get(1).forecastCondition.getTimeDate());
            setWeatherIcon(forecasts.get(1).forecastCondition.getIcon(), forecastIcon2);
            forecastTemp2.setText("\u2193 " + Math.round((forecasts.get(1).temperature.getMinTemp() - 273.15)) + "\u00B0" + " \u2191 " + Math.round((forecasts.get(1).temperature.getMaxTemp() - 273.15)) + "\u00B0" + "C");
            forecast3.setText(forecasts.get(2).forecastCondition.getTimeDate());
            setWeatherIcon(forecasts.get(2).forecastCondition.getIcon(), forecastIcon3);
            forecastTemp3.setText("\u2193 " + Math.round((forecasts.get(2).temperature.getMinTemp() - 273.15)) + "\u00B0" + " \u2191 " + Math.round((forecasts.get(2).temperature.getMaxTemp() - 273.15)) + "\u00B0" + "C");

        } catch (Exception e) {
            Log.e("SimpleWeather", "Field not present in JSON received" + e.getMessage());
        }
    }

    //Sätter väderikonen
    private void setWeatherIcon(String iconString, ImageView conditionIcon) {
        switch (iconString) {
            case "01d":
                conditionIcon.setImageResource(R.drawable.clear_sky_day);
                break;
            case "01n":
                conditionIcon.setImageResource(R.drawable.clear_sky_night);
                break;
            case "02d":
                conditionIcon.setImageResource(R.drawable.few_clouds_day);
                break;
            case "02n":
                conditionIcon.setImageResource(R.drawable.few_clouds_night);
                break;
            case "03d":
                conditionIcon.setImageResource(R.drawable.scattered_clouds_day);
                break;
            case "03n":
                conditionIcon.setImageResource(R.drawable.scattered_clouds_night);
                break;
            case "04d":
                conditionIcon.setImageResource(R.drawable.broken_clouds_day);
                break;
            case "04n":
                conditionIcon.setImageResource(R.drawable.broken_clouds_night);
                break;
            case "09d":
                conditionIcon.setImageResource(R.drawable.shower_rain_day);
                break;
            case "09n":
                conditionIcon.setImageResource(R.drawable.shower_rain_night);
                break;
            case "10d":
                conditionIcon.setImageResource(R.drawable.rain_day);
                break;
            case "10n":
                conditionIcon.setImageResource(R.drawable.rain_night);
                break;
            case "11d":
                conditionIcon.setImageResource(R.drawable.thunderstorm_day);
                break;
            case "11n":
                conditionIcon.setImageResource(R.drawable.thunderstorm_night);
                break;
            case "13d@2x":
                conditionIcon.setImageResource(R.drawable.snow_day);
                break;
            case "13n":
                conditionIcon.setImageResource(R.drawable.snow_night);
                break;
            case "50d":
                conditionIcon.setImageResource(R.drawable.mist_day);
                break;
            case "50n":
                conditionIcon.setImageResource(R.drawable.mist_night);
                break;
        }
    }

    public void changeCity(String city) {
        updateWeatherData(city);
        updateForecastData(city);
    }
}

