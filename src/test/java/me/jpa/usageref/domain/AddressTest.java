package me.jpa.usageref.domain;

import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-22
 */
public class AddressTest {

    @Test
    public void city_와_street_이_모두_동일할_경우_동등해야한다() {
        //Given
        Address firstAddress = createAddress();
        Address secondAddress = createAddress();

        //When
        boolean equals = firstAddress.equals(secondAddress);

        //Then
        assertThat(equals).isTrue();
    }

    private Address createAddress(String city, String street) {
        return Address.builder().city(city).street(street).build();
    }

    private Address createAddress() {
        return this.createAddress("seoul", "city");
    }

    @Test
    public void city_는_같고_street_이_다를_경우_동등하지_않아야한다() {
        //Given
        String city = "seoul";
        String firstStreet = "street1";
        String secondStreet = "street2";

        Address firstAddress = createAddress(city, firstStreet);
        Address secondAddress = createAddress(city, secondStreet);

        //When
        boolean equals = firstAddress.equals(secondAddress);

        //Then
        assertThat(equals).isFalse();
    }

    @Test
    public void street_은_같고_city_가_다를_경우_동등하지_않아야한다() {
        //Given
        String seoul = "seoul";
        String busan = "busan";
        String street = "street";

        Address firstAddress = createAddress(seoul, street);
        Address secondAddress = createAddress(busan, street);

        //When
        boolean equals = firstAddress.equals(secondAddress);

        //Then
        assertThat(equals).isFalse();
    }

    @Test
    public void city와_street_이_모두_다를_경우_동등하지_않아야한다() {
        //Given
        Address firstAddress = createAddress("city1", "street1");
        Address secondAddress = createAddress("city2", "street2");

        //When
        boolean equals = firstAddress.equals(secondAddress);

        //Then
        assertThat(equals).isFalse();
    }

}
