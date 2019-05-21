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
}
