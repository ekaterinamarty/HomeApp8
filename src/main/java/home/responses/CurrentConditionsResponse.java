package home.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrentConditionsResponse {
    @JsonProperty("LocalObservationDateTime")
    private OffsetDateTime localObservationDateTime;

    @JsonProperty("Temperature")
    private CurrentConditionsTemperature temperature;
}
