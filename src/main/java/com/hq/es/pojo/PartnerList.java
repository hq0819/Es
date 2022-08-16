package com.hq.es.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

import java.util.Date;

@Data
@Document(indexName = "partner")
@Setting(shards = 2)
public class PartnerList {
    @Id
    private String partnerId = "";
    @Field(value = "name",analyzer = "ik_smart",type = FieldType.Text)
    private String name = "";
    @Field(value ="status", type=FieldType.Keyword)
    private String status = "";
    @Field(value ="snycFlg", type=FieldType.Keyword)
    private String snycFlg = "";
    @Field(value ="accountLevel", type=FieldType.Keyword)
    private String accountLevel = "";
    @Field(value ="accountType", type=FieldType.Keyword)
    private String accountType = "";
    @Field(value ="province", type=FieldType.Keyword)
    private String province = "";
    @Field(value ="city", type=FieldType.Keyword)
    private String city = "";
    @Field(value ="area", type=FieldType.Keyword)
    private String area = "";
    @Field(value ="salerName", type=FieldType.Keyword)
    private String salerName = "";
    @Field(value ="salerLogin", type=FieldType.Keyword)
    private String salerLogin = "";
    @Field(value ="businessCenter", type=FieldType.Keyword)
    private String businessCenter = "";
    @Field(value ="sales", type=FieldType.Keyword)
    private String sales = "";
    @Field(value ="target", type=FieldType.Keyword)
    private String target = "";
    @Field(value ="histSlsVol", type=FieldType.Long)
    private String histSlsVol= "";
    @Field(value ="targetCustomerFlag", type=FieldType.Keyword)
    private String targetCustomerFlag;
    @Field(value ="salerPositionId", type=FieldType.Keyword)
    private String salerPositionId;
    @Field(value ="industry", type=FieldType.Keyword)
    private String industry = "";
    @Field(value ="collectionFlag", type=FieldType.Keyword)
    private String collectionFlag = "";
    @Field(value ="receivableTrouble", type=FieldType.Keyword)
    private String receivableTrouble= "";
    @Field(value ="borrowTrouble", type=FieldType.Keyword)
    private String borrowTrouble= "";
    @Field(value ="unresolvedTrouble", type=FieldType.Keyword)
    private String unresolvedTrouble= "";
    @Field(value ="backLetterTrouble", type=FieldType.Keyword)
    private String backLetterTrouble= "";
    @Field(value ="created", type=FieldType.Date)
    private Date created;
}
