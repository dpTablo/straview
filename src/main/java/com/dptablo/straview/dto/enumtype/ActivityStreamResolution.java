package com.dptablo.straview.dto.enumtype;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ActivityStreamResolution {
    @JsonProperty("low")
    LOW("low"),
    @JsonProperty("medium")
    MEDIUM("medium"),
    @JsonProperty("high")
    HIGH("high");

    @Getter
    private final String value;

    ActivityStreamResolution(String value) {
        this.value = value;
    }
}
