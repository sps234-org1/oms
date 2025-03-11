package com.jocata.oms.product.controller;

import com.jocata.oms.bean.ProductBean;
import com.jocata.oms.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @PostMapping("/save")
    ProductBean saveProduct(@RequestBody ProductBean productBean) {
        return productService.saveProduct(productBean);
    }

    @GetMapping("/get/{productId}")
    ProductBean getProductById(@PathVariable Integer productId) {
        return productService.getProductById(productId);
    }

    @GetMapping("/get")
    List<ProductBean> getAllProducts() {
        return productService.getAllProducts();
    }

    @PutMapping("/update")
    ProductBean updateProduct(@RequestBody ProductBean productBean) {
        return productService.updateProduct(productBean);
    }

    @DeleteMapping("/delete/{productId}")
    String deleteProduct(@PathVariable Integer productId) {
        return productService.deleteProduct(productId);
    }
}
