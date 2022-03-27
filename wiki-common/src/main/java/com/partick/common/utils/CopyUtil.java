package com.partick.common.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

public class CopyUtil {

    /**
     * 单体复制
     */
    public static <T> T copy(Object origin,Class<T> targetClass) {
        if (origin == null) {
            return null;
        }
        T targetObj = null;
        try {
            targetObj  =targetClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BeanUtils.copyProperties(origin, targetObj);
        return targetObj;
    }

    /**
     * 列表复制
     */
    public static  <T> List<T> copy(List originList, Class<T> target) {
        List<T> targetList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(originList)) {
            for (Object origin : originList) {
                T targetObj = copy(origin, target);
                targetList.add(targetObj);
            }
        }
        return targetList;
    }
}
