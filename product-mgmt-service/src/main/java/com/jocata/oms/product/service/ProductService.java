package com.jocata.oms.product.service;

import com.jocata.oms.bean.ProductBean;

public interface ProductService  {

    ProductBean saveProduct(ProductBean productBean);

    ProductBean getProductById(Integer productId);

    ProductBean updateProduct(ProductBean productBean);

    String deleteProduct(Integer productId);


}
