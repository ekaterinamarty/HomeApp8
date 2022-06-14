package home;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import home.entities.CurrentWeatherResponse;
import home.entities.ForecastResponse;
import home.enums.Periods;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class AccuWeatherProvider implements WeatherProvider {
    private static final String BASE_HOST = "dataservice.accuweather.com";
    private static final String FORECAST_ENDPOINT = "forecasts";
    private static final String CURRENT_CONDITIONS_ENDPOINT = "currentconditions";
    private static final String API_VERSION = "v1";

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AccuWeatherProvider() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public void getWeather(Periods periods) throws IOException {
        String cityKey = detectCityKey();
        if (periods.equals(Periods.NOW)) {
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(CURRENT_CONDITIONS_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", ApplicationGlobalState.getInstance().getApiKey())
                    .build();

            Request request = new Request.Builder()
                    .addHeader("accept", "application/json")
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                var body = response.body();
                if (body != null) {
                    var json = body.string();
                    var list = objectMapper.readValue(json, new TypeReference<List<CurrentWeatherResponse>>() { });
                    var item = list.get(0);
                    System.out.printf(
                            "Температура в '%s' %.1f%s.\n",
                            ApplicationGlobalState.getInstance().getSelectedCity(),
                            item.getTemperature().getMetric().getValue(), item.getTemperature().getMetric().getUnit());
                    System.out.println();
                }
            }
        } else if (periods.equals(Periods.FIVE_DAYS)) {
            var url = new HttpUrl.Builder()
                    .scheme("http")
                    .host(BASE_HOST)
                    .addPathSegment(FORECAST_ENDPOINT)
                    .addPathSegment(API_VERSION)
                    .addPathSegment("daily")
                    .addPathSegment("5day")
                    .addPathSegment(cityKey)
                    .addQueryParameter("apikey", ApplicationGlobalState.getInstance().getApiKey())
                    .addQueryParameter("language", "ru-ru")
                    .addQueryParameter("metric", "true")
                    .build();

            var request = new Request.Builder()
                    .url(url)
                    .build();

            try (var response = client.newCall(request).execute()) {
                var body = response.body();
                if (body != null) {
                    var json = body.string();
                    var root = objectMapper.readValue(json, ForecastResponse.class);
                    for (var forecast : root.getForecasts()) {
                        System.out.printf(
                                "В городе '%s' на дату '%s' ожидается: днем '%s', ночью '%s', температура от %.1f%s до %.1f%s.\n",
                                ApplicationGlobalState.getInstance().getSelectedCity(),
                                DateTimeFormatter.ofPattern("dd.MM.yyyy").format(forecast.getDate()),
                                forecast.getDay().getIconPhrase()
                                        + (forecast.getDay().isHasPrecipitation() ? ", с осадками" : ", без осадков"),
                                forecast.getNight().getIconPhrase()
                                        + (forecast.getNight().isHasPrecipitation() ? ", с осадками" : ", без осадков"),
                                forecast.getTemperature().getMinimum().getValue(), forecast.getTemperature().getMinimum().getUnit(),
                                forecast.getTemperature().getMaximum().getValue(), forecast.getTemperature().getMaximum().getUnit()
                        );
                    }
                    System.out.println();
                }
            }
        }
    }

    public String detectCityKey() throws IOException {
        String selectedCity = ApplicationGlobalState.getInstance().getSelectedCity();

        HttpUrl detectLocationURL = new HttpUrl.Builder()
                .scheme("http")
                .host(BASE_HOST)
                .addPathSegment("locations")
                .addPathSegment(API_VERSION)
                .addPathSegment("cities")
                .addPathSegment("autocomplete")
                .addQueryParameter("apikey", ApplicationGlobalState.getInstance().getApiKey())
                .addQueryParameter("q", selectedCity)
                .build();

        Request request = new Request.Builder()
                .addHeader("accept", "application/json")
                .url(detectLocationURL)
                .build();

        Response response = client.newCall(request).execute();

        if (!response.isSuccessful()) {
            throw new IOException("Невозможно прочесть информацию о городе. " +
                    "Код ответа сервера = " + response.code() + " тело ответа = " + response.body().string());
        }
        String jsonResponse = response.body().string();
        System.out.println("Произвожу поиск города " + selectedCity);

        if (objectMapper.readTree(jsonResponse).size() > 0) {
            String cityName = objectMapper.readTree(jsonResponse).get(0).at("/LocalizedName").asText();
            String countryName = objectMapper.readTree(jsonResponse).get(0).at("/Country/LocalizedName").asText();
            System.out.println("Найден город " + cityName + " в стране " + countryName);
        } else throw new IOException("Server returns 0 cities");

        return objectMapper.readTree(jsonResponse).get(0).at("/Key").asText();
    }
}
