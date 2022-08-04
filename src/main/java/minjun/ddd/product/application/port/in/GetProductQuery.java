package minjun.ddd.product.application.port.in;

import minjun.ddd.product.domain.Product;

public interface GetProductQuery {

  Product getProduct(Long productId);
}
