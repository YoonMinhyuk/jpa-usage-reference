package me.jpa.usageref.domain;

import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-23
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Builder
    public Product(String name) {
        Assert.notNull(name, "name cannot be null");
        this.name = name;
    }
}
