package me.jpa.usageref.jpa.jpql;

import me.jpa.usageref.common.Description;
import me.jpa.usageref.domain.Address;
import me.jpa.usageref.domain.Member;
import me.jpa.usageref.domain.Orders;
import me.jpa.usageref.domain.Product;
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
@Description({
        "JPQL 은 Entity 의 이름과 이에 대한 별칭을 주어 query 를 작성해야한다.",
        "JPQL 이용시 자동으로 flush 가 발생한다. 기본적으로는 트랜잭션 커밋시에 자동으로 발샹."
})
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
            "getSingleResult() 메소드는 조회하는 데이터가 존재하지 않을 시 javax.persistence.NoResultException 예외를 던지며",
            "결과가 2개 이상일 시 javax.persistence.NonUniqueResultException 예외를 발생시키기 때문에",
            "적절하게 잘! 사용한다."
    })
    public void 이름이_minhyuk_인_사람을_조회한다() {
        //Given
        final String jpql = "select m from Member m where m.name ='minhyuk'";
        String name = "minhyuk";
        Member minhyuk = createMember(name, 28);
        entityManager.persist(minhyuk);

        //When
        Member member = entityManager.createQuery(jpql, Member.class).getSingleResult();

        //Then
        assertThat(member).isNotNull();
        assertThat(member.getName()).isEqualTo(name);
    }

    private Member createMember(String name, int age, Address address) {
        return Member.builder().name(name).age(age).address(address).build();
    }

    private Member createMember(String name, int age) {
        return this.createMember(name, age, null);
    }

    @Test(expected = NoResultException.class)
    @Description({
            "조회하고자 하는 대상 데이터가 존재하지 않을 경우 getSingleResult() 메소드가 NoResultException 예외를 발생시키는지에 대한 테스트",
            "getSingleResult() 메소드는 조회하는 데이터가 존재하지 않을 시 javax.persistence.NoResultException 예외를 던지며",
            "결과가 2개 이상일 시 javax.persistence.NonUniqueResultException 예외를 발생시키기 때문에",
            "적절하게 잘! 사용한다."
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
            "그리고 사실 반환된 Object 는 Object[] 형태이기 때문에 형변환을 해서 사용해야한다.",
            "배열의 각 인덱스에 저장되어 있는 값은 JQPL projection 시 입력했던 필드 값 순서대로 이다."
    })
    public void projection_을_이용해_타입이_다른_값을_조회할_경우_Query_타입을_반환해야한다() {
        // Given
        final String jpql = "select m.name, m.age from Member m where m.name='minhyuk'";

        String name = "minhyuk";
        int age = 28;
        Member member = createMember(name, age);

        entityManager.persist(member);

        //When
        Query query = entityManager.createQuery(jpql);
        Object obj = query.getSingleResult();
        List resultList = query.getResultList();

        //Then
        assertThat(obj).isNotNull();
        assertThat(obj instanceof Object[]).isTrue();
        Object[] objArr = (Object[]) obj;

        //JPQL 에서 보면 select m.name, m.age ~ 로 시작되며 처음 조회하는 필드는 name 이기 때문에 배열의 0번째는 name 이, 1번째에는 age 가 들어가 있다.
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

    @Test
    @Description({
            "Binding 되어지는 Parameter 를 이름으로 구분하는 방법이다.",
            "이름 기준 파라미터 앞에는 : (콜론) 을 사용하며 : 뒤에 오는 이름이 파라미터 이름이다. 예):name -> name 이 파라미터 이름",
            "아래의 JPQL 에서 where m.name = :name 처럼 말이다."
    })
    public void 이름_기준_파라미터_바인딩_테스트() {
        //Given
        final String jpql = "select m from Member m where m.name=:name";
        String name = "minhyuk";
        int age = 28;
        Member member = createMember(name, age);

        entityManager.persist(member);

        //when
        Member selectedMember = entityManager.createQuery(jpql, Member.class)
                .setParameter("name", name)
                .getSingleResult();

        //Then
        assertThat(selectedMember).isNotNull();
        assertThat(selectedMember.getName()).isEqualTo(name);
        assertThat(selectedMember.getAge()).isEqualTo(age);
    }

    @Test
    @Description({
            "Binding 되어지는 Parameter를 위치 기반으로 구분하는 방법이다.",
            "위치 기준 파라미터를 사용하려면 ? 다음에 위치 값을 주면 된다.",
            "아래에서 where m.name = ?1 and m.age = ?2 에서 ?1, ?2 처럼 말이다.",
            "그러나 위치 기준 파라미터 방식보다는 이름 기준 파라미 바인딩 방식을 사용하는 것이 더 명확하다."
    })
    public void 위치_기준_파라미터_바인딩_테스트() {
        //Given
        final String jpql = "select m from Member m where m.name=?1 and m.age=?2";
        String name = "minhyuk";
        int age = 28;
        Member member = createMember(name, age);

        entityManager.persist(member);

        //When
        Member selectedMember = entityManager.createQuery(jpql, Member.class)
                .setParameter(1, name)
                .setParameter(2, age)
                .getSingleResult();

        //Then
        assertThat(selectedMember).isNotNull();
        assertThat(selectedMember.getName()).isEqualTo(name);
        assertThat(selectedMember.getAge()).isEqualTo(age);
    }

    @Test
    @Description({
            "SELECT 절에서 조회할 대상을 지정하는 것을 Projection 이라 하며",
            "[ SELECT {Projection 대상} FROM ~ ] 처럼 대상을 선택한다.",
            "Projection 의 대상은 Entity, Embedded Type, Scala Type 이 있다.",
            "Scala Type 은 숫자, 문자 등 기본 데이터 타입을 뜻한다.",
            "=> 조회된 Entity 는 Persistence Context 에 의해 관리되어진다."
    })
    public void entity_projection_test() {
        //Given
        final String jpql = "select m from Member m where m.age=:age";
        String name = "minhyuk";
        int age = 28;

        entityManager.persist(createMember(name, age));

        //When
        Member selectedMember = entityManager.createQuery(jpql, Member.class)
                .setParameter("age", age)
                .getSingleResult();

        //Then
        assertThat(selectedMember).isNotNull();
        assertThat(selectedMember.getName()).isEqualTo(name);
        assertThat(selectedMember.getAge()).isEqualTo(age);
    }

    @Test
    @Description({
            "JPQL에서 Embedded Type은 엔티티와 거의 비슷하게 사용된다.",
            "그러나 Embedded Tpe 은 조회의 시작점이 될 수 없다는 제약이 존재한다.",
            "따라서 Embedded Type은 Entity 를 통해서 조회가 되어진다.",
            "Example) select a from Address a; -> 잘못된 JPQL (Address = Embedded Type)",
            "select o.address from Orders o -> 올바른 JPQL (Orders = Entity Type)",
            "=> Embedded Type은 Entity Type이 아닌 값 타입이다.",
            "따라서 조회한 Embedded Type은 Persistence Context에 의해 관리되어지지 않는다."
    })
    public void embeddedType_projection_test() {
        //Given
        final String jpql = "select m.address from Member m";
        Address address = Address.builder().city("seoul").street("street").build();
        Member member = createMember("minhyuk", 28, address);
        entityManager.persist(member);

        //When Then
        entityManager.createQuery(jpql, Address.class)
                .getResultStream()
                .forEach(selectedAddress -> {
                    assertThat(selectedAddress).isNotNull();
                    assertThat(selectedAddress.getCity()).isEqualTo(address.getCity());
                    assertThat(selectedAddress.getStreet()).isEqualTo(address.getStreet());
                });
    }

    @Test(expected = IllegalArgumentException.class)
    @Description({
            "Embedded Type 가 조회의 시작점이 되었을 때 예외가 발생하는 것을 확인하기 위한 테스트"
    })
    public void 잘못된_embeddedType_projection_test() {
        //Given
        final String jpql = "select a from Address a";
        entityManager.persist(createMember("minhyuk", 28, Address.builder().city("seoul").street("street").build()));

        //When Then
        entityManager.createQuery(jpql, Address.class).getResultList();
    }

    @Test
    @Description("숫자, 문자, 날짜와 같이 기본 데이터 타입들을 Scala Type 이라 한다.")
    public void scalaType_projection_test() {
        //Given
        Member member = createMember("minhyuk", 28);
        entityManager.persist(member);

        //When Then
        entityManager.createQuery("select m.age from Member m", Integer.class)
                .getResultList()
                .forEach(age -> assertThat(age).isEqualTo(member.getAge()));

        // avg(), sum() 등을 사용하는 쿼리도 주로 Scala Type 사용
        Double ageAverage = entityManager.createQuery("select avg(m.age) from Member m", Double.class).getSingleResult();
        assertThat(ageAverage).isNotNaN();
        assertThat(ageAverage).isNotZero();
    }

    @Test
    @Description("중복 제거시 SQL 과 같이 distinct 키워드를 사용하면 된다.")
    public void 중복_제거_테스트() {
        //Given
        Member member1 = createMember("member1", 28);
        Member member2 = createMember("member2", 28);

        entityManager.persist(member1);
        entityManager.persist(member2);

        //When
        List<Integer> ages = entityManager.createQuery("select distinct m.age from Member m where m.age=:age", Integer.class)
                .setParameter("age", 28)
                .getResultList();

        //Then
        assertThat(ages.size()).isOne();
    }

    @Test
    @Description({
            "Entity를 대상으로 조회하면 편리하겠지만 꼭! 필요한 데이터들만 선택해서 조회해야할 때도 있다.",
            "Projection에 여러 값을 선택하면 TypedQuery를 사용할 수 없다. 따라서 Query 를 사용해야한다.",
            "Entity Type 도 여러 값을 함께 조회할 수 있다.",
            "물론 이 때 조회한 모든 Entity 는 Persistence Context 에서 관리한다."
    })
    public void 다양한_값을_대상으로한_조회_테스트() {
        //Given
        Member member = createMember("minhyuk", 28);
        Product product = Product.builder().name("productA").build();
        Orders orders = Orders.builder().member(member).product(product).build();
        entityManager.persist(member);
        entityManager.persist(product);
        entityManager.persist(orders);

        //When Then
        entityManager.createQuery("select m.name, m.age from Member m", Object[].class)
                .getResultList()
                .forEach(objects -> {
                    assertThat(objects[0]).isEqualTo(member.getName());
                    assertThat(objects[1]).isEqualTo(member.getAge());
                });

        entityManager.createQuery("select o.member, o.product from Orders o where o.id=:id", Object[].class)
                .setParameter("id", orders.getId())
                .getResultStream()
                .forEach(objects -> {
                    assertThat(objects[0] instanceof Member).isTrue();
                    Member selectedMember = (Member) objects[0];
                    assertThat(selectedMember.getName()).isEqualTo(member.getName());
                    assertThat(selectedMember.getAge()).isEqualTo(member.getAge());

                    assertThat(objects[1] instanceof Product).isTrue();
                    Product selectedProduct = (Product) objects[1];
                    assertThat(selectedProduct.getName()).isEqualTo(product.getName());
                });
    }
}
