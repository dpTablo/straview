package com.dptablo.straview.dto.enumtype;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

public enum ResourceState {
    META(1),
    SUMMARY(2),
    DETAIL(3);

    @Getter
    private final Integer value;

    ResourceState(Integer value) {
        this.value = value;
    }

    @JsonCreator
    public static ResourceState valueOf(Integer value) {
        switch (value) {
            case 1:
                return ResourceState.META;
            case 2:
                return ResourceState.SUMMARY;
            case 3:
                return ResourceState.DETAIL;
            default:
                throw new IllegalArgumentException("ResourceState argument : " + value);
        }
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
