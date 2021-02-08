package com.example.whichweather;

import android.app.Activity;
import android.content.SharedPreferences;

//Class for handeling the chosen location/city
public class CityPreference {

    SharedPreferences prefs;

    public CityPreference(Activity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return prefs.getString("city", "London, UK");
    }

    void setCity(String city) {
        prefs.edit().putString("city", city).commit();
    }

}
