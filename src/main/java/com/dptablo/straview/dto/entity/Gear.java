package com.dptablo.straview.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "gear")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@DynamicUpdate
@Getter
@Setter
public class Gear implements Serializable {
    @Id
    @Column(name = "manage_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long manageId;

    @Column(name = "gear_id")
    @JsonProperty("id")
    private String gearId;

    @Column(name = "resource_state")
    @JsonProperty("resource_state")
    private Integer resourceState;

    @Column(name = "primary_flag")
    @JsonProperty("primary")
    private Boolean primaryFlag;

    @Column(name = "name")
    @JsonProperty("name")
    private String name;

    @Column(name = "distance")
    @JsonProperty("distance")
    private Float distance;

    @Column(name = "converted_distance")
    @JsonProperty("converted_distance")
    private Float convertedDistance;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn
    private StravaAthlete athlete;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Gear)) return false;
        Gear gear = (Gear) o;
        return manageId.equals(gear.manageId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(manageId);
    }
}
