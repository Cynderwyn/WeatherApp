package com.example.whichweather;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;

public class WeatherHttpClient {
    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
    private static String FORECAST_BASE_URL = "https://api.openweathermap.org/data/2.5/forecast?q=";
    private static String APPID = "6b3898ebf97f324cd5e3a5698f19b061";

    public static String getWeatherData(Context context, String location) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            //Koppling till current weather url från openweathermap
            String weatherURL = BASE_URL + location + "&appid=" + APPID;
            connection = (HttpURLConnection) (new URL(weatherURL)).openConnection();

            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            //Läser svaret, reads the response
            StringBuffer buffer = new StringBuffer();
            inputStream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\r\n");
            }
            inputStream.close();
            connection.disconnect();

            return buffer.toString();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Throwable throwable) {
            }
            try {
                connection.disconnect();
            } catch (Throwable throwable) {
            }
        }
        return null;
    }

    public static String getForecastData(Context context, String location) {
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {

            //Koppling till forcast 5 day/3 hour url från openweathermap
            String forecastURL = FORECAST_BASE_URL + location + "&appid=" + APPID;
            System.out.println(forecastURL);
            connection = (HttpURLConnection) (new URL(forecastURL)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);

            connection.connect();

            //Läser svaret, reads the response
            StringBuffer buffer = new StringBuffer();
            inputStream = connection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line + "\r\n");
            }
            inputStream.close();
            connection.disconnect();


            return buffer.toString();

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (Throwable throwable) {
            }
            try {
                connection.disconnect();
            } catch (Throwable throwable) {
            }
        }
        return null;
    }
}



















