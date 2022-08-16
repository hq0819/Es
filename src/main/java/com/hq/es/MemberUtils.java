package com.hq.es;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberUtils {
    public static List<Member> MList = new ArrayList<>();

    static {
        Member member1 = new Member("WB刘志龙","软通","后端","高");
        Member member2 = new Member("耿文娟WB1","博彦","前端","高");
        Member member3 = new Member("姚娟WB1","博彦","前端","中");
        Member member4 = new Member("颜辉恒WB1","博彦","前端","中");
        Member member5 = new Member("WB杨雪丽","软通","前端","中");
        Member member6 = new Member("刘元WB1","软通","后端","中");
        Member member7 = new Member("齐文鹏","软通","测试","中");
        Member member8 = new Member("余林俊","软通","测试","中");
        Member member9 = new Member("刘承毅WB1","软通","后端","中");
        MList.add(member1);
        MList.add(member2);
        MList.add(member3);
        MList.add(member4);
        MList.add(member5);
        MList.add(member6);
        MList.add(member7);
        MList.add(member8);
        MList.add(member9);
    }

    public static Member getMemberByName(String name){
        List<Member> collect = MList.stream().filter(e -> e.getName().equals(name)).collect(Collectors.toList());
        if (collect.size()>0){
            return collect.get(0);
        }
        return new Member();
    }


    public static List<Member> getMemberByWorkType(String workType){
        return MList.stream().filter(e->e.getWorkType().equals(workType)).collect(Collectors.toList());
    }
}
