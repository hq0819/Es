package com.hq.es;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

@SpringBootTest
public class EsDemoTest {
    @Autowired
    ElasticsearchRestTemplate template;
}
