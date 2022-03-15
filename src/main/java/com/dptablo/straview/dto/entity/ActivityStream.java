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
@IdClass(ActivityStreamPK.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityStream implements Serializable {
    @Id
    @Column(name = "type", nullable = false)
    @JsonProperty("type")
    @Convert(converter = ActivityStreamTypeConverter.class)
    private ActivityStreamType type;

    @Id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_id", nullable = false)
    @JsonProperty("activity_id")
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
