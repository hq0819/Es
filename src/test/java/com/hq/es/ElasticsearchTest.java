package com.hq.es;

import com.hq.es.pojo.Goods;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;

@SpringBootTest
public class ElasticsearchTest {
    @Autowired
    ElasticsearchRestTemplate template;

    @Test
    public void indexOper(){
        IndexOperations indexOperations = template.indexOps(Goods.class);
        indexOperations.createWithMapping();
        //indexOperations.create();
        //Document mapping = indexOperations.createMapping(Goods.class);
      //  indexOperations.putMapping(mapping);
    }
    @Test
    public void delIndex(){
        IndexOperations indexOperations = template.indexOps(Goods.class);
        if (indexOperations.exists()){
            indexOperations.delete();
        }
    }
    @Test
    public void updateMapping(){
        IndexOperations indexOperations = template.indexOps(Goods.class);
        Document mapping = indexOperations.createMapping(Goods.class);
        indexOperations.putMapping(mapping);
    }

}
