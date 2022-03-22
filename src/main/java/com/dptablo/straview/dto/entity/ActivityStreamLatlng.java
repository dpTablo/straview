package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.Latlng;
import com.dptablo.straview.dto.converter.StringListLatlngConverter;
import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.converter.StringListFloatConverter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@Entity(name="activity_stream_latlng")
@DiscriminatorValue("latlng")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityStreamLatlng extends ActivityStream {
    @Column(name = "data")
    @Convert(converter = StringListLatlngConverter.class)
    @JsonProperty("data")
    private List<Latlng> data;

    @Builder
    public ActivityStreamLatlng(
            ActivityStreamType type,
            SummaryActivity summaryActivity,
            Long originalSize,
            ActivityStreamResolution resolution,
            ActivityStreamType seriesType,
            List<Latlng> data
    ) {
        super(type, summaryActivity, originalSize, resolution, seriesType);
        this.data = data;
    }
}
