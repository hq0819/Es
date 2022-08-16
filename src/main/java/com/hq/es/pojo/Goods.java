package com.hq.es.pojo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@Data
@AllArgsConstructor
@RequiredArgsConstructor()
@NoArgsConstructor
@ToString
@Document(indexName = "goods")
@Setting(shards = 2,replicas = 1)
public class Goods {
    @Id
    @NonNull
    private String id;
    @Field(name = "goodsName",type = FieldType.Text,analyzer = "ik_max_word")
    private String goodsName;
    @Field(name = "price",type = FieldType.Double)
    private double price;
    @Field(name="created",type = FieldType.Date,format= DateFormat.year_month_day)
    private String created;
    @Field(name = "brand",type = FieldType.Keyword)
    private String brand;
    @Field(name = "productLocal",type = FieldType.Keyword)
    private String productLocal;
    @Field(name = "tag",type = FieldType.Text,analyzer = "ik_max_word")
    private String tag;
    @Field(name = "level",type = FieldType.Keyword)
    private String level;
}
