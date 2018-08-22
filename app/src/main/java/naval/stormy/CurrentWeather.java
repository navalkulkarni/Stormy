package naval.stormy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class CurrentWeather {

    private String locationLabel;
    private String icon;
    private String summary;
    private String timezone;
    private long time;
    private double temp;
    private double humidity;
    private double precipChance;


    public CurrentWeather() {
    }

    public CurrentWeather(String locationLabel, String icon, String summary, String timezone,
                          long time, double temp, double humidity, double precipChance)
    {
        this.locationLabel = locationLabel;
        this.icon = icon;
        this.summary = summary;
        this.timezone = timezone;
        this.time = time;
        this.temp = temp;
        this.humidity = humidity;
        this.precipChance = precipChance;
    }

    public String getFormattedTime(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm:a");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezone));
        Date dateTime = new Date(time*1000);
        return simpleDateFormat.format(dateTime);

    }


    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLocationLabel() {
        return locationLabel;
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getIconId(){

        int iconId = R.drawable.clear_day;

        switch (icon) {
            case "clear-day":
                iconId = R.drawable.clear_day;
                break;
            case "rain":
                iconId = R.drawable.rain;
                break;
            case "snow":
                iconId = R.drawable.snow;
                break;
            case "sleet":
                iconId = R.drawable.sleet;
                break;
            case "wind":
                iconId = R.drawable.wind;
                break;
            case "fog":
                iconId = R.drawable.fog;
                break;
            case "cloudy":
                iconId = R.drawable.cloudy;
                break;
            case "partly-cloudy-day":
                iconId = R.drawable.partly_cloudy;
                break;
            case "partly-cloudy-night":
                iconId = R.drawable.cloudy_night;
                break;

        }
        return iconId;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }
}


