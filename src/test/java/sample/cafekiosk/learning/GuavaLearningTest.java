package sample.cafekiosk.learning;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GuavaLearningTest {

    @Test
    @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
    void partitionLearningTest1() {
        //given
        final List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        //when
        final List<List<Integer>> partition = Lists.partition(integers, 3);

        //then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                        List.of(1, 2, 3),
                        List.of(4, 5, 6)
                ));
    }

    @Test
    @DisplayName("주어진 개수만큼 List를 파티셔닝한다.")
    void partitionLearningTest2() {
        //given
        final List<Integer> integers = List.of(1, 2, 3, 4, 5, 6);

        //when
        final List<List<Integer>> partition = Lists.partition(integers, 4);

        //then
        assertThat(partition).hasSize(2)
                .isEqualTo(List.of(
                        List.of(1, 2, 3, 4),
                        List.of(5, 6)
                ));
    }

    @Test
    @DisplayName("멀티맵 기능 확인")
    void multimapLearningTest() {
        //given
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "카푸치노");
        multimap.put("베이커리", "크루아상");
        multimap.put("베이커리", "식빵");

        //when
        final Collection<String> 커피 = multimap.get("커피");
        final Collection<String> 베이커리 = multimap.get("베이커리");

        //then
        assertThat(커피).hasSize(3)
                .containsExactly("아메리카노", "카페라떼", "카푸치노");

        assertThat(베이커리).hasSize(2)
                .containsExactly("크루아상", "식빵");
    }

    @TestFactory
    @DisplayName("멀티맵 기능 확인")
    Collection<DynamicTest> multimapLearningTest2() {

        //given
        Multimap<String, String> multimap = ArrayListMultimap.create();
        multimap.put("커피", "아메리카노");
        multimap.put("커피", "카페라떼");
        multimap.put("커피", "카푸치노");
        multimap.put("베이커리", "크루아상");
        multimap.put("베이커리", "식빵");

        return List.of(
                DynamicTest.dynamicTest("1개 value 삭제", () ->
                {
                    // when
                    multimap.remove("커피", "아메리카노");

                    // then
                    final Collection<String> 커피 = multimap.get("커피");
                    assertThat(커피).hasSize(2)
                            .containsExactly("카페라떼", "카푸치노");
                }),
                DynamicTest.dynamicTest("key에 해당하는 모든 value 삭제", () ->
                {
                    // when
                    multimap.removeAll("커피");

                    // then
                    final Collection<String> 커피 = multimap.get("커피");
                    assertThat(커피).isEmpty();
                })
        );

    }
}
