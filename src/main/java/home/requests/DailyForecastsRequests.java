package home.requests;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;

import java.io.IOException;

import home.ApplicationGlobalState;
import home.responses.DailyForecastsResponse;

public class DailyForecastsRequests extends AccuWeatherRequest {
    private final String locationKey;
    private final String forecastPeriod;
    private final ObjectMapper objectMapper;

    public DailyForecastsRequests(String locationKey, String forecastPeriod, ObjectMapper objectMapper) {
        if (locationKey == null || locationKey.length() == 0)
            throw new IllegalArgumentException("Location Key cannot be null or empty.");
        if (forecastPeriod == null || forecastPeriod.length() == 0)
            throw new IllegalArgumentException("Forecast Period cannot be null or empty.");
        this.locationKey = locationKey;
        this.forecastPeriod = forecastPeriod;
        this.objectMapper = objectMapper;
    }

    @Override
    HttpUrl getUrl() {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment("forecasts")
                .addPathSegment(API_VERSION)
                .addPathSegment("daily")
                .addPathSegment(forecastPeriod)
                .addPathSegment(locationKey)
                .addQueryParameter("apikey", ApplicationGlobalState.getInstance().getApiKey())
                .addQueryParameter("language", "ru-ru")
                .addQueryParameter("metric", "true")
                .build();
    }

    public DailyForecastsResponse getForecastsResponse() throws IOException {
        var json = getJson();
        return objectMapper.readValue(json, DailyForecastsResponse.class);
    }
}
