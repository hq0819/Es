package com.hq.es;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import com.google.common.base.Splitter;
import org.apache.logging.log4j.util.Strings;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@SpringBootTest
public class ExcelTest {
    String loginUrl = "http://ms.hiktest.com/user/V1.0/ldap/login";
    Map<String,String > user = new HashMap<>();
    String token  = "";

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void login(){
        user.put("username","heqin11");
        user.put("password","Hq19960199.");
        ResponseEntity<Map> mapResponseEntity = restTemplate.postForEntity(loginUrl, user, Map.class, new Object());
        Map body = mapResponseEntity.getBody();
        token = (String) body.get("data");
        System.out.println(mapResponseEntity);
    }
    @Test
    public void exportExcel() throws Exception {
        Pattern compile = Pattern.compile("[R|S][A-Z]+\\d+_\\d+-{0,}\\d+");
        ImportParams params = new ImportParams();
        List<Business> li = ExcelImportUtil.importExcel(new FileInputStream("E:\\needs.xlsx"), Business.class, params);
        for (Business business : li) {
            business.setFrontDevelopTime((Strings.isEmpty(business.getFrontDevelopTime())?"0人天":business.getFrontDevelopTime()).replace("人天",""));
            business.setJavaDevelopTime((Strings.isEmpty(business.getJavaDevelopTime())?"0人天":business.getJavaDevelopTime()).replace("人天",""));
            business.setTestDevelopTime((Strings.isEmpty(business.getTestDevelopTime())?"0人天":business.getTestDevelopTime()).replace("人天",""));

            Matcher matcher = compile.matcher(business.getNeedName());
            while (matcher.find()){
                business.setNeedNum(matcher.group());
            }
            String system = "";
            if(business.getSystem().contains("国内")){
                system = "易销售";
            }else{
                system = "BW";
            }
            business.setNeedName(system+"-"+business.getNeedName().replaceAll("【.+】",""));
            List<String> names = Splitter.on(";").omitEmptyStrings().splitToList(business.getJoiner());
            List<String>  strings= new ArrayList<>(names);
            boolean flags =  Collections.disjoint(names,MemberUtils.getMemberByWorkType("测试").stream().map(Member::getName).collect(Collectors.toList()));
            if (flags){
                strings.add(business.getCharger());
            }
            for (String string : strings) {
                for (Member member : MemberUtils.MList) {
                    if (string.equals(member.getName())){
                        switch (member.getWorkType()+member.getLevel()){
                            case "前端高":
                                business.setFrontDevelop(member.getName());
                                business.setHighFront(Double.parseDouble(business.getFrontDevelopTime()));
                                break;
                            case "前端中":
                                business.setFrontDevelop(member.getName());
                                business.setMidFront(Double.parseDouble(business.getFrontDevelopTime()));
                                break;
                            case "后端高":
                                business.setJavaDevelop(member.getName());
                                business.setHighJava(Double.parseDouble(business.getJavaDevelopTime()));
                                break;
                            case "后端中":
                                business.setJavaDevelop(member.getName());
                                business.setMidJava(Double.parseDouble(business.getJavaDevelopTime()));
                                break;
                            case "测试高":
                                business.setTestDevelop(member.getName());
                                business.setHighTest(Double.parseDouble(business.getTestDevelopTime()));
                                break;
                            case "测试中":
                                business.setTestDevelop(member.getName());
                                business.setMidTest(Double.parseDouble(business.getTestDevelopTime()));
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
        List<Business> collect = new ArrayList<>();
                li.forEach(e -> {
                    if ("软通".equals(MemberUtils.getMemberByName(e.getFrontDevelop()).getBelong()) ||
                            "软通".equals(MemberUtils.getMemberByName(e.getJavaDevelop()).getBelong()) ||
                            "软通".equals(MemberUtils.getMemberByName(e.getTestDevelop()).getBelong())) {
                        Business business = new Business();
                        BeanUtils.copyProperties(e,business);
                        collect.add(business);
                    }
                }
        );

        List<Business> collect1 = li.stream().filter(e ->
                "博彦".equals(MemberUtils.getMemberByName(e.getFrontDevelop()).getBelong()) ||
                        "博彦".equals(MemberUtils.getMemberByName(e.getJavaDevelop()).getBelong()) ||
                        "博彦".equals(MemberUtils.getMemberByName(e.getTestDevelop()).getBelong())
        ).collect(Collectors.toList());

        Map<String,List<Business>>  maps  = new HashMap<>();
        maps.put("软通",collect);
        maps.put("博彦",collect1);
        AtomicInteger a = new AtomicInteger(1);
        maps.forEach((k,v)->{
            v.forEach(e->{
                Member front = MemberUtils.getMemberByName(e.getFrontDevelop());
                Member java = MemberUtils.getMemberByName(e.getJavaDevelop());
                Member test = MemberUtils.getMemberByName(e.getTestDevelop());
                boolean flag1 = "前端".equals(front.getWorkType())&& k.equals(front.getBelong());
                boolean flag2 = "后端".equals(java.getWorkType())&& k.equals(java.getBelong());
                boolean flag3 = "测试".equals(test.getWorkType())&& k.equals(test.getBelong());
                if (!flag1) {
                    e.setHighFront(0);
                    e.setMidFront(0);
                }
                if (!flag2) {
                    e.setHighJava(0);
                    e.setMidJava(0);
                }
                if (!flag3) {
                    e.setHighTest(0);
                    e.setMidTest(0);
                }
            });
            double fhsum = v.stream().mapToDouble(Business::getHighFront).sum();
            double fmsum = v.stream().mapToDouble(Business::getMidFront).sum();
            double jhsum = v.stream().mapToDouble(Business::getHighJava).sum();
            double jmsum = v.stream().mapToDouble(Business::getMidJava).sum();
            double thsum = v.stream().mapToDouble(Business::getHighTest).sum();
            double tmsum = v.stream().mapToDouble(Business::getMidTest).sum();
            TemplateExportParams templateExportParams = new TemplateExportParams("C:\\Users\\heqin11\\Desktop\\模板.xlsx");
            Map<String,Object> map = null;
            try {
                map = poListToMap(v);
            map.put("needDate","需求包20220805");
            map.put("Evaluates","蒋超俊、黄绪辉、贺钦11");
            map.put("fhsum",fhsum);
            map.put("fmsum",fmsum);
            map.put("jhsum",jhsum);
            map.put("jmsum",jmsum);
            map.put("thsum",thsum);
            map.put("tmsum",tmsum);
            Workbook sheets = ExcelExportUtil.exportExcel(templateExportParams,map);
            Sheet sh = sheets.getSheet("双方确认人天");
            sh.addMergedRegion(new CellRangeAddress(9,9+v.size()-1,0,0));
            FileOutputStream fileOutputStream = new FileOutputStream("E:\\"+k+".xlsx");
            sheets.write(fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            a.getAndIncrement();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    private  Map<String,Object> poListToMap(List<?> list) throws IllegalAccessException {
        List<Map<String,Object>> li = new ArrayList<>(list.size());
        Field[] declaredFields = Business.class.getDeclaredFields();
            for (Object t : list) {
                Map<String,Object> map = new HashMap<>();
                for (Field declaredField : declaredFields) {
                declaredField.setAccessible(true);
                map.put(declaredField.getName(),declaredField.get(t));
            }
                li.add(map);
        }
        Map<String,Object> re = new HashMap<>();
        re.put("list",li);
        return re;
    }



}