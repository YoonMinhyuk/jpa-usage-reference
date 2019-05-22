package me.jpa.usageref.domain;

import org.junit.Test;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-23
 */
public class OrderTest {

    @Test(expected = IllegalArgumentException.class)
    public void member_가_null_이면_예외가_발생해야한다() {
        //Given When Then
        createOrder(null, createBasicProduct());
    }

    private Member createBasicMember() {
        return Member.builder().name("member").age(10).build();
    }

    private Product createBasicProduct() {
        return Product.builder().name("productA").build();
    }

    private Order createOrder(Member member, Product product) {
        return Order.builder().member(member).product(product).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void product_가_null_이면_예외가_발생해야한다() {
        //Given When Then
        createOrder(createBasicMember(), null);
    }
}
