package minjun.ddd.product.application.port.out;

import minjun.ddd.product.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
