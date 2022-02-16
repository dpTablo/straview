package com.dptablo.straview.dto;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Table(name = "activity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
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

    @Override
    public boolean isNew() {
        return !this.update;
    }
}
