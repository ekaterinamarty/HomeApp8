package home.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

import home.ApplicationGlobalState;
import home.responses.DailyForecastsEntry;

@Data
@NoArgsConstructor
public class DailyForecast {
    private String city;
    private String date;
    private String dayText;
    private String nightText;
    private double minTemp;
    private double maxTemp;

    public DailyForecast(DailyForecastsEntry e) {
        this.city = ApplicationGlobalState.getInstance().getSelectedCity();
        this.date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(e.getDate());
        this.dayText = e.getDay().getIconPhrase();
        this.nightText = e.getNight().getIconPhrase();
        this.minTemp = e.getTemperature().getMinimum().getValue();
        this.maxTemp = e.getTemperature().getMaximum().getValue();
    }
}
