package com.dptablo.straview.dto.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "strava_sync_info")
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class StravaSyncInfo {
    @Id
    @Column(name = "athlete_id", unique = true, nullable = false)
    @JsonProperty("athlete_id")
    private Long athleteId;

    @Column(name = "sync_epoch_time", unique = true, nullable = false)
    @JsonProperty("sync_epoch_time")
    private Long syncEpochTime;
}
