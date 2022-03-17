package com.dptablo.straview.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "activity_power_info")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class ActivityPowerInfo {
    @Id
    @Column(name = "activity_manage_id")
    private Long activityManageId;

    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "activity_manage_id")
    private SummaryActivity activity;

    @Column(name = "max")
    @JsonProperty("max")
    private Integer max;

    @Column(name = "average")
    @JsonProperty("average")
    private Integer average;

    @Column(name = "np")
    @JsonProperty("np")
    private Integer np;

    @Column(name = "if_score")
    @JsonProperty("if_score")
    private Float ifScore;

    @Column(name = "tss")
    @JsonProperty("tss")
    private Float tss;

    @Column(name = "ftp")
    @JsonProperty("ftp")
    private Integer ftp;

    @Column(name = "kilojoules")
    @JsonProperty("kilojoules")
    private Float kilojoules;

    @Column(name = "z1_percent")
    @JsonProperty("z1_percent")
    private Float z1Percent;

    @Column(name = "z2_percent")
    @JsonProperty("z2_percent")
    private Float z2Percent;

    @Column(name = "z3_percent")
    @JsonProperty("z3_percent")
    private Float z3Percent;

    @Column(name = "z4_percent")
    @JsonProperty("z4_percent")
    private Float z4Percent;

    @Column(name = "z5_percent")
    @JsonProperty("z5_percent")
    private Float z5Percent;

    @Column(name = "z6_percent")
    @JsonProperty("z6_percent")
    private Float z6Percent;

    @Column(name = "z7_percent")
    @JsonProperty("z7_percent")
    private Float z7Percent;

    @Column(name = "z1_seconds")
    @JsonProperty("z1_seconds")
    private Integer z1Seconds;

    @Column(name = "z2_seconds")
    @JsonProperty("z2_seconds")
    private Integer z2Seconds;

    @Column(name = "z3_seconds")
    @JsonProperty("z3_seconds")
    private Integer z3Seconds;

    @Column(name = "z4_seconds")
    @JsonProperty("z4_seconds")
    private Integer z4Seconds;

    @Column(name = "z5_seconds")
    @JsonProperty("z5_seconds")
    private Integer z5Seconds;

    @Column(name = "z6_seconds")
    @JsonProperty("z6_seconds")
    private Integer z6Seconds;

    @Column(name = "z7_seconds")
    @JsonProperty("z7_seconds")
    private Integer z7Seconds;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityPowerInfo)) return false;
        ActivityPowerInfo that = (ActivityPowerInfo) o;
        return Objects.equals(activityManageId, that.activityManageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(activityManageId);
    }
}
