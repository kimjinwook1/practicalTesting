package sample.cafekiosk.spring.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o " +
            "where o.registeredDateTime >= :startDateTime " +
            "and o.registeredDateTime < :endDateTime " +
            "and o.orderStatus = :orderStatus")
    List<Order> findOrdersBy(@Param("startDateTime") final LocalDateTime startDateTime,
                             @Param("endDateTime") final LocalDateTime endDateTime,
                             @Param("orderStatus") final OrderStatus orderStatus);
}
