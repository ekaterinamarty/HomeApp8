package home.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.OffsetDateTime;

public class ForecastEntry {
    private OffsetDateTime date;
    private ForecastTemperature temperature;
    private ForecastEntryTime day;
    private ForecastEntryTime night;

    @JsonGetter("Date")
    public OffsetDateTime getDate() {
        return date;
    }

    @JsonGetter("Temperature")
    public ForecastTemperature getTemperature() {
        return temperature;
    }

    @JsonGetter("Day")
    public ForecastEntryTime getDay() {
        return day;
    }

    @JsonGetter("Night")
    public ForecastEntryTime getNight() {
        return night;
    }

    @JsonSetter("Date")
    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    @JsonSetter("Temperature")
    public void setTemperature(ForecastTemperature temperature) {
        this.temperature = temperature;
    }

    @JsonSetter("Day")
    public void setDay(ForecastEntryTime day) {
        this.day = day;
    }

    @JsonSetter("Night")
    public void setNight(ForecastEntryTime night) {
        this.night = night;
    }
}
