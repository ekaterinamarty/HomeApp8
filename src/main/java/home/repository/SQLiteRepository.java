package home.repository;


import home.ApplicationGlobalState;
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

}
