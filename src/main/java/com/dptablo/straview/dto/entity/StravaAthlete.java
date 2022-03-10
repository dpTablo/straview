package com.dptablo.straview.dto.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Strava Athlete 정보
 *
 * <pre>
 * {
 *   "id" : 1234567890987654321,
 *   "username" : "marianne_t",
 *   "resource_state" : 3,
 *   "firstname" : "Marianne",
 *   "lastname" : "Teutenberg",
 *   "city" : "San Francisco",
 *   "state" : "CA",
 *   "country" : "US",
 *   "sex" : "F",
 *   "premium" : true,
 *   "created_at" : "2017-11-14T02:30:05Z",
 *   "updated_at" : "2018-02-06T19:32:20Z",
 *   "badge_type_id" : 4,
 *   "profile_medium" : "https://xxxxxx.cloudfront.net/pictures/athletes/123456789/123456789/2/medium.jpg",
 *   "profile" : "https://xxxxx.cloudfront.net/pictures/athletes/123456789/123456789/2/large.jpg",
 *   "friend" : null,
 *   "follower" : null,
 *   "follower_count" : 5,
 *   "friend_count" : 5,
 *   "mutual_friend_count" : 0,
 *   "athlete_type" : 1,
 *   "date_preference" : "%m/%d/%Y",
 *   "measurement_preference" : "feet",
 *   "clubs" : [ ],
 *   "ftp" : null,
 *   "weight" : 0,
 *   "bikes" : [ {
 *     "id" : "b12345678987655",
 *     "primary" : true,
 *     "name" : "EMC",
 *     "resource_state" : 2,
 *     "distance" : 0
 *   } ],
 *   "shoes" : [ {
 *     "id" : "g12345678987655",
 *     "primary" : true,
 *     "name" : "adidas",
 *     "resource_state" : 2,
 *     "distance" : 4904
 *   } ]
 * }
 *
 * </pre>
 */
@Entity
@Table(name = "athlete")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StravaAthlete implements Serializable {
    @Id
    @Column(name = "athlete_id", unique = true, nullable = false)
    @JsonProperty("id")
    private Long athleteId;

    /**
     * 1: meta
     * 2: summary
     * 3: detail
     */
    @Column(name = "resource_state")
    @JsonProperty("resource_state")
    private Integer resourceState;

    @Column(name = "first_name")
    @JsonProperty("firstname")
    private String firstName;

    @Column(name = "last_name")
    @JsonProperty("lastname")
    private String lastName;

    /**
     * URL to a 62x62 pixel profile picture.
     */
    @Column(name = "profile_medium")
    @JsonProperty("profile_medium")
    private String profileMedium;

    /**
     * URL to a 124x124 pixel profile picture.
     */
    @Column(name = "profile")
    @JsonProperty("profile")
    private String profile;

    @Column(name = "city")
    @JsonProperty("city")
    private String city;

    @Column(name = "state")
    @JsonProperty("state")
    private String state;

    @Column(name = "country")
    @JsonProperty("country")
    private String country;

    /**
     * 'M', 'F'
     */
    @Column(name = "sex")
    @JsonProperty("sex")
    private String sex;

    /**
     * @deprecated Deprecated. Use summit field instead.
     */
    @Column(name = "premium")
    @JsonProperty("premium")
    private Boolean premium;

    /**
     * Whether the athlete has any Summit subscription.
     */
    @Column(name = "summit")
    @JsonProperty("summit")
    private Boolean summit;

    @Column(name = "created_at")
    @JsonProperty("created_at")
    private String createdAt;

    @Column(name = "updated_at")
    @JsonProperty("updated_at")
    private String updatedAt;

    @Column(name = "user_name")
    @JsonProperty("username")
    private String userName;

    @Column(name = "follower_count")
    @JsonProperty("follower_count")
    private Integer followerCount;

    @Column(name = "friend_count")
    @JsonProperty("friend_count")
    private Integer friendCount;

    /**
     * The athlete's preferred unit system.
     * 'feet', 'meters'
     */
    @Column(name = "measurement_preference")
    @JsonProperty("measurement_preference")
    private String measurementPreference;

    @Column(name = "ftp")
    @JsonProperty("ftp")
    private Integer ftp;

    @Column(name = "weight")
    @JsonProperty("weight")
    private Float weight;

    @Column(name = "badge_type_id")
    @JsonProperty("badge_type_id")
    private Integer badgeTypeId;

    @Column(name = "athlete_type")
    @JsonProperty("athlete_type")
    private Integer athleteType;

    @Column(name = "date_preference")
    @JsonProperty("date_preference")
    private String datePreference;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "athlete", cascade = CascadeType.ALL)
    @JsonProperty("bikes")
    @Builder.Default
    private List<Gear> bikes = new ArrayList<>();

    @OneToOne(mappedBy = "athlete", cascade = CascadeType.ALL)
    @JoinColumn
    @JsonIgnore
    private StravaOAuthTokenInfo stravaOAuthTokenInfo;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof StravaAthlete)) return false;
        StravaAthlete athlete = (StravaAthlete) o;
        return athleteId.equals(athlete.athleteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(athleteId);
    }
}
