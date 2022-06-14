package home.repository;


import home.ApplicationGlobalState;
import home.entities.DailyForecast;
import home.responses.DailyForecastsEntry;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SQLiteRepository implements DatabaseRepository {
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            // DDL section here. Database initialization.
            var repo = new SQLiteRepository();
            createTables(repo);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void createTables(SQLiteRepository repo) throws SQLException {
        var cmd_text =
                "CREATE TABLE IF NOT EXISTS daily_forecasts (" +
                "  id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ", city TEXT NOT NULL" +
                ", date_time TEXT NOT NULL" +
                ", weather_dayText TEXT NOT NULL" +
                ", weather_nightText TEXT NOT NULL" +
                ", minTemperature REAL NOT NULL" +
                ", maxTemperature REAL NOT NULL" +
                ")";
        var statement = repo.getConnection().createStatement();
        statement.execute(cmd_text);
        statement.close();
    }

    private static Connection connection;

    private Connection getConnection() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection("jdbc:sqlite:" + ApplicationGlobalState.getInstance().getDbFileName());
        return connection;
    }

    @Override
    public void closeRepository () throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    @Override
    public void saveDailyForecasts(List<DailyForecast> data) throws SQLException {
        // Only DML code here.
        var cmd_text =
                "INSERT INTO daily_forecasts(city, date_time, weather_dayText, weather_nightText, minTemperature, maxTemperature)\n" +
                        "VALUES (?,?,?,?,?,?)\n" +
                        ";";
        var statement = getConnection().prepareStatement(cmd_text);
        for (var item : data) {
            statement.setString(1, item.getCity());
            statement.setString(2, item.getDate());
            statement.setString(3, item.getDayText());
            statement.setString(4, item.getNightText());
            statement.setDouble(5, item.getMinTemp());
            statement.setDouble(6, item.getMaxTemp());
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
    }

    @Override
    public List<DailyForecast> readDailyForecasts() throws SQLException {
        var list = new ArrayList<DailyForecast>();
        var resultSet = getConnection().createStatement().executeQuery("SELECT * FROM daily_forecasts");
        while (resultSet.next()) {
            var item = new DailyForecast();
            item.setCity(resultSet.getString(2));
            item.setDate(resultSet.getString(3));
            item.setDayText(resultSet.getString(4));
            item.setNightText(resultSet.getString(5));
            item.setMinTemp(resultSet.getDouble(6));
            item.setMaxTemp(resultSet.getDouble(7));
            list.add(item);
        }
        resultSet.close();
        return list;
    }
}
