package me.jpa.usageref.domain;

import lombok.*;

import javax.persistence.Embeddable;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-22
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Address {
    private String city;
    private String street;
}
