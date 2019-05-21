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
}
