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
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn
    private Member member;

    @OneToOne
    @JoinColumn
    private Product product;

    @Builder
    public Orders(Member member, Product product) {
        this.initializeMember(member);
        this.initializeProduct(product);
    }

    private void initializeMember(Member member) {
        Assert.notNull(member, "member cannot be null");
        this.member = member;
    }

    private void initializeProduct(Product product) {
        Assert.notNull(product, "product cannot be null");
        this.product = product;
    }

}
