package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamResolution;
import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import com.dptablo.straview.dto.enumtype.converter.ActivityStreamTypeConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Table(name = "activity_stream")
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityStream implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "stream_id")
    private Long streamId;

    @Column(name = "type", nullable = false)
    @JsonProperty("type")
    @Convert(converter = ActivityStreamTypeConverter.class)
    private ActivityStreamType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_manage_id", nullable = false)
    @JsonProperty("activity_manage_id")
    private SummaryActivity summaryActivity;

    @Column(name = "original_size")
    @JsonProperty("original_size")
    private Long originalSize;

    @Column(name = "resolution")
    @JsonProperty("resolution")
    private ActivityStreamResolution resolution;

    @Column(name = "series_type")
    @Convert(converter = ActivityStreamTypeConverter.class)
    @JsonProperty("series_type")
    private ActivityStreamType seriesType;

    public ActivityStream(
            ActivityStreamType type,
            SummaryActivity summaryActivity,
            Long originalSize,
            ActivityStreamResolution resolution,
            ActivityStreamType seriesType
    ) {
        this.type = type;
        this.summaryActivity = summaryActivity;
        this.originalSize = originalSize;
        this.resolution = resolution;
        this.seriesType = seriesType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityStream)) return false;
        ActivityStream that = (ActivityStream) o;
        return type == that.type && Objects.equals(summaryActivity, that.summaryActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, summaryActivity);
    }
}
