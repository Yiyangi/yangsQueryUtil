package com.yangs.query.domain;

/**
 * @Author: yangs
 * @Description:
 * @Date: 2021/9/2 11:30
 */
public class RepositoryDomain {

    private Object value;

    private Integer type;

    public RepositoryDomain() {
    }

    public RepositoryDomain(Object value, Integer type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public enum RepositoryDomainEnum {

        OTHER(0, "无"),
        /**
         * like
         */
        LIKE(1, "like");

        private final Integer code;
        private final String str;

        RepositoryDomainEnum(Integer code, String str) {
            this.code = code;
            this.str = str;
        }

        public Integer getCode() {
            return code;
        }

        public String getInfo() {
            return str;
        }

    }
}

