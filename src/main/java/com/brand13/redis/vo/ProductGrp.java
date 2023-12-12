package com.brand13.redis.vo;

import java.util.List;

import lombok.Data;

@Data
public class ProductGrp {
    private String prodGrpId; // FPG0001

    private List<Product> productList;

}
