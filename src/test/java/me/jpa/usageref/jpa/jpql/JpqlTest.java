package me.jpa.usageref.jpa.jpql;

import me.jpa.usageref.common.TestDescription;
import me.jpa.usageref.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-22
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class JpqlTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testEntityManagerIsNotNull() {
        //Given When Then
        assertThat(entityManager).isNotNull();
    }

    @Test
    @TestDescription("JPQL 을 이용한 간단한 조회 테스트")
    public void 이름이_minhyuk_인_사람을_조회한다() {
        //Given
        final String jpql = "select m from Member m where m.name ='minhyuk'";
        String name = "minhyuk";
        Member minhyuk = Member.builder().name(name).age(28).build();
        entityManager.persist(minhyuk);
        entityManager.flush();
        entityManager.clear();

        //When
        Member member = entityManager.createQuery(jpql, Member.class).getSingleResult();

        //Then
        assertThat(member).isNotNull();
        assertThat(member.getName()).isEqualTo(name);
    }


}
