package home.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class ForecastEntryTime {
    private String iconPhrase;
    private boolean hasPrecipitation;

    @JsonGetter("IconPhrase")
    public String getIconPhrase() {
        return iconPhrase;
    }

    @JsonSetter("IconPhrase")
    public void setIconPhrase(String iconPhrase) {
        this.iconPhrase = iconPhrase;
    }

    @JsonGetter("HasPrecipitation")
    public boolean isHasPrecipitation() {
        return hasPrecipitation;
    }

    @JsonSetter("HasPrecipitation")
    public void setHasPrecipitation(boolean hasPrecipitation) {
        this.hasPrecipitation = hasPrecipitation;
    }
}
