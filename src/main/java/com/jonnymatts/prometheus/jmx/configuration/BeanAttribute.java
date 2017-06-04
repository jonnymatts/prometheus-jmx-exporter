package com.jonnymatts.prometheus.jmx.configuration;

public class BeanAttribute {
    private String name;
    private MetricType type;
    private MetricCollector collector;

    public BeanAttribute() {}

    public BeanAttribute(String name, MetricType type, MetricCollector collector) {
        this.name = name;
        this.type = type;
        this.collector = collector;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MetricType getType() {
        return type;
    }

    public void setType(MetricType type) {
        this.type = type;
    }

    public MetricCollector getCollector() {
        return collector;
    }

    public void setCollector(MetricCollector collector) {
        this.collector = collector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanAttribute that = (BeanAttribute) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != that.type) return false;
        return collector == that.collector;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (collector != null ? collector.hashCode() : 0);
        return result;
    }
}