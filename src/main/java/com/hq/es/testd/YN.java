package com.hq.es.testd;

public enum YN {
    YN("是","Y");
    private String name;
    private String value;
    private YN(String v1,String v2){
        this.name = v1;
        this.value = v2;
    }
}
