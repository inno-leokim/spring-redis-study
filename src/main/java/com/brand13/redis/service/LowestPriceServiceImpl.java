package com.brand13.redis.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.brand13.redis.vo.Keyword;
import com.brand13.redis.vo.NotFoundException;
import com.brand13.redis.vo.Product;
import com.brand13.redis.vo.ProductGrp;
import com.brand13.redis.vo.ProductGrpToKeyword;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class LowestPriceServiceImpl implements LowestPriceService{

    @Autowired
    private RedisTemplate myProdPriceRedis;

    @Override
    public Set getZsetValue(String key) {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        return myTempSet;
    }

    @Override
    public Set getZsetValueWithStatus(String key) throws Exception {
        Set myTempSet = new HashSet();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        if (myTempSet.size() < 1){
            throw new Exception("The key doesn't have any member");
        }
        return myTempSet;
    }

    @Override
    public Set getZsetValueWithSpecificException(String key) throws Exception {
        Set myTempSet = new HashSet<>();
        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);
        if (myTempSet.size() < 1){
            throw new NotFoundException("The key doesn't exist in redis.", HttpStatus.NOT_FOUND);
        }
        return myTempSet;
    }

    // 완성해 볼 것!!
    // public void DeleteKey (String key){
    //     myProdPriceRedis.delete(key);
    // }

    @Override
    public int setNewProduct(Product newProduct) {
        int rank = 0;
        myProdPriceRedis.opsForZSet().add(newProduct.getProdGrpId(), newProduct.getProductId(), newProduct.getPrice());
        rank = myProdPriceRedis.opsForZSet().rank(newProduct.getProdGrpId(), newProduct.getProductId()).intValue();
        return rank;
    }

    @Override
    public int setNewProductGrp(ProductGrp newProductGrp) {
        
        List<Product> product = newProductGrp.getProductList();
        String productId = product.get(0).getProductId();
        int price = product.get(0).getPrice();
        myProdPriceRedis.opsForZSet().add(newProductGrp.getProdGrpId(), productId, price);
        int productCnt = myProdPriceRedis.opsForZSet().zCard(newProductGrp.getProdGrpId()).intValue();
        
        return productCnt;
    }

    @Override
    public int setNewProductGrpToKeyword(ProductGrpToKeyword newProductGrpToKeyword){
        String keyword = newProductGrpToKeyword.getKeyword();
        String prodGrpId = newProductGrpToKeyword.getProdGrpId();
        double score = newProductGrpToKeyword.getScore();
        myProdPriceRedis.opsForZSet().add(keyword, prodGrpId, score);
        int rank = myProdPriceRedis.opsForZSet().rank(keyword, prodGrpId).intValue();
        return rank;
    }

    @Override
    public Keyword getLowestPriceProductByKeyword(String keyword) {
        Keyword returnInfo = new Keyword();
        List<ProductGrp> tempProductGrp = new ArrayList<>();
       
        // keyword를 통해 productGroup 가져오기(10개)
        tempProductGrp = getProdGrpUsingKeyword(keyword);

        // Loop를 진행하면서 ProductGroup으로 Product:price 가져오기(10개)
        returnInfo.setKeyword(keyword);
        returnInfo.setProductGrpList(tempProductGrp);

        // 가져온 정보들을 return 할 Object에 넣기 

        // 해당 Object 리턴
        return returnInfo;
    }
    
    @Override
    public List<ProductGrp> getProdGrpUsingKeyword(String keyword){
        List<ProductGrp> returnInfo = new ArrayList<>();
        
        List<String> prodGrpIdList = new ArrayList<>();
        prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().reverseRange(keyword, 0, 0));
        
        List<Product> tempProdList = new ArrayList<>();
        
        //10개의 productID로 loop
        for(final String prodGrpId:prodGrpIdList){
            
            ProductGrp temProductGrp = new ProductGrp();
            
            Set prodAndPriceList = new HashSet<>();
            prodAndPriceList = myProdPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 0);
            Iterator<Object> prodPriObj = prodAndPriceList.iterator();
            
            // loop 하면서 object bind(10개)
            while(prodPriObj.hasNext()){
                ObjectMapper objMapper = new ObjectMapper();
                Map<String, Object> prodPricMap = objMapper.convertValue(prodPriObj.next(), Map.class);
                
                Product tempProduct = new Product();
                // Product Obj bind
                tempProduct.setProductId(prodPricMap.get("value").toString()); //prod_id
                tempProduct.setPrice(Double.valueOf(prodPricMap.get("score").toString()).intValue());
                tempProduct.setProdGrpId(prodGrpId);

                tempProdList.add(tempProduct);
            }

            temProductGrp.setProdGrpId(prodGrpId);
            temProductGrp.setProductList(tempProdList);
            returnInfo.add(temProductGrp);
        }


        return returnInfo;
    }

}
