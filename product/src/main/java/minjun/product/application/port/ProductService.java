package minjun.product.application.port;

import lombok.RequiredArgsConstructor;
import minjun.product.application.port.in.GetProductQuery;
import minjun.product.domain.Product;
import minjun.product.application.port.out.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ProductService implements GetProductQuery {

  private final ProductRepository productRepository;

  @Override
  public Product getProduct(Long productId) {
    return productRepository.findById(productId)
        .orElseThrow(NoSuchElementException::new);
  }
}
