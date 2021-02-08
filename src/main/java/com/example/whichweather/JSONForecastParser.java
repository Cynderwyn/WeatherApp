package com.example.whichweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class JSONForecastParser {

    //Gets the JSON data and parses it to add it in a list of the class Forecast
    public static ArrayList<Forecast> getForecast(String data) throws JSONException {
        Forecast forecast = new Forecast();
        ArrayList<Forecast> forecasts = new ArrayList<>();

        //Creates a JSON object from the data
        JSONObject jsonObject = new JSONObject(data);

        //Gets the information, an array
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        //Creates a new forecast for each element
        for (int i = 0; i < jsonArray.length(); i++) {

            forecast = new Forecast();

            //I only get and use the first value
            JSONObject JSONForecast = jsonArray.getJSONObject(i);

            //Time and Date
            Long timeDateObj = getLong("dt", JSONForecast);
            forecast.forecastCondition.setTimeDate(timeDateObj);

            //forecastDate
            String forecastDateObj = getString("dt_txt", JSONForecast);
            forecast.forecastCondition.setForecastDate(forecastDateObj);

            //temperature
            JSONObject mainObject = getObject("main", JSONForecast);
            forecast.temperature.setTemperature(getFloat("temp", mainObject));

            //icon
            JSONArray jsonWeatherArray = JSONForecast.getJSONArray("weather");
            JSONObject JSONWeather = jsonWeatherArray.getJSONObject(0);
            forecast.forecastCondition.setIcon(getString("icon", JSONWeather));

            //stores a forecast in the list
            forecasts.add(forecast);
        }
        //returns a list of the forecast data
        return forecastDate(forecasts);
    }

    //Get a list with forecast data and gets a list with a 3 day forcast
    public static ArrayList<Forecast> forecastDate(ArrayList<Forecast> forecasts) {
        ArrayList<Forecast> resultThreeDayForecast = new ArrayList<>();

        String firstForecastDay = forecasts.get(0).forecastCondition.getForecastDate();
        Calendar calForecastDay = Calendar.getInstance();
        Calendar calForecastForIndex = Calendar.getInstance();
        Date dateFirstForecast = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        //Converts the first element dt_text to Calendat format
        try {
            dateFirstForecast = format.parse(firstForecastDay);
            calForecastDay.setTime(dateFirstForecast);
            calForecastDay.add(Calendar.DATE, 1);
            calForecastForIndex.setTime(dateFirstForecast);
            calForecastForIndex.add(Calendar.DATE, 1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calCurrent = Calendar.getInstance();
        int lastIndexOfDay = -1;
        float minTemp = 0;
        float maxTemp = 0;
        int numberOfDaysForecast = 3;
        int numberOfDaysCurrent = 0;
        int[] indexOfForecastDays = new int[3];
        
        //Get the start-index for 3 days from the list to be used in the main loop
        //This is to know which index each day starts on
        while (numberOfDaysCurrent < numberOfDaysForecast) {
            for (int i = 0; i < forecasts.size(); i++) {
                Date dateCurrent = new Date();
                String dtStart = forecasts.get(i).forecastCondition.getForecastDate();

                try {
                    dateCurrent = format.parse(dtStart);
                    calCurrent.setTime(dateCurrent);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (calForecastForIndex.compareTo(calCurrent) == 0) {
                    indexOfForecastDays[numberOfDaysCurrent] = i;
                    break;
                }
            }
            numberOfDaysCurrent++;
            calForecastForIndex.add(Calendar.DATE, 1);
        }

        numberOfDaysCurrent = 0;
        
        //The main loop to get forecast data for the first three days
        while (numberOfDaysCurrent < numberOfDaysForecast) {

            //Go through all the forecast data to get the forecast for the three days, and set the min and max temp
            for (int i = indexOfForecastDays[numberOfDaysCurrent]; i < forecasts.size(); i++) {
                Date dateCurrent = new Date();
                String dtStart = forecasts.get(i).forecastCondition.getForecastDate();

                try {
                    dateCurrent = format.parse(dtStart);
                    // skapar ett Calendar objekt av datumsträngen
                    calCurrent.setTime(dateCurrent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                //Set start min and max temp for the start value for the day I test in the while loop
                if (i == indexOfForecastDays[numberOfDaysCurrent]) {
                    minTemp = forecasts.get(i).temperature.getTemperature();
                    maxTemp = forecasts.get(i).temperature.getTemperature();
                    lastIndexOfDay = indexOfForecastDays[numberOfDaysCurrent];
                }

                //Test if I still am on the same day, if not it goes out of the loop and count up to the next day
                if (calForecastDay.compareTo(calCurrent) != 0) {
                    lastIndexOfDay = indexOfForecastDays[numberOfDaysCurrent];
                    break;
                } else {
                    if (minTemp > forecasts.get(i).temperature.getTemperature()) {
                        minTemp = forecasts.get(i).temperature.getTemperature();
                    }
                    if (maxTemp < forecasts.get(i).temperature.getTemperature()) {
                        maxTemp = forecasts.get(i).temperature.getTemperature();
                    }
                }
            }
            Forecast forecast = forecasts.get(lastIndexOfDay);
            forecast.temperature.setMinTemp(minTemp);
            forecast.temperature.setMaxTemp(maxTemp);

            resultThreeDayForecast.add(forecast);
            numberOfDaysCurrent++;
            calForecastDay.add(Calendar.DATE, 1);
        }

        return resultThreeDayForecast;

    }

    private static JSONObject getObject(String tagName, JSONObject jsonObject) throws JSONException {
        JSONObject subObject = jsonObject.getJSONObject(tagName);
        return subObject;
    }

    private static String getString(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getString(tagName);
    }

    private static float getFloat(String tagName, JSONObject jsonObject) throws JSONException {
        return (float) jsonObject.getDouble(tagName);
    }

    private static int getInt(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getInt(tagName);
    }

    private static long getLong(String tagName, JSONObject jsonObject) throws JSONException {
        return jsonObject.getLong(tagName);
    }

}
