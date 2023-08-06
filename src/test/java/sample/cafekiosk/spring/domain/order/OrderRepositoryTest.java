package sample.cafekiosk.spring.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import sample.cafekiosk.spring.domain.product.Product;
import sample.cafekiosk.spring.domain.product.ProductRepository;
import sample.cafekiosk.spring.domain.product.ProductType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.SELLING;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test")
@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductRepository productRepository;

    @Test
    @DisplayName("주문 등록날짜와 주문 상태를 가지고 주문을 조회할 수 있다.")
    void findOrdersBy() {
        //given
        final LocalDateTime registeredDateTime = LocalDateTime.now();

        final Product product1 = createProduct(HANDMADE, "001", 1000);
        final Product product2 = createProduct(HANDMADE, "002", 3000);
        final Product product3 = createProduct(HANDMADE, "003", 5000);

        final List<Product> products = List.of(product1, product2, product3);

        productRepository.saveAll(products);
        orderRepository.save(createOrder(products, OrderStatus.PAYMENT_COMPLETED, registeredDateTime));

        final LocalDate now = LocalDate.now();
        final LocalDateTime startDateTime = now.atStartOfDay();
        final LocalDateTime endDateTime = now.plusDays(1).atStartOfDay();
        final OrderStatus orderStatus = OrderStatus.PAYMENT_COMPLETED;

        //when
        final List<Order> orders = orderRepository.findOrdersBy(startDateTime, endDateTime, orderStatus);

        //then
        assertThat(orders).hasSize(1)
                .extracting("orderStatus")
                .contains(OrderStatus.PAYMENT_COMPLETED);

        assertThat(orders)
                .allMatch(order -> order.getRegisteredDateTime().isAfter(startDateTime)
                        && order.getRegisteredDateTime().isBefore(endDateTime));
    }

    private Order createOrder(final List<Product> products, final OrderStatus orderStatus, final LocalDateTime registeredDateTime) {
        return Order.builder()
                .products(products)
                .orderStatus(orderStatus)
                .registeredDateTime(registeredDateTime)
                .build();
    }

    private Product createProduct(ProductType type, String productNumber, int price) {
        return Product.builder()
                .type(type)
                .productNumber(productNumber)
                .price(price)
                .sellingStatus(SELLING)
                .name("메뉴이름")
                .build();
    }

}
