package com.hq.es.repository;

import com.hq.es.pojo.PartnerList;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface PartnerRepostory extends ElasticsearchRepository<PartnerList,String> {
    List<PartnerList> findByName(String name);
    List<PartnerList> findByNameLike(String name);
}
