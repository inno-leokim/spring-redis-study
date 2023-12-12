package com.brand13.redis.vo;

import lombok.Data;

@Data
public class ProductGrpToKeyword {

    private String keyword; //유아용품: 하기스기저귀(FPG0001), A사 딸랑이(FPG0002)
    private String prodGrpId;
    private double score;
    
}
