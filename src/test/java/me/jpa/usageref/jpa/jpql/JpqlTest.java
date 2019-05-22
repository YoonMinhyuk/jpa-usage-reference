package me.jpa.usageref.jpa.jpql;

import me.jpa.usageref.common.Description;
import me.jpa.usageref.domain.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.*;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Minhyuk Yoon (ymh92730@gmail.com)
 * @since 2019-05-22
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@Description("JPQL 은 Entity 의 이름과 이에 대한 별칭을 주어 query 를 작성해야한다.")
public class JpqlTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testEntityManagerIsNotNull() {
        //Given When Then
        assertThat(entityManager).isNotNull();
    }

    @Test
    @Description({
            "JPQL 을 이용한 간단한 조회 테스트.",
            "getSingleResult() 메소드는 조회하는 데이터가 존재하지 않을 시 NoResultException 예외를 던지기 때문에 적절하게 사용."
    })
    public void 이름이_minhyuk_인_사람을_조회한다() {
        //Given
        final String jpql = "select m from Member m where m.name ='minhyuk'";
        String name = "minhyuk";
        Member minhyuk = createMember(name, 28);
        entityManager.persist(minhyuk);
        entityManager.flush();
        entityManager.clear();

        //When
        Member member = entityManager.createQuery(jpql, Member.class).getSingleResult();

        //Then
        assertThat(member).isNotNull();
        assertThat(member.getName()).isEqualTo(name);
    }

    private Member createMember(String name, int age) {
        return Member.builder().name(name).age(age).build();
    }

    @Test(expected = NoResultException.class)
    @Description({
            "조회하고자 하는 대상 데이터가 존재하지 않을 경우 getSingleResult() 메소드가 NoResultException 예외를 발생시키는지에 대한 테스트",
            "getSingleResult() 메소드는 조회하는 데이터가 존재하지 않을 시 NoResultException 예외를 던지기 때문에 적절하게 사용."
    })
    public void 조회하는_대상이_존재하지_않을_경우_getSingleResult_메소드는_NoResultException_예외를_발생시켜야_한다() {
        //Given
        final String jpql = "select m from Member m where m.name ='minhyuk'";

        //When Then
        entityManager.createQuery(jpql, Member.class).getSingleResult();
    }

    @Test
    @Description({
            "createQuery(..) 메소드의 2번째 인자로 Return 받고자 하는 타입을 주게 되면 결과는 TypedQuery<T> 타입의 객체가 Return 되어야한다.",
            "createQuery(..) 메소드의 2번째 인자로 Return 받고자하는 타입을 명시해주지 않으면 Query 타입의 객체를 반환한다.",
            "또한 query.getSingleResult() 호출시 Object Type 의 결과를 Return 하며 ",
            "query.getResultList() 호출시 List Type (Type Argument 가 없는. 즉, List<T> 의 형태가 아닌 List 의 형태)의 결과를 Return 한다."
    })
    public void createQuery_메소드의_2번째_argument_로_클래스타입을_주게_되면_TypedQuery_타입의_객체가_Return_되어야한다() {
        //Given
        final String jpql = "select m from Member m where m.name = 'minhyuk'";

        String name = "minhyuk";
        Member member = createMember(name, 28);

        entityManager.persist(member);
        entityManager.flush();
        entityManager.clear();

        //When
        TypedQuery<Member> memberTypedQuery = entityManager.createQuery(jpql, Member.class);

        //Then
        assertThat(memberTypedQuery).isNotNull();
        assertThat(memberTypedQuery.getSingleResult().getName()).isEqualTo(name);
    }

    @Test
    @Description({
            "createQuery(..) 의 2번째 인자로 Return 받을 타입을 명시하지 않았을 경우 Query 타입의 객체를 Return 해야한다.",
            "createQuery(..) 메소드의 2번째 인자로 Return 받고자하는 타입을 명시해주지 않으면 Query 타입의 객체를 반환한다.",
            "또한 query.getSingleResult() 호출시 Object Type 의 결과를 Return 하며 ",
            "query.getResultList() 호출시 List Type (Type Argument 가 없는. 즉, List<T> 의 형태가 아닌 List 의 형태)의 결과를 Return 한다."
    })
    public void createQuery_메소드의_2번째_인자로_Return_받고자하는_타입을_명시하지않을_경우_테스트() {
        // Given
        final String jpql = "select m from Member m where m.name = 'minhyuk'";
        String name = "minhyuk";
        entityManager.persist(createMember(name, 28));
        entityManager.flush();
        entityManager.clear();

        //When
        Query query = entityManager.createQuery(jpql);
        Object memberObj = query.getSingleResult();
        List members = query.getResultList();

        //Then
        assertThat(query).isNotNull();
        assertThat(memberObj).isNotNull();
        assertThat(memberObj instanceof Member).isTrue();
        assertThat(members.size()).isOne();
        assertThat(members.get(0) instanceof Member).isTrue();
    }

    @Test
    @Description({
            "select age, name from member where name='minhyuk' 과 같은 SQL 을 JPQL 로 작성하여 결과를 확인한다고 생각하면 된다.",
            "이 경우 반환되는 값이 String & Integer 이기 때문에 하나의 타입으로 정의할 수 없다.",
            "따라서 createQuery(..) 메소드의 2번째 인자로 타입을 정해줄 수 없기에 이 경우 Query Type 을 반환받을 수 밖에 된다.",
            "그리고 이 경우 query.getSingleResult() 호출시 Object 타입의 결과를 반환하며",
            "query.getResultList() 메소드의 경우 Type Argument 가 존재하지 않는 List 의 결과를 Return 한다.",
            "그리고 사실 반환된 Object 는 Object[] 형태이기 때문에 형변환을 해서 사용해야한다."
    })
    public void projection_을_이용해_타입이_다른_값을_조회할_경우_Query_타입을_반환해야한다() {
        // Given
        final String jpql = "select m.name, m.age from Member m where m.name='minhyuk'";

        String name = "minhyuk";
        int age = 28;
        Member member = createMember(name, age);

        entityManager.persist(member);
        entityManager.flush();
        entityManager.clear();

        //When
        Query query = entityManager.createQuery(jpql);
        Object obj = query.getSingleResult();
        List resultList = query.getResultList();

        //Then
        assertThat(obj).isNotNull();
        assertThat(obj instanceof Object[]).isTrue();
        Object[] objArr = (Object[]) obj;
        assertThat(objArr[0]).isEqualTo(name);
        assertThat(objArr[1]).isSameAs(age);

        assertThat(resultList).isNotNull();
        assertThat(resultList.size()).isOne();
        assertThat(resultList.get(0) instanceof Object[]).isTrue();
        resultList.forEach(o -> {
            Object[] objects = (Object[]) o;
            assertThat(objects[0]).isEqualTo(name);
            assertThat(objects[1]).isSameAs(age);
        });
    }
}
