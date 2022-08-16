package com.hq.es;

import com.hq.es.pojo.Goods;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.data.elasticsearch.core.query.UpdateResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class ElasticsearchDocOper {
    @Autowired
    ElasticsearchRestTemplate template;

    @Test
    public void addDoc(){
        List<Goods> list = new ArrayList<>();


        template.save(list);
    }

    @Test
    public void deleteDoc(){
        Goods goods = new Goods("1");
        template.delete(goods);
    }

    /**
     * 部分更新 ，全量更新使用save;
     */
    @Test
    public void updateDoc(){
        Map map = new HashMap<String,Object>();
        map.put("goodsName","苹果");
        map.put("productLocal","美国");
        Document parse = Document.from(map);
        UpdateQuery build = UpdateQuery.builder("2").withDocument(parse).build();
        UpdateResponse goods = template.update(build, IndexCoordinates.of("goods"));
        System.out.println(goods);

    }
    @Test
    public void queryDoc(){
        //条件查询
        Query query = new NativeSearchQuery(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("price").
                gte(3000).lte(6000)).must(QueryBuilders.matchQuery("productLocal","中国")));
        query.addSort(Sort.by(Sort.Direction.DESC,"price"));

       /* RangeQueryBuilder rangequery = QueryBuilders.rangeQuery("price").gte(3000).lte(6000);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("productLocal", "中国");
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(rangequery)
        //        .withQuery(matchQueryBuilder)
                .build();*/

        query.setPageable(PageRequest.of(0,2));
            SearchHits<Goods> search = template.search(query, Goods.class);
        search.forEach(System.out::println);
    }

    @Test
    public void queryDoc2() throws InstantiationException, IllegalAccessException {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brand", "华为手机");
        Query query = new NativeSearchQuery(termQueryBuilder);
        SearchHits<Goods> search = template.search(query, Goods.class);
        search.forEach(System.out::println);
    }











}
