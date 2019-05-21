package me.jpa.usageref.domain;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-21
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn
    private Team team;

    @Builder
    public Member(Long id, int age, String name) {
        this.id = id;
        validateAndSetAge(age);
        checkNullAndSetName(name);
    }

    private void validateAndSetAge(int age) {
        if (age < 0) throw new IllegalArgumentException("age cannot less than zero");
        this.age = age;
    }

    private void checkNullAndSetName(String name) {
        Assert.notNull(name, "name cannot be null");
        this.name = name;
    }

    public void joinTeam(Team team) {
        verifyThatAlreadyJoinedTeam(team);
        checkSameTeam(team);
        this.team = team;
    }

    private void verifyThatAlreadyJoinedTeam(Team team) {
        if (this.team != null && !this.team.equals(team)) throw new RuntimeException("이미 팀에 가입되어있습니다.");
    }

    private void checkSameTeam(Team team) {
        if (this.team != null && this.team.equals(team)) throw new RuntimeException("동일한 팀입니다.");
    }
}