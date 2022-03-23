package com.dptablo.straview.dto.enumtype;

import com.dptablo.straview.dto.entity.*;
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

    public <T extends ActivityStream> Class<T> getActivityStreamEntityClass() {
        switch(this) {
            case VELOCITY_SMOOTH:
                return (Class<T>) ActivityStreamVelocitySmooth.class;
            case DISTANCE:
                return (Class<T>) ActivityStreamDistance.class;
            case WATTS:
                return (Class<T>) ActivityStreamWatts.class;
            case HEARTRATE:
                return (Class<T>) ActivityStreamHeartrate.class;
            case ALTITUDE:
                return (Class<T>) ActivityStreamAltitude.class;
            case CADENCE:
                return (Class<T>) ActivityStreamCadence.class;
            case GRADE_SMOOTH:
                return (Class<T>) ActivityStreamGradeSmooth.class;
            case LATLNG:
                return (Class<T>) ActivityStreamLatlng.class;
            case MOVING:
                return (Class<T>) ActivityStreamMoving.class;
            case TEMP:
                return (Class<T>) ActivityStreamTemp.class;
            case TIME:
                return (Class<T>) ActivityStreamTime.class;
        }
        return null;
    }
}
