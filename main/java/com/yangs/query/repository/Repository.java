package an.dy.domain.repository;

import an.dy.annotation.Query;
import an.dy.enums.RepositoryEnum;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import javax.management.RuntimeErrorException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @Author: yangs
 * @Description: 查询辅助类
 * @Date: 2021/9/1 14:18
 */
public class Repository {
    public static final char UNDERLINE = '_';

    public static QueryWrapper fielList(Object body) {
        return fielList(body, "", "");
    }

    public static QueryWrapper fielList(Object body, String startTime, String endTime) {
        QueryWrapper queryWrapper = new QueryWrapper();
        Map<Object, RepositoryDomain> fieldMap = getField(body);
        fieldMap.forEach((key, value) -> {
            if (StrUtil.isNotEmpty(key.toString()) && BeanUtil.isNotEmpty(value) && StrUtil.isNotEmpty(value.getValue().toString())) {
                if (RepositoryEnum.EQ.getCode().equals(value.getValue().toString())){
                    queryWrapper.eq(key.toString(), value.getValue());
                }else if (RepositoryEnum.LIKE.getCode().equals(value.getType())) {
                    queryWrapper.like(key.toString(), value.getValue());
                }else if (RepositoryEnum.NE.getCode().equals(value.getType())) {
                    queryWrapper.ne(key.toString(), value.getValue());
                }else if (RepositoryEnum.GT.getCode().equals(value.getType())) {
                    queryWrapper.gt(key.toString(), value.getValue());
                }else if (RepositoryEnum.GE.getCode().equals(value.getType())) {
                    queryWrapper.ge(key.toString(), value.getValue());
                }else if (RepositoryEnum.LT.getCode().equals(value.getType())) {
                    queryWrapper.lt(key.toString(), value.getValue());
                }else if (RepositoryEnum.LE.getCode().equals(value.getType())) {
                    queryWrapper.le(key.toString(), value.getValue());
                }else if (RepositoryEnum.IS_NULL.getCode().equals(value.getType())) {
                    queryWrapper.isNull(key.toString());
                }else if (RepositoryEnum.IS_NOT_NULL.getCode().equals(value.getType())) {
                    queryWrapper.isNotNull(key.toString());
                }else if (RepositoryEnum.ORDER_BY_DESC.getCode().equals(value.getType())) {
                    queryWrapper.orderByDesc(key.toString());
                }else if (RepositoryEnum.ORDER_BY_ASC.getCode().equals(value.getType())) {
                    queryWrapper.orderByAsc(key.toString());
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

    /**
     * 获取类所有属性
     * @param
     * @return map
     */
    public static Map<Object, RepositoryDomain> getField(Object t) {
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
                                            Query annotation = str.getAnnotation(Query.class);
                                            String name = str.getName();
                                            name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());
                                            return RepositoryDomainBuilder.builder()
                                                    .withValue(Optional.ofNullable(t.getClass().getMethod("get" + name).invoke(t)).orElse(""))
                                                    .withType(BeanUtil.isNotEmpty(annotation) ?  annotation.queryType().getCode() : RepositoryEnum.EQ.getCode())
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
    public static String camelToUnderline(String param, Integer charType) {
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
    public static String underlineToCamel(String param) {
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
