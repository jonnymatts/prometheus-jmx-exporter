package com.jonnymatts.prometheus.jmx.configuration;

import java.util.List;

public class Bean {
    private String name;
    private List<BeanAttribute> attributes;

    public Bean() {}

    public Bean(String name, List<BeanAttribute> attributes) {
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BeanAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<BeanAttribute> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bean bean = (Bean) o;

        if (name != null ? !name.equals(bean.name) : bean.name != null) return false;
        return attributes != null ? attributes.equals(bean.attributes) : bean.attributes == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }
}