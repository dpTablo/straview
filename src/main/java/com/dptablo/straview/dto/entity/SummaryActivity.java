package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.Latlng;
import com.dptablo.straview.dto.enumtype.ResourceState;
import com.dptablo.straview.dto.enumtype.converter.LatlngConverter;
import com.dptablo.straview.dto.enumtype.converter.ResourceStateConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "summary_activity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SummaryActivity implements Serializable {
    @Transient
    @JsonProperty("gear_id")
    private String gearId;

    @Id
    @Column(name = "athlete_id", unique = true, nullable = false)
    @JsonProperty("id")
    private Long activityId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private StravaAthlete athlete;

    @Column(name = "resource_state")
    @Convert(converter = ResourceStateConverter.class)
    @JsonProperty("resource_state")
    private ResourceState resourceState;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "distance")
    @JsonProperty("distance")
    private Float distance;

    @Column(name = "moving_time")
    @JsonProperty("moving_time")
    private Integer movingTime;

    @Column(name = "elapsed_time")
    @JsonProperty("elapsed_time")
    private Integer elapsedTime;

    @Column(name = "total_elevation_gain")
    @JsonProperty("total_elevation_gain")
    private Float totalElevationGain;

    @Column(name = "type")
    @JsonProperty("type")
    private String type;

    @Column(name = "start_date")
    @JsonProperty("start_date")
    private String startDate;

    @Column(name = "start_date_local")
    @JsonProperty("start_date_local")
    private String startDateLocal;

    @Column(name = "timezone")
    @JsonProperty("timezone")
    private String timezone;

    @Column(name = "utc_offset")
    @JsonProperty("utc_offset")
    private Integer utcOffset;

    @Column(name = "location_city")
    @JsonProperty("location_city")
    private String locationCity;

    @Column(name = "location_state")
    @JsonProperty("location_state")
    private String locationState;

    @Column(name = "location_country")
    @JsonProperty("location_country")
    private String locationCountry;

    @Column(name = "achievement_count")
    @JsonProperty("achievement_count")
    private Integer achievementCount;

    @Column(name = "kudos_count")
    @JsonProperty("kudos_count")
    private Integer kudosCount;

    @Column(name = "comment_count")
    @JsonProperty("comment_count")
    private Integer commentCount;

    @Column(name = "athlete_count")
    @JsonProperty("athlete_count")
    private Integer athleteCount;

    @Column(name = "photo_count")
    @JsonProperty("photo_count")
    private Integer photoCount;

    @Column(name = "pr_count")
    @JsonProperty("pr_count")
    private Integer prCount;

    @Column(name = "total_photo_count")
    @JsonProperty("total_photo_count")
    private Integer totalPhotoCount;

    @Column(name = "trainer")
    @JsonProperty("trainer")
    private Boolean trainer;

    @Column(name = "commute")
    @JsonProperty("commute")
    private Boolean commute;

    @Column(name = "manual")
    @JsonProperty("manual")
    private Boolean manual;

    @Column(name = "private")
    @JsonProperty("private")
    private Boolean privateFlag;

    @Column(name = "visibility")
    @JsonProperty("visibility")
    private String visibility;

    @Column(name = "flagged")
    @JsonProperty("flagged")
    private Boolean flagged;

    @Column(name = "start_latlng")
    @Convert(converter = LatlngConverter.class)
    @JsonProperty("start_latlng")
    private Latlng startLatlng;

    @Column(name = "end_latlng")
    @Convert(converter = LatlngConverter.class)
    @JsonProperty("end_latlng")
    private Latlng endLatlng;

    @Column(name = "start_latitude")
    @JsonProperty("start_latitude")
    private Float startLatitude;

    @Column(name = "start_longitude")
    @JsonProperty("start_longitude")
    private Float startLongitude;

    @Column(name = "average_speed")
    @JsonProperty("average_speed")
    private Float averageSpeed;

    @Column(name = "max_speed")
    @JsonProperty("max_speed")
    private Float maxSpeed;

    @Column(name = "average_cadence")
    @JsonProperty("average_cadence")
    private Float averageCadence;

    @Column(name = "average_watts")
    @JsonProperty("average_watts")
    private Float averageWatts;

    @Column(name = "weighted_average_watts")
    @JsonProperty("weighted_average_watts")
    private Float weightedAverageWatts;

    @Column(name = "max_watts")
    @JsonProperty("max_watts")
    private Float maxWatts;

    @Column(name = "kilojoules")
    @JsonProperty("kilojoules")
    private Float kilojoules;

    @Column(name = "device_watts")
    @JsonProperty("device_watts")
    private Boolean deviceWatts;

    @Column(name = "has_heartrate")
    @JsonProperty("has_heartrate")
    private Boolean hasHeartrate;

    @Column(name = "elev_high")
    @JsonProperty("elev_high")
    private Float elevHigh;

    @Column(name = "elev_low")
    @JsonProperty("elev_low")
    private Float elevLow;

    @Column(name = "upload_id")
    @JsonProperty("upload_id")
    private Long uploadId;

    @Column(name = "external_id")
    @JsonProperty("external_id")
    private String externalId;

    @Column(name = "has_kudoed")
    @JsonProperty("has_kudoed")
    private Boolean hasKudoed;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "gear_id")
    private Gear gear;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "summaryActivity", targetEntity = ActivityStream.class)
    private List<ActivityStream> activityStreams;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SummaryActivity)) return false;
        SummaryActivity that = (SummaryActivity) o;
        return Objects.equals(activityId, that.activityId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityId);
    }
}