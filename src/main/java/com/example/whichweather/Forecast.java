package com.example.whichweather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//Holds the current weather forecast for the chosen location according to the JSON structure
public class Forecast {

    public Forecast.ForecastCondition forecastCondition = new Forecast.ForecastCondition();
    public Forecast.ForecastTemperature temperature = new Forecast.ForecastTemperature();

    public class ForecastCondition {
        private String icon;
        private long timeDate;
        private String forecastDate;
        private long timeZone;

        public String getTimeDate() {

            //Forms the time and date
            long time = this.timeDate * (long) 1000;
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE ");
            TimeZone timeZone = TimeZone.getTimeZone("GMT");

            sdf.setTimeZone(timeZone);

            int timeAdjust = (int) this.timeZone / 3600;

            Calendar cal = Calendar.getInstance();          //Creates a Calendar instance
            cal.setTime(date);                              //Sets Calendar time/date 
            cal.add(Calendar.HOUR_OF_DAY, timeAdjust);      //Adds a hour

            return sdf.format(cal.getTime());
        }

        public void setTimeDate(long timeDate) {
            this.timeDate = timeDate;
        }

        public String getForecastDate() {
            return this.forecastDate;
        }

        public void setForecastDate(String forecastDate) {
            this.forecastDate = forecastDate;
        }

        public String getIcon() {
            return this.icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

    }

    public class ForecastTemperature {
        private float temperature;
        private float maxTemp;
        private float minTemp;

        public float getTemperature() {
            return this.temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public float getMaxTemp () {
            return this.maxTemp;
        }

        public void setMaxTemp(float maxTemp) {
            this.maxTemp = maxTemp;
        }

        public float getMinTemp () {
            return this.minTemp;
        }

        public void setMinTemp(float minTemp) {
            this.minTemp = minTemp;
        }

    }


}
