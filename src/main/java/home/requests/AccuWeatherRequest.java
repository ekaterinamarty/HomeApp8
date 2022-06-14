package home.requests;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import java.io.IOException;

public abstract class AccuWeatherRequest {
    static final String BASE_HOST = "dataservice.accuweather.com";
    static final String API_VERSION = "v1";

    abstract HttpUrl getUrl();

    String getJson() throws IOException {
        var url = getUrl();

        var request = new Request.Builder()
                .url(url)
                .build();

        var client = new OkHttpClient();
        try (var response = client.newCall(request).execute()) {
            var body = response.body();
            if (body != null) {
                return body.string();
            }
        }
        return null;
    }
}
