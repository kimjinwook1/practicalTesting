package sample.cafekiosk.spring.api.service.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.IntegrationTestSupport;
import sample.cafekiosk.spring.api.controller.product.dto.request.ProductCreateRequest;
import sample.cafekiosk.spring.api.service.product.request.ProductServiceCreateRequest;
import sample.cafekiosk.spring.api.service.product.response.ProductResponse;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductSellingStatus;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

class ProductServiceTest extends IntegrationTestSupport {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("신규 상품을 등록한다. 상품번호는 가장 최근 상품번호에서 1 증가한 값이다.")
    void createProduct() {
        //given
        final Product product = createProduct("001", HANDMADE, SELLING, "아메리카노", 4500);
        productRepository.save(product);

        final ProductServiceCreateRequest request = ProductServiceCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        //when
        final ProductResponse productResponse = productService.createProduct(request);


        //then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("002", HANDMADE, SELLING, "카푸치노", 5000);

        final List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .containsExactlyInAnyOrder(
                        tuple("001", HANDMADE, SELLING, "아메리카노", 4500),
                        tuple("002", HANDMADE, SELLING, "카푸치노", 5000)
                );
    }

    @Test
    @DisplayName("상품이 하나도 없는 경우 신규 상품을 등록하면 상품 번호는 001이다")
    void createProductWhenProductIsEmpty() {
        //given
        final ProductServiceCreateRequest request = ProductServiceCreateRequest.builder()
                .type(HANDMADE)
                .sellingStatus(SELLING)
                .name("카푸치노")
                .price(5000)
                .build();

        //when
        final ProductResponse productResponse = productService.createProduct(request);

        //then
        assertThat(productResponse)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains("001", HANDMADE, SELLING, "카푸치노", 5000);

        final List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(1)
                .extracting("productNumber", "type", "sellingStatus", "name", "price")
                .contains(tuple("001", HANDMADE, SELLING, "카푸치노", 5000));
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
