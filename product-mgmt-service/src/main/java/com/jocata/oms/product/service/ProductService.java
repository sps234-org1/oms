package com.jocata.oms.product.service;

import com.jocata.oms.bean.ProductBean;

import java.util.List;

public interface ProductService  {

    ProductBean saveProduct(ProductBean productBean);

    ProductBean getProductById(Integer productId);

    List<ProductBean> getAllProducts();

    ProductBean updateProduct(ProductBean productBean);

    String deleteProduct(Integer productId);


}
