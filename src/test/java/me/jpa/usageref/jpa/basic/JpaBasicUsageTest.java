package me.jpa.usageref.jpa.basic;

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
 * @since 2019-05-21
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class JpaBasicUsageTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @TestDescription("비영속 상태 테스트")
    public void testNotPersistence() {
        /**
         * 비영속 : PeristenceContext 에 관리되지 않는 상태. 순수한 자바 객체 상태이다. (식별자 존재 X)
         * */
        //Given
        Member member = createMember();

        //When Then
        assertThat(entityManager.contains(member)).isFalse();
    }

    private Member createMember() {
        return Member.builder().name("memberA").build();
    }

    @Test
    @TestDescription(value = "영속 상태 테스트")
    public void testPersistence() {
        /**
         * 영속 : PersistenceContext 에 관리되는 상태. 1차 캐시에 존재한다. (식별자 존재)
         * */
        //Given
        Member member = createMember();

        //When
        entityManager.persist(member);
        Member savedMember = entityManager.find(Member.class, member.getId());

        //Then
        assertThat(savedMember).isNotNull();
        assertThat(entityManager.contains(member)).isTrue();
    }

    @Test
    @TestDescription("준영속 상태 테스트")
    public void testDetach() {
        /**
         * 준영속 : PersistenceContext 에 의해 관리되어지다가 특정한 이유로 인해 더 이상 관리되지 않는 상태 (식별자 존재)
         *
         * 영속상태의 객체를 준영속 상태로 만드는 방법
         * 1. EntityManager 의 detach(..) 메소드 호출 => 특정 객체 준영속화
         * 2. EntityManager 의 clear() 메소드 호출    => PersistenceContext가 관리하던 모든 영속 객체 준영속화
         * 3. EntityManager 의 close() 메소드 호출    => PersistenceContext가 종료되어 모든 영속 객체 준영속화
         */
        // Given
        Member member = createMember();

        //When Then
        entityManager.persist(member);
        assertThat(entityManager.contains(member)).isTrue();

        entityManager.detach(member);
        assertThat(entityManager.contains(member)).isFalse();
    }

    @Test
    @TestDescription("삭제 상태 테스트")
    public void testRemove() {
        /**
         * 삭제 상태 : PersistenceContext 에 존재하다가 제거되어진 상태. 준영속 상태와 마찬가지로 PersistenceContext 에 의해 관리되어지지 않는다.
         **/

        //Given
        Member member = createMember();

        //When Then
        entityManager.persist(member);
        assertThat(entityManager.contains(member)).isTrue();

        entityManager.remove(member);
        assertThat(entityManager.contains(member)).isFalse();
    }

    @Test
    @TestDescription("변경 감지 (Dirty Checking) Test")
    public void testDirtyChecking() {
        /**
         * Dirty Checking : 영속화 상태의 엔티티의 변경을 감지하여 트랜잭션 커밋 전 변경을 반영하는 것
         * */
        //Given
        String newName = "newName";
        Member member = createMember();
        entityManager.persist(member);

        //When
        member.changeName(newName);
        entityManager.flush();
        entityManager.clear();

        Member selectedMember = entityManager.find(Member.class, member.getId());

        //Then
        assertThat(selectedMember.getName()).isEqualTo(newName);
    }

    @Test
    @TestDescription("엔티티가 준영속 상태일 경우 병합 (merge) 테스트")
    public void testMergeWhenEntityIsDetach() {
        /**
         * [ Merge ]
         * 1. 기능 : 준영속 상태의 Entity 를 영속 상태로 만든다. & 준영속 상태의 Entity 를 받아서 그 정보로 새로운 영속 상태(내부적으로 새로운 객체를 생성해서 1차 캐시에 담아둠)의 Entity 를 반환한다.
         *         ( DetachedEntity != MergedEntity )
         *
         * 2. Merge(..)는 Entity에 식별자 값이 존재하면 Update         수행
         *
         * 3. Merge(..)는 Entity애 식별자 값이 존재하지 않으면 Save 를   수행
         *
         * 4. 순서
         *     - 넘어온 식별자 값으로 1차 캐시 먼저 조회 => 존재하면 1차 캐시에 있는 Entity Return. 없으면 DB 조회
         *     - DB에서도 조회할 수 없으면 새로운 엔티티를 생성해서 병합
         *
         * 5. 결론 :
         *     - merge(..)는 준영속, 비영속을 신경쓰지 않는다.
         *     - 식별자 값으로 Entity 를 조회할 수 있으면 불러서 병합.
         *     - 조회할 수 없으면 새로 생성해서 병합.
         **/

        //Given
        Member member = createMember();

        entityManager.persist(member);
        entityManager.flush();

        entityManager.detach(member);

        //When
        Member mergedMember = entityManager.merge(member);

        //Then
        assertThat(member).isNotSameAs(mergedMember);
        assertThat(entityManager.contains(member)).isFalse();
        assertThat(entityManager.contains(mergedMember)).isTrue();
    }

    @Test
    @TestDescription("엔티티가 비영속 상태일 경우 병합 (merge) 테스트")
    public void testMergeWhenEntityIsNotPersistence() {
        //Given
        Member member = createMember();

        //When
        Member mergedMember = entityManager.merge(member);

        //Then
        assertThat(member).isNotSameAs(mergedMember);
        assertThat(entityManager.contains(member)).isFalse();
        assertThat(entityManager.contains(mergedMember)).isTrue();
    }
}
