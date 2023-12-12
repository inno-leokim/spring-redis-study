package com.brand13.redis.vo;

import java.util.List;

import lombok.Data;

@Data
public class Keyword {
    
    private String keyword; //유아용품: 하기스기저귀(FPG0001), A사 딸랑이(FPG0002)
    private List<ProductGrp> productGrpList; // {"FPG0001", "FPG0002"}
}
