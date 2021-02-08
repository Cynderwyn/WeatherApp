package com.example.whichweather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONWeatherParser {

    //Tar JSON-datan, parsar den och lägger in den i ett objekt av klassen Weather
    public static Weather getWeather(String data) throws JSONException {
        Weather weather = new Weather();

        //Skapar upp ett JSON-Objekt från datan
        JSONObject jsonObject = new JSONObject(data);

        //Extraherar informationen
        Location locations = new Location();

        JSONObject coordinatesObject = getObject("coord", jsonObject);
        locations.setLatitude(getFloat("lat", coordinatesObject));
        locations.setLongitude(getFloat("lon", coordinatesObject));

        JSONObject sysObj = getObject("sys", jsonObject);
        locations.setCountry(getString("country", sysObj));
        locations.setSunrise(getInt("sunrise", sysObj));
        locations.setSunset(getInt("sunset", sysObj));
        locations.setCity(getString("name", jsonObject));
        weather.location = locations;

        //Tar in väder informationen, en array
        JSONArray jsonArray = jsonObject.getJSONArray("weather");

        //Jag tar bara in och använder mig av första värdet
        JSONObject JSONWeather = jsonArray.getJSONObject(0);
        weather.currentCondition.setWeatherId(getInt("id", JSONWeather));
        weather.currentCondition.setDescription(getString("description", JSONWeather));
        weather.currentCondition.setCondition(getString("main", JSONWeather));
        weather.currentCondition.setIcon(getString("icon", JSONWeather));

        JSONObject mainObject = getObject("main", jsonObject);
        weather.currentCondition.setHumidity(getInt("humidity", mainObject));
        weather.currentCondition.setPressure(getInt("pressure", mainObject));
        weather.temperature.setMaxTemperature(getFloat("temp_max", mainObject));
        weather.temperature.setMinTemperature(getFloat("temp_min", mainObject));
        weather.temperature.setTemperature(getFloat("temp", mainObject));

        //Wind
        JSONObject windObject = getObject("wind", jsonObject);
        weather.wind.setSpeed(getFloat("speed", windObject));
        weather.wind.setDegrees(getFloat("deg", windObject));

        //Clouds
        JSONObject cloudObj = getObject("clouds", jsonObject);
        weather.clouds.setPercentage(getInt("all", cloudObj));

        //Time and Date
        Long timeDateObj = getLong("dt", jsonObject);
        weather.currentCondition.setTimeDate(timeDateObj);

        //Timezone
        Long timeZoneObj = getLong("timezone", jsonObject);
        weather.currentCondition.setTimeZone(timeZoneObj);

        return weather;
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
