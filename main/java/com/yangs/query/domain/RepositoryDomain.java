package an.dy.domain.repository;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: yangs
 * @Description:
 * @Date: 2021/9/2 11:30
 */
public class RepositoryDomain {

    private Object value;

    private String type;

    public RepositoryDomain() {
    }

    public RepositoryDomain(Object value, String type) {
        this.value = value;
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}

