package com.hq.es.repository;

import com.hq.es.pojo.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface GoodsRepository extends ElasticsearchRepository<Goods,String> {
     List<Goods> findByGoodsName(String goodsName);
}
