package home.responses;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;

public class TemperatureEntry {
    private double value;
    private String unit;
    private int unitType;

    @JsonGetter("Value")
    public double getValue() {
        return value;
    }

    @JsonGetter("Unit")
    public String getUnit() {
        return unit;
    }

    @JsonGetter("UnitType")
    public int getUnitType() {
        return unitType;
    }

    @JsonSetter("Value")
    public void setValue(double value) {
        this.value = value;
    }

    @JsonSetter("Unit")
    public void setUnit(String unit) {
        this.unit = unit;
    }

    @JsonSetter("UnitType")
    public void setUnitType(int unitType) {
        this.unitType = unitType;
    }
}