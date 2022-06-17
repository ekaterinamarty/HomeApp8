package home.responses;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyForecastsResponse {
    private final List<DailyForecastsEntry> forecasts = new ArrayList<>();

    @JsonGetter("DailyForecasts")
    public List<DailyForecastsEntry> getForecasts() {
        return forecasts;
    }
}
