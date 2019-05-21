package me.jpa.usageref.domain;

import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-21
 */
public class TeamTest {

    @Test
    public void id_와_name_이_동일할_경우_동등해야한다() {
        //Given
        Team firstTeam = createTeam();
        Team secondTeam = createTeam();

        //When
        boolean equals = firstTeam.equals(secondTeam);

        //Then
        assertThat(equals).isTrue();
    }

    private Team createTeam(Long id, String name) {
        return Team.builder().id(id).name(name).build();
    }

    private Team createTeam() {
        return this.createTeam(1L, "TeamA");
    }

    @Test
    public void id는_같고_name_은_다를_경우_동등하지_않아야_한다() {
        //Given
        Team teamA = createTeam(1L, "TeamA");
        Team teamB = createTeam(1L, "TeamB");

        //When
        boolean equals = teamA.equals(teamB);

        //Then
        assertThat(equals).isFalse();
    }

    @Test
    public void id가_다르고_name_이_같을_경우_동등하지_않아야한다() {
        //Given
        Team firstTeam = createTeam(1L, "TeamA");
        Team secondTeam = createTeam(2L, "TeamA");

        //When
        boolean equals = firstTeam.equals(secondTeam);

        //Then
        assertThat(equals).isFalse();
    }

    @Test(expected = IllegalArgumentException.class)
    public void 객체생성시_name_이_null_일_경우_예외가_발생해야한다() {
        //Given When Then
        createTeam(1L, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void 객체생성시_name_이_비어있을_경우_예외가_발생해야한다() {
        //Given When Then
        createTeam(1L, "");
    }

    @Test
    public void temp() {
        String packageName = "me.test.tes";
        boolean matches = packageName.matches("^(me)");
        System.out.println(matches);
    }
}
