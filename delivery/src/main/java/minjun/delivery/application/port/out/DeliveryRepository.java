package minjun.delivery.application.port.out;

import java.util.Optional;
import minjun.delivery.domain.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

  Optional<Delivery> findByOrderId(Long orderId);
}
