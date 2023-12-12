package com.brand13.redis.service;

import java.util.List;
import java.util.Set;

import com.brand13.redis.vo.Keyword;
import com.brand13.redis.vo.Product;
import com.brand13.redis.vo.ProductGrp;
import com.brand13.redis.vo.ProductGrpToKeyword;

public interface LowestPriceService {
    Set getZsetValue(String key);
    Set getZsetValueWithStatus(String key) throws Exception;
    Set getZsetValueWithSpecificException(String key) throws Exception;
    int setNewProduct(Product newProduct);
    int setNewProductGrp(ProductGrp newProductGrp);
    int setNewProductGrpToKeyword(ProductGrpToKeyword newProductGrpToKeyword);
    Keyword getLowestPriceProductByKeyword(String keyword);
    List<ProductGrp> getProdGrpUsingKeyword(String keyword);
}
