package com.dptablo.straview.dto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;


@Entity
@Table(name = "activity")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class Activity implements Persistable<Integer> {
    @Transient
    private boolean update;

    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "upload_id", nullable = false)
    private Integer uploadId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    private String startDate;

    @Column(name = "start_date_local", nullable = false)
    private String startDateLocal;

    @Column(name = "moving_time", nullable = false)
    private Integer movingTime;

    @Column(name = "elapsed_time", nullable = false)
    private Integer elapsedTime;

    @Builder
    public Activity(
            Integer id, String externalId, Integer uploadId,
            String name, String startDate, String startDateLocal,
            Integer movingTime, Integer elapsedTime
    ) {
        this.id = id;
        this.externalId = externalId;
        this.uploadId = uploadId;
        this.name = name;
        this.startDate = startDate;
        this.startDateLocal = startDateLocal;
        this.movingTime = movingTime;
        this.elapsedTime = elapsedTime;
    }

    @Override
    public boolean isNew() {
        return !this.update;
    }
}
