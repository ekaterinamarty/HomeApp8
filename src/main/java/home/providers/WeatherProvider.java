package home.providers;

import java.io.IOException;
import java.sql.SQLException;

public interface WeatherProvider {
    void getCurrentWeather() throws IOException;

    void get1DayForecast() throws IOException, SQLException;

    void get5DaysForecasts() throws IOException, SQLException;

    void getAllSavedData() throws SQLException;

    void Shutdown() throws SQLException;
}
