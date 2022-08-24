package minjun.product.application.port.in;

import minjun.product.domain.Product;

public interface GetProductQuery {

  Product getProduct(Long productId);
}
