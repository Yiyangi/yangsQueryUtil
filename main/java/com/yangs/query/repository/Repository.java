package com.yangs.query.repository;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangs.query.annotation.LikeQuery;
import com.yangs.query.domain.RepositoryDomain;
import com.yangs.query.domain.RepositoryDomainBuilder;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author: yangs
 * @Description: 查询辅助类
 * @Date: 2021/9/1 14:18
 */
public class Repository<T> {
    public static final char UNDERLINE = '_';

    public QueryWrapper fielList(T body, String startTime, String endTime) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<Object, RepositoryDomain> fieldMap = getField(body);
        fieldMap.forEach((key, value) -> {
            if (StrUtil.isNotEmpty(key.toString()) && BeanUtil.isNotEmpty(value) && StrUtil.isNotEmpty(value.getValue().toString())) {
                if (RepositoryDomain.RepositoryDomainEnum.LIKE.getCode().equals(value.getType())) {
                    queryWrapper.like(key.toString(), value.getValue());
                } else {
                    queryWrapper.eq(key.toString(), value.getValue());
                }
            }
        });
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)) {
            queryWrapper.ge("create_time", startTime);
            queryWrapper.le("create_time", endTime);
        }
        return queryWrapper;
    }

    public QueryWrapper fielList(T body) {
        return fielList(body, "", "");
    }

    /**
     * 获取类所有属性
     * @param t
     * @return map
     */
    public Map<Object, RepositoryDomain> getField(T t) {
        try {
            Map<Object, RepositoryDomain> resultMap = new HashMap<>();
            List<Field> serialVersionUID = Arrays.stream(t.getClass().getDeclaredFields())
                    .filter(f -> !f.getName().equals("serialVersionUID"))
                    .collect(Collectors.toList());
            resultMap = Arrays
                    .stream(t.getClass().getDeclaredFields())
                    .filter(f -> !f.getName().equals("serialVersionUID"))
                    .collect(
                            Collectors.toMap(str ->
                                            camelToUnderline(str.getName(), 0),
                                    str -> {
                                        try {
                                            LikeQuery annotation = str.getAnnotation(LikeQuery.class);
                                            String name = str.getName();
                                            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                                            return RepositoryDomainBuilder.builder()
                                                    .withValue(Optional.ofNullable(t.getClass().getMethod("get" + name).invoke(t)).orElse(""))
                                                    .withType(BeanUtil.isNotEmpty(annotation) ? RepositoryDomain.RepositoryDomainEnum.LIKE.getCode() : RepositoryDomain.RepositoryDomainEnum.OTHER.getCode())
                                                    .build();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            return new RepositoryDomain();
                                        }
                                    }
                            )
                    );
            return resultMap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 驼峰转下划线
     */
    public String camelToUnderline(String param, Integer charType) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (Character.isUpperCase(c)) {
                sb.append(UNDERLINE);
            }
            if (charType == 2) {
                /**统一都转大写*/
                sb.append(Character.toUpperCase(c));
            } else {
                /**统一都转小写*/
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    /**
     * 下划线转驼峰
     */
    public String underlineToCamel(String param) {
        if (param == null || "".equals(param.trim())) {
            return "";
        }
        int len = param.length();
        StringBuilder sb = new StringBuilder(len);
        /** "_" 后转大写标志,默认字符前面没有"_"*/
        Boolean flag = false;
        for (int i = 0; i < len; i++) {
            char c = param.charAt(i);
            if (c == UNDERLINE) {
                flag = true;
                continue;   /**标志设置为true,跳过*/
            } else {
                if (flag == true) {
                    /**表示当前字符前面是"_" ,当前字符转大写*/
                    sb.append(Character.toUpperCase(param.charAt(i)));
                    flag = false;  /**重置标识*/
                } else {
                    sb.append(Character.toLowerCase(param.charAt(i)));
                }
            }
        }
        return sb.toString();
    }
}
