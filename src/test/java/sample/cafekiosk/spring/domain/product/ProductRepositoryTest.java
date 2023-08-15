package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import sample.cafekiosk.spring.IntegrationTestSupport;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

class ProductRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    @Test
    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    void findAllBySellingStatusIn() {
        //given
        final Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4500);
        final Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        final Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        final List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @Transactional
    @Test
    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    void findAllByProductNumberIn() {
        //given
        final Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4500);
        final Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        final Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        final List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus")
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @Transactional
    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    void findLatestProductNumber() {
        //given
        final String targetProductNumber = "003";

        final Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4500);
        final Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        final Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        final String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @Test
    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    void findLatestProductNumberWhenProductIsEmpty() {
        //when
        final String latestProductNumber = productRepository.findLatestProductNumber();

        //then
        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(final String productNumber, final ProductType type, final ProductSellingStatus sellingStatus, final String name, final int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(sellingStatus)
                .name(name)
                .price(price)
                .build();
    }

}
