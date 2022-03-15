package com.dptablo.straview.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;

import java.util.Objects;

@Builder
@Getter
@Setter
@JsonFormat(shape = JsonFormat.Shape.ARRAY)
@JsonPropertyOrder({"latitude", "longitude"})
public class Latlng {
    @JsonProperty("latitude")
    private Double latitude;

    @JsonProperty("longitude")
    private Double longitude;

    @JsonCreator
    public Latlng(@JsonProperty("latitude") Double latitude, @JsonProperty("longitude") Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Latlng)) return false;
        Latlng latlng = (Latlng) o;
        return Objects.equals(latitude, latlng.latitude) && Objects.equals(longitude, latlng.longitude);
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
