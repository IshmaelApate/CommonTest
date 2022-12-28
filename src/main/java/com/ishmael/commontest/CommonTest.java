package com.ishmael.commontest;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.ishmael.entity.Book;
import com.ishmael.util.ExcelUtils;
import org.apache.catalina.Manager;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: zhimeng.an
 * @createDate: 2022-07-08 下午 1:47
 */
public class CommonTest {


    static float calculate(float x){

        float xhalf = 0.5f * x;

        int i = Float.floatToIntBits(x);

        i = 0x5f3759df - (i >> 1);

        x = Float.intBitsToFloat(i);

        x *= (1.5f - xhalf * x * x);

        return x;

    }

    public static double invSqrt(double x) {

        double xhalf = 0.5d * x;

        long i = Double.doubleToLongBits(x);

        i = 0x5fe6ec85e7de30daL - (i >> 1);

        x = Double.longBitsToDouble(i);

        x *= (1.5d - xhalf * x * x);

        return x;
    }
}
