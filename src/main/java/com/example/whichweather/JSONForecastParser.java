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

    //Tar in JSON-datan och parsar den för att sedan lägga in den i en lista av klassen Forecast
    public static ArrayList<Forecast> getForecast(String data) throws JSONException {
        Forecast forecast = new Forecast();
        ArrayList<Forecast> forecasts = new ArrayList<>();

        //Skapar upp ett JSON-Objekt från datan
        JSONObject jsonObject = new JSONObject(data);

        //Tar in informationen, en array
        JSONArray jsonArray = jsonObject.getJSONArray("list");

        //Skapar upp en ny forecast för varje element
        for (int i = 0; i < jsonArray.length(); i++) {

            forecast = new Forecast();

            //Jag tar bara in och använder mig av första värdet
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

            //lagrar en forecast i listan
            forecasts.add(forecast);
        }
        //returnerar en lista av prognosdata
        return forecastDate(forecasts);
    }


    //Tar in en lista med prognosdata och hämtar ut en lista med 3-dagar prognosdata
    public static ArrayList<Forecast> forecastDate(ArrayList<Forecast> forecasts) {
        ArrayList<Forecast> resultThreeDayForecast = new ArrayList<>();

        String firstForecastDay = forecasts.get(0).forecastCondition.getForecastDate();
        Calendar calForecastDay = Calendar.getInstance();
        Calendar calForecastForIndex = Calendar.getInstance();
        Date dateFirstForecast = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        //Konverterar första elementet dt_txt till Calendar format
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

        //Hämtar start-index för 3 dagar ur listan för att användas i huvudloopen
        //så jag vet vilket index varje dag startar på
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

        //huvudloopen för att hämta prognosdata för de 3 dagarna
        while (numberOfDaysCurrent < numberOfDaysForecast) {
            //Gå igenom all prognosdata för att hämta prognos de 3 dagarna och sätta min och max temp.
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

                //Sätt start min och max temp för startvärdet på den dag jag testar i while-loopen
                if (i == indexOfForecastDays[numberOfDaysCurrent]) {
                    minTemp = forecasts.get(i).temperature.getTemperature();
                    maxTemp = forecasts.get(i).temperature.getTemperature();
                    lastIndexOfDay = indexOfForecastDays[numberOfDaysCurrent];
                }

                //Testar om jag fortfarande är på samma dag om inte hoppa ur loopen och räkna ev. upp till nästa dag
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
