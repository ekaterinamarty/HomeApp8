package home.providers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import home.ApplicationGlobalState;
import home.entities.DailyForecast;
import home.pipeline.Handler;
import home.pipeline.Pipeline;
import home.repository.DatabaseRepository;
import home.repository.SQLiteRepository;
import home.requests.CurrentConditionsRequest;
import home.requests.DailyForecastsRequests;
import home.requests.LocationKeyRequest;
import home.requests.RequestContext;
import home.responses.CurrentConditionsResponse;
import home.responses.DailyForecastsResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class AccuWeatherProvider implements WeatherProvider {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final DatabaseRepository repository = new SQLiteRepository();

    private final Pipeline<RequestContext, CurrentConditionsResponse> currentWeatherMiddleware;
    private final Pipeline<RequestContext, DailyForecastsResponse> dailyForecastsMiddleware;

    static class LocationKeyHandler implements Handler<RequestContext, RequestContext> {
        @Override
        public RequestContext invoke(RequestContext context) throws IOException {
            var text = (String)context.get("LocationText");
            context.put("LocationKey", new LocationKeyRequest(text).getLocationKey());
            return context;
        }
    }

    class CurrentConditionsHandler implements Handler<RequestContext, CurrentConditionsResponse> {
        @Override
        public CurrentConditionsResponse invoke(RequestContext context) throws IOException {
            var key = (String)context.get("LocationKey");
            return new CurrentConditionsRequest(key, objectMapper).getCurrentConditions();
        }
    }

    class DailyForecastsHandler implements Handler<RequestContext, DailyForecastsResponse> {
        @Override
        public DailyForecastsResponse invoke(RequestContext context) throws IOException {
            var key = (String)context.get("LocationKey");
            var period = (String)context.get("ForecastPeriod");
            return new DailyForecastsRequests(key, period, objectMapper).getForecastsResponse();
        }
    }

    public AccuWeatherProvider() {
        objectMapper.registerModule(new JavaTimeModule());
        currentWeatherMiddleware =
                new Pipeline<>(new LocationKeyHandler())
                        .add(new CurrentConditionsHandler());
        dailyForecastsMiddleware =
                new Pipeline<>(new LocationKeyHandler())
                        .add(new DailyForecastsHandler());
    }

    @Override
    public void getCurrentWeather() throws IOException {
        var context = new RequestContext();
        context.put("LocationText", ApplicationGlobalState.getInstance().getSelectedCity());
        var response = currentWeatherMiddleware.execute(context);
        System.out.printf(
                "Температура в '%s' %.1f%s.\n",
                ApplicationGlobalState.getInstance().getSelectedCity(),
                response.getTemperature().getMetric().getValue(), response.getTemperature().getMetric().getUnit());
        System.out.println();
    }

    @Override
    public void get1DayForecast() throws IOException, SQLException {
        getDailyForecasts("1day");
    }

    @Override
    public void get5DaysForecasts() throws IOException, SQLException {
        getDailyForecasts("5day");
    }

    private void getDailyForecasts(String period) throws IOException, SQLException {
        var context = new RequestContext();
        context.put("LocationText", ApplicationGlobalState.getInstance().getSelectedCity());
        context.put("ForecastPeriod", period);
        var response = dailyForecastsMiddleware.execute(context);
        for (var forecast : response.getForecasts()) {
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
        // Save to database here.
        var data = response.getForecasts()
                .stream().map(DailyForecast::new).collect(Collectors.toList());
        repository.saveDailyForecasts(data);
    }

    @Override
    public void getAllSavedData() throws SQLException {
        var list = repository.readDailyForecasts();
        System.out.printf("%-10s %-10s %-15s %-15s %6s %6s\n", "City", "Date", "Day", "Night", "Min.T", "Max.T");
        for (var item : list) {
            System.out.printf("%-10.10s ", item.getCity());
            System.out.printf("%-10.10s ", item.getDate());
            System.out.printf("%-15.15s ", item.getDayText());
            System.out.printf("%-15.15s ", item.getNightText());
            System.out.printf("%5.1fC ", item.getMinTemp());
            System.out.printf("%5.1fC ", item.getMaxTemp());
            System.out.println();
        }
        System.out.println();
    }

    public void Shutdown() throws SQLException {
        repository.closeRepository();
    }
}
