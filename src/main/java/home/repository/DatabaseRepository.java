package home.repository;

import home.responses.DailyForecastsEntry;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepository {
    void closeRepository() throws SQLException;
}
