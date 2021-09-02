package com.yangs.query.domain;

/**
 * @Author: yangs
 * @Description:
 * @Date: 2021/9/2 13:49
 */
public final class RepositoryDomainBuilder {
    private Object value;
    private Integer type;

    private RepositoryDomainBuilder() {
    }

    public static RepositoryDomainBuilder builder() {
        return new RepositoryDomainBuilder();
    }

    public RepositoryDomainBuilder withValue(Object value) {
        this.value = value;
        return this;
    }

    public RepositoryDomainBuilder withType(Integer type) {
        this.type = type;
        return this;
    }

    public RepositoryDomain build() {
        RepositoryDomain repositoryDomain = new RepositoryDomain();
        repositoryDomain.setValue(value);
        repositoryDomain.setType(type);
        return repositoryDomain;
    }
}
