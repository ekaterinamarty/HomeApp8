package home;

import home.enums.Periods;

import java.io.IOException;

public interface WeatherProvider {
    void getWeather(Periods period) throws IOException;
}
