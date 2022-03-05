package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.StravaSyncInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class StravaSyncInfoRepositoryTest {
    @Autowired
    private StravaSyncInfoRepository repository;

    @Test
    public void save() {
        //given
        long athleteId = 123456L;
        long syncEpochTime = 1646457716L;

        StravaSyncInfo info = StravaSyncInfo.builder()
                .athleteId(athleteId)
                .syncEpochTime(syncEpochTime)
                .build();

        //when
        StravaSyncInfo retrunedInfo = repository.save(info);
        StravaSyncInfo foundInfo = repository.findById(athleteId).orElseThrow(NullPointerException::new);

        //then
        assertThat(foundInfo).isEqualTo(retrunedInfo);
    }
}