package com.jonnymatts.prometheus.jmx.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BeanAttribute {
    private String name;
    private MetricType type;
    @JsonProperty("collector")
    private BeanAttributeMetricCollectorReference collectorReference;

    public BeanAttribute() {}

    public BeanAttribute(String name, MetricType type, BeanAttributeMetricCollectorReference collectorReference) {
        this.name = name;
        this.type = type;
        this.collectorReference = collectorReference;
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

    public BeanAttributeMetricCollectorReference getCollectorReference() {
        return collectorReference;
    }

    public void setCollectorReference(BeanAttributeMetricCollectorReference collectorReference) {
        this.collectorReference = collectorReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanAttribute that = (BeanAttribute) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (type != that.type) return false;
        return collectorReference != null ? collectorReference.equals(that.collectorReference) : that.collectorReference == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (collectorReference != null ? collectorReference.hashCode() : 0);
        return result;
    }
}