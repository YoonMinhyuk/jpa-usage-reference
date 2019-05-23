package me.jpa.usageref.dto;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-23
 */
@Getter
@ToString
public class MemberItem {
    private Long id;
    private String name;

    public MemberItem(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
