package com.dptablo.straview.dto.entity;

import com.dptablo.straview.dto.enumtype.ActivityStreamType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ActivityStreamPK implements Serializable {
    private ActivityStreamType type;
    private Long summaryActivity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ActivityStreamPK)) return false;
        ActivityStreamPK that = (ActivityStreamPK) o;
        return type == that.type && Objects.equals(summaryActivity, that.summaryActivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, summaryActivity);
    }
}