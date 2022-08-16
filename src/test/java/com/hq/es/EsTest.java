package com.hq.es;

import com.hq.es.pojo.Goods;
import com.hq.es.repository.GoodsRepository;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.FuzzyQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.PipelineAggregatorBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.pipeline.ParsedBucketMetricValue;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.clients.elasticsearch7.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class EsTest {

    @Autowired
    ElasticsearchRestTemplate template;
    @Autowired
    GoodsRepository goodsRepository;


    @Test
    public void initDocs() {

        Goods goods = new Goods("1", "小米2", 2000, "2020-01-01", "小米", "中国", "性价比 发烧 国产", "旗舰机");
        Goods goods1 = new Goods("2", "红米2", 799, "2021-03-11", "红米", "中国", "性价比", "千元机");
        Goods goods2 = new Goods("3", "华为mate40", 4999, "2021-08-07", "华为", "中国", "高端机型 自研 国产 高清4k", "旗舰机");
        Goods goods3 = new Goods("4", "苹果 5s", 5999, "2021-08-07", "apple", "美国", "高端机型 美产 美观", "旗舰机");
        Goods goods4 = new Goods("5", "小灵通", 500, "2012-09-12", "小灵通", "中国", " 国产", "百元机");
        Goods goods5 = new Goods("6", "三星s20", 7999, "2021-10-21", "三星", "韩国", "性价比 高端", "旗舰机");
        Goods goods6 = new Goods("7", "索尼s2", 6100, "2022-08-02", "索尼", "日本", "高端", "旗舰机");
        List<Goods> list = new ArrayList<>();
        list.add(goods);
        list.add(goods1);
        list.add(goods2);
        list.add(goods3);
        list.add(goods4);
        list.add(goods5);
        list.add(goods6);
        IndexOperations indexOperations = template.indexOps(Goods.class);
        indexOperations.createWithMapping();
        template.save(list);
    }

    @Test
    public void create() {
        IndexOperations indexOperations = template.indexOps(Goods.class);
        indexOperations.createWithMapping();
    }

    @Test
    public void query() {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("goodsName", "红米2");
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withPageable(PageRequest.of(0, 10)).build();

        SearchHits<Goods> search = template.search(build, Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()).forEach(System.out::println);

        System.out.println(search);
    }

    @Test
    public void termQuerys() {

        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder().withQuery(QueryBuilders.termQuery("goodsName", "苹果")).build(), Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()).forEach(System.out::println);

    }

    @Test
    public void rangeQuery() {

        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder().withQuery(QueryBuilders.rangeQuery("price").lte(5000).gte(2000)).build(), Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()).forEach(System.out::println);

    }

    @Test
    public void boolQuery() {
        String[] a = new String[]{"goodsName"};
        String[] b= {"price"};

        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery("price").lte(5000).gte(2000))
                .must(QueryBuilders.matchQuery("goodsName","小米")))
                .withSourceFilter(new FetchSourceFilter(a,b))
                .build(), Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).collect(Collectors.toList()).forEach(System.out::println);
    }

    @Test
    public void highQuery(){
        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery("price").lte(5000).gte(2000))
                        .must(QueryBuilders.matchQuery("goodsName","小米")))
                .withHighlightFields(new HighlightBuilder.Field("goodsName").postTags("<font color= red>").preTags("</font>"))
                .build(), Goods.class);


        search.getSearchHits().stream().forEach(System.out::println);

    }



    @Test
    public void fuzzysQuery(){
        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery()
                        .must(QueryBuilders.rangeQuery("price").lte(5000).gte(2000))
                        .must(QueryBuilders.matchQuery("goodsName","小米")))
                .withHighlightFields(new HighlightBuilder.Field("goodsName").postTags("<font color= red>").preTags("</font>"))
                .build(), Goods.class);
    }



    @Test
    public void update() {
        Document document = Document.create();
        document.put("goodsName", "苹果6s");
        template.update(UpdateQuery.builder("4").withDocument(document).build(), IndexCoordinates.of("goods"));
    }

    @Test
    public void updateByQuery() {
        NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(QueryBuilders.idsQuery().addIds("5"));
        Document document = Document.create();
        document.put("username", "123噶阿哥");
        // Script painless = new Script(ScriptType.INLINE, "painless", "ctx._source.username=params.username", document);
        UpdateQuery build1 = UpdateQuery.builder(nativeSearchQuery).withScript("ctx._source.username=params.username").withLang("painless").withParams(document).build();
        template.updateByQuery(build1, IndexCoordinates.of("easysale-logtest"));
    }


    @Test
    public void matchQueryTest() {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("goodsName", "华为小米").operator(Operator.AND);
        NativeSearchQuery build = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withSorts(SortBuilders.fieldSort("price").order(SortOrder.ASC))
                .withPageable(PageRequest.of(0, 10))
                .withHighlightFields(new HighlightBuilder.Field("goodsName").preTags("<font color='red'>").postTags("</font>")).build();
        SearchHits<Goods> search1 = template.search(build, Goods.class);
        search1.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);
    }


    @Test
    public void termQueryTest() {
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("goodsName", "苹果");
        SearchHits<Goods> search = template.search(new NativeSearchQuery(termQueryBuilder), Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);
    }

    @Test
    public void multiQueryTest() {
        MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery("中国小米", "goodsName", "productLocal");
        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(multiMatchQueryBuilder).build();
        SearchHits<Goods> search = template.search(build, Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);

    }


    @Test
    public void matchPhraseQueryTest() {
        SearchHits<Goods> search = template.search(new NativeSearchQueryBuilder()
                        .withQuery(QueryBuilders.matchPhraseQuery("goodsName", "小米的华为"))
                        .build()
                , Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);
    }

    @Test
    public void RangeQueryTest() {
        RangeQueryBuilder price = QueryBuilders.rangeQuery("price")
                .from(2000).to(5000);
        SearchHits<Goods> search = template.search(new NativeSearchQuery(price), Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);
    }

    @Test
    public void testGoods(){
        List<Goods> a = goodsRepository.findByGoodsName("小米");
        System.out.println(a);
    }

    @Test
    public void fuzzyQueryTest() {
        FuzzyQueryBuilder queryBuilder = QueryBuilders.fuzzyQuery("goodsName", "小米通").fuzziness(Fuzziness.ONE);
        SearchHits<Goods> search = template.search(new NativeSearchQuery(queryBuilder), Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);
    }

    @Test
    public void wildcardQueryTest() {
        WildcardQueryBuilder goodsName = QueryBuilders.wildcardQuery("goodsName", "*米").caseInsensitive(false);
        System.out.println(goodsName.getName());
        System.out.println(goodsName.getWriteableName());
        SearchHits<Goods> search = template.search(new NativeSearchQuery(goodsName), Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);
    }

    @Test
    public void boolQueryTest() {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.boolQuery().must(QueryBuilders.rangeQuery("price").from(2000).to(6000)))
                .must(QueryBuilders.termQuery("productLocal", "中国"))
                .should(QueryBuilders.idsQuery().addIds("2"));

        NativeSearchQuery build = new NativeSearchQueryBuilder().withQuery(boolQueryBuilder).build();

        SearchHits<Goods> search = template.search(build, Goods.class);
        search.getSearchHits().stream().map(SearchHit::getContent).forEach(System.out::println);


    }


    @Test
    public void aggsTest() {
        NativeSearchQuery level = new NativeSearchQueryBuilder()
                .withAggregations(
                        AggregationBuilders.terms("level_term").field("level")
                                .subAggregation(AggregationBuilders.avg("avg_price").field("price"))
                ).withPipelineAggregations(
                        PipelineAggregatorBuilders.minBucket("pipl_min", "level_term>avg_price")
                )
                .build();
        SearchHits<Goods> search = template.search(level, Goods.class);
        ElasticsearchAggregations aggregations = (ElasticsearchAggregations) search.getAggregations();
        assert aggregations != null;
        List<Aggregation> aggregations1 = aggregations.aggregations().asList();
        for (Aggregation aggregation : aggregations1) {
            if (aggregation instanceof Terms) {
                Terms terms = (Terms) aggregation;
                List<? extends Terms.Bucket> buckets = terms.getBuckets();
                for (Terms.Bucket bucket : buckets) {
                    System.out.println(bucket.getKey());
                    Aggregations aggregations2 = bucket.getAggregations();
                    for (Aggregation aggregation1 : aggregations2) {
                        if (aggregation1 instanceof Avg) {
                            Avg avg = (Avg) aggregation1;
                            System.out.println(avg.getValue());
                            System.out.println(avg.getName());
                        }
                    }

                }
            }

            if (aggregation instanceof ParsedBucketMetricValue) {
                ParsedBucketMetricValue parsedBucketMetricValue = (ParsedBucketMetricValue) aggregation;
                System.out.println(parsedBucketMetricValue.value());
                for (String key : parsedBucketMetricValue.keys()) {
                    System.out.println(key);

                }
            }
        }

    }
}
