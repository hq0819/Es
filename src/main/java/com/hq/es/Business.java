package com.hq.es;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
public class Business implements Serializable {
    @Excel(name = "名称")
    private String needName;
    private String needNum;
    @Excel(name = "参与人")
    private String joiner;
    @Excel(name = "负责人")
    private String charger;
    private String frontDevelop;
    @Excel(name = "前端开发人天")
    private String frontDevelopTime;
    private String javaDevelop;
    @Excel(name = "后端开发人天")
    private String javaDevelopTime;
    private String testDevelop;
    @Excel(name = "测试人天")
    private String testDevelopTime;
    @Excel(name = "需求提出人")
    private String needAsker;
    @Excel(name = "需求分类")
    private String system;
    private String Evaluates = "蒋超俊、黄绪辉、贺钦11";

    private double highFront;
    private double midFront;

    private double highJava ;
    private double midJava ;

    private double highTest ;
    private double midTest;


}
