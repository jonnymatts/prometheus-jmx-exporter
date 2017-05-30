package com.jonnymatts.prometheus.jmx;

import java.util.List;

public class Configuration {
    private List<Bean> beans;

    public Configuration() {}

    public Configuration(List<Bean> beans) {
        this.beans = beans;
    }

    public List<Bean> getBeans() {
        return beans;
    }

    public void setBeans(List<Bean> beans) {
        this.beans = beans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Configuration that = (Configuration) o;

        return beans != null ? beans.equals(that.beans) : that.beans == null;
    }

    @Override
    public int hashCode() {
        return beans != null ? beans.hashCode() : 0;
    }
}