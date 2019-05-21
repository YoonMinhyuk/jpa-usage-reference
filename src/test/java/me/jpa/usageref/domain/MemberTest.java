package me.jpa.usageref.domain;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-21
 */
public class MemberTest {

    @Test(expected = IllegalArgumentException.class)
    public void 객체생성시_age_가_0보다_작을_경우_예외가_발생해야한다() {
        //Given When Then
        createMember(1L, -1, "memberA");
    }

    private Member createMember(Long id, int age, String name) {
        return Member.builder().id(id).age(age).name(name).build();
    }

    private Member createMember() {
        return this.createMember(1L, 10, "memberA");
    }

    @Test(expected = IllegalArgumentException.class)
    public void 객체생성시_name_이_null_이면_예외가_발생해야한다() {
        //Given When Then
        createMember(1L, 10, null);
    }

    @Test
    public void id_가_같으면_동등해야한다() {
        //Given
        Member memberA = createMember(1L, 10, "memberA");
        Member memberB = createMember(1L, 20, "memberB");

        //When
        boolean equals = memberA.equals(memberB);

        //Then
        assertThat(equals).isTrue();
    }

    @Test
    public void id_가_다를_경우_동등하지않아야_한다() {
        //Given
        Member memberA = createMember(1L, 10, "memberA");
        Member memberB = createMember(2L, 10, "memberA");

        //When
        boolean equals = memberA.equals(memberB);

        //Then
        assertThat(equals).isFalse();
    }

    @Test
    public void 팀에_가입되어있지_않은_경우_팀에_가입해야한다() {
        //Given
        Member member = createMember();
        Team team = createTeam();

        //When
        member.joinTeam(team);

        //Then
        assertThat(member.getTeam()).isNotNull();
        assertThat(member.getTeam().getName()).isEqualTo(team.getName());
    }

    private Team createTeam(Long id, String name) {
        return Team.builder().id(id).name(name).build();
    }

    private Team createTeam() {
        return Team.builder().id(1L).name("TeamA").build();
    }

    @Test(expected = RuntimeException.class)
    public void 이미_팀에_가입되어있는_상태에서_다른_팀에_가입할_경우_예외가_발생해야한다() {
        //Given
        Member member = createMember();
        Team teamA = createTeam(1L, "TeamA");
        Team teamB = createTeam(2L, "TeamB");

        member.joinTeam(teamA);

        //When Then
        member.joinTeam(teamB);
    }

    @Test(expected = RuntimeException.class)
    public void 동일한_팀으로_가입하려고_하는_경우_예외가_발생해야한다() {
        //Given
        Member member = createMember();
        Team team = createTeam();

        //When Then
        member.joinTeam(team);
        member.joinTeam(team);
    }
}
