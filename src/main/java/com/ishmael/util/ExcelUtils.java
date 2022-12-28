package com.ishmael.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ishmael.entity.ExcelTag;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: zhimeng.an
 * @createDate: 2022-11-01 下午 4:39
 */
public class ExcelUtils {

    public static void read(File file){
        ExcelReaderBuilder builder=EasyExcel.read(file);
       List<Map<Integer,String>> list= builder.sheet().doReadSync();
       list.forEach(p->{
               System.out.println(p.get(1)+p.get(2)+p.get(4));
       });
    }
}
