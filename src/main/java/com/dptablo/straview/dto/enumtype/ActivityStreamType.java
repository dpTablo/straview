package com.dptablo.straview.dto.enumtype;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public enum ActivityStreamType {
    @JsonProperty("time")
    TIME("time"),
    @JsonProperty("distance")
    DISTANCE("distance"),
    @JsonProperty("latlng")
    LATLNG("latlng"),
    @JsonProperty("altitude")
    ALTITUDE("altitude"),
    @JsonProperty("velocity_smooth")
    VELOCITY_SMOOTH("velocity_smooth"),
    @JsonProperty("heartrate")
    HEARTRATE("heartrate"),
    @JsonProperty("cadence")
    CADENCE("cadence"),
    @JsonProperty("watts")
    WATTS("watts"),
    @JsonProperty("temp")
    TEMP("temp"),
    @JsonProperty("moving")
    MOVING("moving"),
    @JsonProperty("grade_smooth")
    GRADE_SMOOTH("grade_smooth");

    @Getter
    private final String value;

    ActivityStreamType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.name();
    }


}
