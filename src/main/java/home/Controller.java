package home;

import home.pipeline.Action;
import home.providers.AccuWeatherProvider;
import home.providers.WeatherProvider;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    WeatherProvider weatherProvider = new AccuWeatherProvider();
    Map<Integer, Action> variantResult = new HashMap<>();

    public Controller() {
        variantResult.put(1, this::getCurrentWeather);
        variantResult.put(2, this::getWeatherIn1Day);
        variantResult.put(3, this::getWeatherIn5Days);
        variantResult.put(4, this::showAllSavedData);
    }

    public void onUserInput(String input) throws IOException, SQLException {
        int command = Integer.parseInt(input);
        if (!variantResult.containsKey(command)) {
            throw new IOException("There is no command for command-key " + command);
        }
        variantResult.get(command).invoke();
    }

    public void getCurrentWeather() throws IOException {
        weatherProvider.getCurrentWeather();
    }

    public void getWeatherIn1Day() throws IOException, SQLException {
        weatherProvider.get1DayForecast();
    }

    public void getWeatherIn5Days() throws IOException, SQLException {
        weatherProvider.get5DaysForecasts();
    }

    public void showAllSavedData() throws SQLException {
        weatherProvider.getAllSavedData();
    }

    public void onCloseApp() throws SQLException {
        weatherProvider.Shutdown();
    }
}
