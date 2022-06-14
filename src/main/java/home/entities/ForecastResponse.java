package home.entities;

import com.fasterxml.jackson.annotation.JsonGetter;

import java.util.ArrayList;
import java.util.List;

public class ForecastResponse {
    private final List<ForecastEntry> forecasts = new ArrayList<>();

    @JsonGetter("DailyForecasts")
    public List<ForecastEntry> getForecasts() {
        return forecasts;
    }
}
