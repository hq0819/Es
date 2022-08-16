package com.hq.es.dao;


import com.hq.es.pojo.PartnerList;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PartnerListMapper {
    List<PartnerList> queryPartnerList();
}
