package com.example.whichweather;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

//Håller aktuell väderdata för vald placering enligt JSON-strukturen
public class Weather {
    public Location location;
    public CurrentCondition currentCondition = new CurrentCondition();
    public Temperature temperature = new Temperature();
    public Wind wind = new Wind();
    public Rain rain = new Rain();
    public Snow snow = new Snow();
    public Clouds clouds = new Clouds();

    public class CurrentCondition {
        private int weatherId;
        private String condition;
        private long timeDate;
        private String description;
        private String icon;
        private float pressure;
        private float humidity;
        private long timeZone;

        public int getWeatherId() {
            return weatherId;
        }

        public void setWeatherId(int weatherId) {
            this.weatherId = weatherId;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getTimeDate() {
            //Skriver ut datum och tid
            long time = this.timeDate * (long) 1000;
            Date date = new Date(time);
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE d MMMM yyyy HH:mm ");
            TimeZone timeZone = TimeZone.getTimeZone("GMT");

            sdf.setTimeZone(timeZone);


            int timeAdjust = (int) this.timeZone/3600;

            Calendar cal = Calendar.getInstance();          // Skapar upp Calendar instans
            cal.setTime(date);                              // sätter calendat time/date
            cal.add(Calendar.HOUR_OF_DAY, timeAdjust);      // lägger till en timme

            return sdf.format(cal.getTime());

        }

        public void setTimeDate(long timeDate) {
            this.timeDate = timeDate;
        }

        public Long getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(long timeZone) {
            this.timeZone = timeZone;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public float getPressure() {
            return pressure;
        }

        public void setPressure(float preassure) {
            this.pressure = preassure;
        }

        public float getHumidity() {
            return humidity;
        }

        public void setHumidity(float humidity) {
            this.humidity = humidity;
        }

    }

    public class Temperature {
        private float temperature;
        private float minTemperature;
        private float maxTemperature;

        public float getTemperature() {
            return temperature;
        }

        public void setTemperature(float temperature) {
            this.temperature = temperature;
        }

        public float getMinTemperature() {
            return minTemperature;
        }

        public void setMinTemperature(float minTemperature) {
            this.minTemperature = minTemperature;
        }

        public float getMaxTemperature() {
            return maxTemperature;
        }

        public void setMaxTemperature(float maxTemperature) {
            this.maxTemperature = maxTemperature;
        }
    }

    public class Wind {
        private float speed;
        private float degrees;

        public float getSpeed() {
            return speed;
        }

        public void setSpeed(float speed) {
            this.speed = speed;
        }

        public float getDegrees() {
            return degrees;
        }

        public void setDegrees(float degrees) {
            this.degrees = degrees;
        }
    }

    public class Rain {
        private String time;
        private float ammount;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmmount() {
            return ammount;
        }

        public void setAmmount(float ammount) {
            this.ammount = ammount;
        }


    }

    public class Snow {
        private String time;
        private float amount;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public float getAmount() {
            return amount;
        }

        public void setAmount(float amount) {
            this.amount = amount;
        }


    }

    public class Clouds {
        private int percentage;

        public int getPerc() {
            return percentage;
        }

        public void setPercentage(int percentage) {
            this.percentage = percentage;
        }


    }

}
