package com.hq.es;

import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RabbitmqApplicationTests {
    @Autowired
    RestHighLevelClient client;
    @Autowired
    ElasticsearchRestTemplate template;

    @Test
    void createIndex() throws IOException {
        IndicesClient indices = client.indices();
        Map<String,Object> mapping  = new HashMap<>();
        Map<String,Object> properties =  new HashMap<>();
        Map<String,Object> name =  new HashMap<>(1);
        name.put("type", "keyword");
        properties.put("name",name);
        mapping.put("properties",properties);
        CreateIndexRequest createIndex = new CreateIndexRequest("student");
        createIndex.mapping(mapping);
        System.out.println(indices.create(createIndex, RequestOptions.DEFAULT));


    }

    @Test
    void getIndex() throws IOException {

    }
}