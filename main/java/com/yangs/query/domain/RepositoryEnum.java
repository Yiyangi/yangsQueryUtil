package an.dy.enums;

import com.baomidou.mybatisplus.core.toolkit.StringPool;

/**
 * @Author: yangs
 * @Description:
 * @Date: 2021/9/2 16:41
 */
public enum RepositoryEnum {

    IN("IN"),
    NOT("NOT"),
    EQ("EQ"),
    LIKE("LIKE"),
    NE("NE"),
    GT("GT"),
    GE("GE"),
    LT("LT"),
    LE("LE"),
    IS_NULL("IS_NULL"),
    IS_NOT_NULL("IS_NOT_NULL"),
    GROUP_BY("GROUP_BY"),
    ORDER_BY_ASC("ORDER_BY_ASC"),
    ORDER_BY_DESC("ORDER_BY_DESC");


    private final String code;

    RepositoryEnum(String code) {
        this.code = code;

    }

    public String getCode() {
        return code;
    }

}
