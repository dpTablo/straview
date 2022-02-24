package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.Activity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class ActivityRepositoryTest {
    @Autowired
    ActivityRepository activityRepository;

    @Test
    void save() {
        Activity activity = Activity.builder()
                .id(98723487)
                .externalId("external_id_12838")
                .uploadId(875348798)
                .name("ride_jfjfls")
                .startDate("2018-05-02T12:15:09Z")
                .startDateLocal("2018-05-02T12:15:09Z")
                .movingTime(4500)
                .elapsedTime(4500)
                .build();

        assertThatCode(() -> activityRepository.save(activity)).doesNotThrowAnyException();

        Optional<Activity> activity2 = activityRepository.findById(activity.getId());
        assertThat(activity2.isPresent()).isTrue();
    }
}