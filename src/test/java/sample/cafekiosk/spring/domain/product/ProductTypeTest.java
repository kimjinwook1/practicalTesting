package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTypeTest {

    @Test
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    void containsStockType() {
        //given
        final ProductType givenType = ProductType.HANDMADE;

        //when
        final boolean result = ProductType.containsStockType(givenType);

        //then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("상품 타입이 재고 관련 타입인지를 체크한다.")
    void containsStockType2() {
        //given
        final ProductType givenType = ProductType.BAKERY;

        //when
        final boolean result = ProductType.containsStockType(givenType);

        //then
        assertThat(result).isTrue();
    }

}
