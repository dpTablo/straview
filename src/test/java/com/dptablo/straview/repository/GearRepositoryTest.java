package com.dptablo.straview.repository;

import com.dptablo.straview.dto.entity.Gear;
import com.dptablo.straview.dto.entity.StravaAthlete;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class GearRepositoryTest {
    @Autowired
    private GearRepository gearRepository;

    @Autowired
    private StravaAthleteRepository athleteRepository;

    @Test
    public void save() {
        //given
        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(9484L)
                .userName("dptablo")
                .build();

        Gear gear = Gear.builder()
                .gearId("b11")
                .resourceState(2)
                .name("2021 MERIDA REACTO")
                .primaryFlag(true)
                .distance(3889259f)
                .convertedDistance(3889.3f)
                .athlete(athlete)
                .build();

        //when
        Gear savedGear = gearRepository.save(gear);

        //then
        Gear foundGear = gearRepository.findById(savedGear.getManageId())
                .orElseThrow(NullPointerException::new);

        assertThat(foundGear.getGearId()).isEqualTo(gear.getGearId());
        assertThat(foundGear.getResourceState()).isEqualTo(gear.getResourceState());
        assertThat(foundGear.getName()).isEqualTo(gear.getName());
        assertThat(foundGear.getPrimaryFlag()).isEqualTo(gear.getPrimaryFlag());
        assertThat(foundGear.getDistance()).isEqualTo(gear.getDistance());
        assertThat(foundGear.getConvertedDistance()).isEqualTo(gear.getConvertedDistance());
        assertThat(foundGear.getAthlete()).isEqualTo(athlete);
    }

    @Test
    public void findAllByAthleteId() {
        //given
        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(9485L)
                .userName("dpTablo")
                .build();

        Gear gear1 = Gear.builder()
                .gearId("b22")
                .resourceState(2)
                .name("2021 MERIDA REACTO")
                .primaryFlag(true)
                .distance(3889259f)
                .convertedDistance(3889.3f)
                .athlete(athlete)
                .build();

        Gear gear2 = Gear.builder()
                .gearId("b22334")
                .resourceState(2)
                .name("2021 BMC")
                .primaryFlag(true)
                .distance(2089259f)
                .convertedDistance(2889.3f)
                .athlete(athlete)
                .build();

        //when
        gearRepository.save(gear1);
        gearRepository.save(gear2);

        //then
        List<Gear> gearList = gearRepository.findAllByAthlete_AthleteId(athlete.getAthleteId());
        assertThat(gearList.size()).isEqualTo(2);
        assertThat(gearList.get(0).getAthlete().getAthleteId()).isEqualTo(athlete.getAthleteId());
        assertThat(gearList.get(0)).isEqualTo(gear1);
        assertThat(gearList.get(1).getAthlete().getAthleteId()).isEqualTo(athlete.getAthleteId());
        assertThat(gearList.get(1)).isEqualTo(gear2);
    }

    @Test
    public void findByGearId() {
        //given
        StravaAthlete athlete = StravaAthlete.builder()
                .athleteId(9485L)
                .userName("dpTablo")
                .build();

        Gear gear = Gear.builder()
                .gearId("b22")
                .resourceState(2)
                .name("2021 MERIDA REACTO")
                .primaryFlag(true)
                .distance(3889259f)
                .convertedDistance(3889.3f)
                .athlete(athlete)
                .build();

        //when
        gearRepository.save(gear);
        Gear foundGear = gearRepository.findByGearIdAndAthleteId(gear.getGearId(), gear.getAthlete().getAthleteId())
                .orElseThrow(NullPointerException::new);

        //then
        assertThat(foundGear).isEqualTo(gear);
    }
}