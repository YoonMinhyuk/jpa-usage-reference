package me.jpa.usageref.domain;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-21
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id", "name"})
@ToString
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany
    private List<Member> members = new ArrayList<>();

    @Builder
    public Team(Long id, String name) {
        this.id = id;
        this.validateAndSetName(name);
    }

    private void validateAndSetName(String name) {
        validateName(name);
        this.name = name;
    }

    private void validateName(String name) {
        Assert.notNull(name, "name cannot be null");
        Assert.hasText(name, "name cannot be empty");
    }
}
