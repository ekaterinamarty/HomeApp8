package home.requests;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;

import java.io.IOException;

import home.ApplicationGlobalState;

public class LocationKeyRequest extends AccuWeatherRequest {
    private final String locationText;

    public LocationKeyRequest(String locationText) {
        if (locationText == null || locationText.length() == 0)
            throw new IllegalArgumentException("Location text cannot be null or empty.");
        this.locationText = locationText;
    }

    @Override
    HttpUrl getUrl() {
        return new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment("locations")
                .addPathSegment(API_VERSION)
                .addPathSegment("cities")
                .addPathSegment("autocomplete")
                .addQueryParameter("apikey", ApplicationGlobalState.getInstance().getApiKey())
                .addQueryParameter("q", locationText)
                .build();
    }

    public String getLocationKey() throws IOException {
        System.out.println("Произвожу поиск города " + locationText);
        var mapper = new ObjectMapper();
        var json = getJson();
        var tree = mapper.readTree(json);
        if (tree.size() > 0) {
            var cityName = tree.get(0).at("/LocalizedName").asText();
            var countryName = tree.get(0).at("/Country/LocalizedName").asText();
            System.out.println("Найден город " + cityName + " в стране " + countryName);
            return tree.get(0).at("/Key").asText();
        } else throw new IOException("Server returns 0 cities");
    }
}
