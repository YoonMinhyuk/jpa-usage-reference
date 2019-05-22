package me.jpa.usageref.domain;

import org.junit.Test;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-23
 */
public class ProductTest {
    @Test(expected = IllegalArgumentException.class)
    public void name_이_null_이면_예외가_발생해야한다() {
        //Given When Then
        Product.builder().name(null).build();
    }
}
