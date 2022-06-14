package home.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ForecastTemperature {
    private TemperatureEntry minimum;
    private TemperatureEntry maximum;

    @JsonGetter("Minimum")
    public TemperatureEntry getMinimum() {
        return minimum;
    }

    @JsonGetter("Maximum")
    public TemperatureEntry getMaximum() {
        return maximum;
    }

    @JsonSetter("Minimum")
    public void setMinimum(TemperatureEntry minimum) {
        this.minimum = minimum;
    }

    @JsonSetter("Maximum")
    public void setMaximum(TemperatureEntry maximum) {
        this.maximum = maximum;
    }
}

