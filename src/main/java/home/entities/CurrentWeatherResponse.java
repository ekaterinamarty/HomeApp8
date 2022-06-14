package home.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.time.OffsetDateTime;

public class CurrentWeatherResponse {
    private OffsetDateTime localObservationDateTime;
    private CurrentWeatherTemperature temperature;

    @JsonGetter("LocalObservationDateTime")
    public OffsetDateTime getLocalObservationDateTime() {
        return localObservationDateTime;
    }

    @JsonGetter("Temperature")
    public CurrentWeatherTemperature getTemperature() {
        return temperature;
    }

    @JsonSetter("LocalObservationDateTime")
    public void setLocalObservationDateTime(OffsetDateTime localObservationDateTime) {
        this.localObservationDateTime = localObservationDateTime;
    }

    @JsonSetter("Temperature")
    public void setTemperature(CurrentWeatherTemperature temperature) {
        this.temperature = temperature;
    }
}
