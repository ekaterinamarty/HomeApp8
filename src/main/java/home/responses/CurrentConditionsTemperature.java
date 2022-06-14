package home.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentConditionsTemperature {
    @JsonProperty("Metric")
    private TemperatureEntry metric;

    @JsonProperty("Imperial")
    private TemperatureEntry imperial;
}
