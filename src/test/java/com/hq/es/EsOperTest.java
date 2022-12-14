package com.hq.es;


import com.hq.es.dao.PartnerListMapper;
import com.hq.es.pojo.Goods;
import com.hq.es.pojo.PartnerList;
import com.hq.es.repository.PartnerRepostory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhrasePrefixQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.SpanContainingQueryBuilder;
import org.elasticsearch.index.query.SpanTermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.query.ByQueryResponse;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class EsOperTest {
    @Autowired
    PartnerListMapper mapper;
    @Autowired
    ElasticsearchRestTemplate template;
    @Autowired
    PartnerRepostory repostory;


    @Test
    public void     testPartnerList(){
       List<PartnerList> partnerLists = mapper.queryPartnerList();

       /* IndexOperations indexOperations = template.indexOps(PartnerList.class);
        boolean withMapping = indexOperations.createWithMapping();*/

        //Iterable<PartnerList> save = template.save(partnerLists);
       // template.delete()
       /* Document document = Document.create();
        document.put("province","??????");
        document.put("accountLevel","A");
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(QueryBuilders.idsQuery().addIds("1-1AH-197"));
        SearchHits<PartnerList> search = template.search(nativeSearchQuery, PartnerList.class);
        List<PartnerList> collect = search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList());
        collect.forEach(System.out::println);
        Script painless = new Script(ScriptType.INLINE, "painless", "ctx._source.province=params.province;ctx._source.accountLevel=params.accountLevel", document);
        UpdateQuery build1 = UpdateQuery.builder(nativeSearchQuery).withScript("ctx._source.province=params.province;ctx._source.accountLevel=params.accountLevel").withLang("painless")
                .withScriptType(org.springframework.data.elasticsearch.core.ScriptType.INLINE).withParams(document).build();
        ByQueryResponse partner = template.updateByQuery(build1, IndexCoordinates.of("partner"));*/

        // ??????id??????
       /* IdsQueryBuilder idsQueryBuilder = QueryBuilders.idsQuery().addIds("1-104MHDM");
        */

        //????????????
       // TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("status", "????????????");
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("name", "??????");
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termsQuery("name", "??????");


        //????????????
       // FuzzyQueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("name", "??????").fuzziness(Fuzziness.ZERO);

        //???????????????
       // QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery("+?????? -????????????").field("name").analyzer("ik_max_word");

        //???????????????
        //WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("name", "??????*");

        //????????????
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("name","??????"));

        //
       /* NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(new HighlightBuilder.Field("name").postTags("<font coler='red'>").preTags("</font>") )
                .withSorts(SortBuilders.fieldSort("salerLogin").order(SortOrder.ASC))
                .build();
        SearchHits<PartnerList> search = template.search(build, PartnerList.class);

        SearchHitsIterator<PartnerList> partnerListSearchHitsIterator = template.searchForStream(build, PartnerList.class);
        search.stream().map(SearchHit::getHighlightFields).forEach(System.out::println);
        System.out.println(search.getTotalHits());
        search.stream().map(SearchHit::getContent).forEach(System.out::println);*/

      /*  System.out.println(repostory.count());
        List<PartnerList> list= repostory.findByName("??????");
       list.forEach(System.out::println);*/


       // NativeSearchQuery build = new NativeSearchQueryBuilder().withAggregations(AggregationBuilders.terms(""))
       // SearchHits<PartnerList> search = template.search(build, PartnerList.class);


    }

    @Test
    public void deleteTest(){
        ByQueryResponse delete = template.delete(new NativeSearchQuery(QueryBuilders.matchAllQuery()), PartnerList.class);
    }

    @Test
    public void createIndex(){
        IndexOperations indexOperations = template.indexOps(PartnerList.class);
        IndexOperations indexOperations1 = template.indexOps(Goods.class);
        indexOperations1.createWithMapping();
        indexOperations.createWithMapping();
    }

    @Test
    public void createDocTest(){
       /* List<PartnerList> partnerLists = mapper.queryPartnerList();
        template.save(partnerLists);*/

        List<Goods> list = new ArrayList<>();

        template.save(list);
    }

    @Test
    public void aggeDocTest(){
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.matchAllQuery())
                .withAggregations(AggregationBuilders.terms("group_by_brand").field("brand")
                        .subAggregation(AggregationBuilders.avg("avg_price").field("price")))
                .build();






        SearchHits<Goods> search = template.search(build, Goods.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) (search.getAggregations());
        Map<String, Aggregation> stringAggregationMap = aggregations.aggregations().asMap();
        ParsedStringTerms group_by_brand = (ParsedStringTerms) (stringAggregationMap.get("group_by_brand"));
        group_by_brand.getBuckets().forEach(e->{
            System.out.println(e.getKey());
        });


    }

    @Test
    public void matchQuery(){
        //????????? ????????????  ??????????????????
        MatchQueryBuilder operator = QueryBuilders.matchQuery("goodsName", "????????????").analyzer("ik_max_word").operator(Operator.OR);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(operator).build();
        SearchHits<Goods> search = template.search(build, Goods.class);
        search.stream().map(SearchHit::getContent).forEach(System.out::println);

    }

    @Test
    public void matchPhraseQuery(){
        //?????????match oprator???and
        MatchPhraseQueryBuilder analyzer = QueryBuilders.matchPhraseQuery("goodsName", "????????????");
        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(analyzer).build();
        SearchHits<Goods> search = template.search(query, Goods.class);
        search.stream().map(SearchHit::getContent).forEach(System.out::println);

    }

    @Test
    public void matchPhrasePrefixQuery(){
        MatchPhrasePrefixQueryBuilder matchPhrasePrefixQueryBuilder = QueryBuilders.matchPhrasePrefixQuery("goodsName","????????????");
                NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(matchPhrasePrefixQueryBuilder)
                .build();
        SearchHits<Goods> search = template.search(build, Goods.class);
        search.stream().map(SearchHit::getContent).forEach(System.out::println);
    }


    @Test
    public void multiQuery(){
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("??????", "goodsName", "productLocal").field("productLocal",3);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQueryBuilder)
                .withSorts(SortBuilders.scoreSort())
                .build();

        SearchHits<Goods> search = template.search(build, Goods.class);
        search.stream().map(SearchHit::getContent).forEach(System.out::println);
    }


    @Test
    public void stringQuery(){
        QueryStringQueryBuilder queryStringQueryBuilder = QueryBuilders.queryStringQuery("??????????????????").field("goodsName").field("productLocal").defaultOperator(Operator.OR);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(queryStringQueryBuilder)
                .build();
        SearchHits<Goods> search = template.search(build, Goods.class);
        search.stream().map(SearchHit::getContent).forEach(System.out::println);
    }


    @Test
    public void prefixQuery(){
        //?????????
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("goodsName", "??????");
        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder().withQuery(prefixQueryBuilder).build(), Goods.class);
        search.stream().map(SearchHit::getContent).forEach(System.out::println);
    }

    @Test
    public void spanQuery(){
        SpanTermQueryBuilder big = QueryBuilders.spanTermQuery("goodsName", "??????");
        SpanTermQueryBuilder little = QueryBuilders.spanTermQuery("goodsName", "??????");

        SpanContainingQueryBuilder spanContainingQueryBuilder = QueryBuilders.spanContainingQuery( little,big);
        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder().withQuery(spanContainingQueryBuilder).build(), Goods.class);
        search.stream().map(SearchHit::getContent).forEach(System.out::println);
    }

}
