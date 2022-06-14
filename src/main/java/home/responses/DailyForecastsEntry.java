package home.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DailyForecastsEntry {
    @JsonProperty("Date")
    private OffsetDateTime date;

    @JsonProperty("Temperature")
    private DailyForecastsTemperature temperature;

    @JsonProperty("Day")
    private DailyForecastsEntryTime day;

    @JsonProperty("Night")
    private DailyForecastsEntryTime night;
}
