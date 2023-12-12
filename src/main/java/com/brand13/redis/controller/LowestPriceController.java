package com.brand13.redis.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.brand13.redis.service.LowestPriceService;
import com.brand13.redis.vo.Keyword;
import com.brand13.redis.vo.NotFoundException;
import com.brand13.redis.vo.Product;
import com.brand13.redis.vo.ProductGrp;
import com.brand13.redis.vo.ProductGrpToKeyword;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RequestMapping("/")
@RestController
public class LowestPriceController {

    private final LowestPriceService lowestPriceService;

    @Autowired
    public LowestPriceController(LowestPriceService lowestPriceService){
        this.lowestPriceService = lowestPriceService;
    }
    
    
    @GetMapping("/product")
    public Set GetZsetValue (@RequestParam("key") String key){
        return lowestPriceService.getZsetValue(key);
    }

    @GetMapping("/product1")
    public Set GetZsetValueWithStatus (@RequestParam("key") String key){
        try {
            return lowestPriceService.getZsetValueWithStatus(key);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    @GetMapping("/product2")
    public Set GetZsetValueUsingExController (@RequestParam("key") String key) throws Exception{
        try {
            return lowestPriceService.getZsetValueWithStatus(key);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @GetMapping("/product3")
    public ResponseEntity<Set> GetZsetValueUsingExControllerWithSpecificException (@RequestParam("key") String key) throws Exception{
        Set<String> mySet = new HashSet<>();
        try {
            mySet = lowestPriceService.getZsetValueWithSpecificException(key);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getErrmsg(), HttpStatus.NOT_FOUND);
        }

        HttpHeaders responHeaders = new HttpHeaders();

        return new ResponseEntity<Set>(mySet, responHeaders, HttpStatus.OK);
    }
    
    @PostMapping("/product")
    public int SetNewProduct(@RequestBody Product newProduct) {
        return lowestPriceService.setNewProduct(newProduct);
    }

    @PostMapping("/productGroup")
    public int SetNewProduct(@RequestBody ProductGrp newProductGrp) {
        return lowestPriceService.setNewProductGrp(newProductGrp);
    }

    @PostMapping("/productGroupToKeyword")
    public int SetNewProductGrpToKeyword(@RequestBody ProductGrpToKeyword newProductGrpToKeyword) {
        return lowestPriceService.setNewProductGrpToKeyword(newProductGrpToKeyword);
    }
    
    @GetMapping("/productPrice/lowest")
    public Keyword getLowestPriceProductByKeyword(@RequestParam("keyword") String keyword) {
        return lowestPriceService.getLowestPriceProductByKeyword(keyword);
    }
    
}
