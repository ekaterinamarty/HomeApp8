package home.repository;

import home.entities.DailyForecast;
import home.responses.DailyForecastsEntry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepository {
    void closeRepository() throws SQLException;

    void saveDailyForecasts(List<DailyForecast> data) throws SQLException;

    List<DailyForecast> readDailyForecasts() throws SQLException;
}
