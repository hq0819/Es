package com.hq.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "label")
@Setting(shards = 2,replicas = 1)
@ToString
public class Label {
    @Id
    private String id;
    @Field(name = "labelName",type = FieldType.Keyword)
    private String labelName;
    @Field(name = "labelDesc",type = FieldType.Text,analyzer = "ik_max_word")
    private String labelDesc;
    @Field(name = "source",type = FieldType.Text,analyzer = "ik_max_word")
    private String source;
    @Field(name = "orderBy",type = FieldType.Keyword)
    private String orderBy;
    @Field(name = "created",type = FieldType.Date)
    private Date created;
}
