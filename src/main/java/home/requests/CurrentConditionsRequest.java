package home.requests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;

import java.io.IOException;
import java.util.List;

import home.ApplicationGlobalState;
import home.responses.CurrentConditionsResponse;

public class CurrentConditionsRequest extends AccuWeatherRequest {
    private final String locationKey;
    private final ObjectMapper objectMapper;

    public CurrentConditionsRequest(String locationKey, ObjectMapper objectMapper) {
        if (locationKey == null || locationKey.length() == 0)
            throw new IllegalArgumentException("Location Key cannot be null or empty.");
        this.locationKey = locationKey;
        this.objectMapper = objectMapper;
    }

    @Override
    HttpUrl getUrl() {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment("currentconditions")
                .addPathSegment(API_VERSION)
                .addPathSegment(locationKey)
                .addQueryParameter("apikey", ApplicationGlobalState.getInstance().getApiKey())
                .addQueryParameter("language", "ru-ru")
                .build();
    }

    public CurrentConditionsResponse getCurrentConditions() throws IOException {
        var json = getJson();
        var list = objectMapper.readValue(json, new TypeReference<List<CurrentConditionsResponse>>() { });
        return list.get(0);
    }
}
