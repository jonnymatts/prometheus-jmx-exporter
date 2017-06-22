package com.jonnymatts.prometheus.jmx.configuration;

public class BeanAttributeMetricCollectorReference {
    private MetricCollectorType type;
    private String name;

    public BeanAttributeMetricCollectorReference() {}

    public BeanAttributeMetricCollectorReference(MetricCollectorType type, String name) {
        this.type = type;
        this.name = name;
    }

    public BeanAttributeMetricCollectorReference(MetricCollectorType type) {
        this.type = type;
    }

    public MetricCollectorType getType() {
        return type;
    }

    public void setType(MetricCollectorType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BeanAttributeMetricCollectorReference that = (BeanAttributeMetricCollectorReference) o;

        if (type != that.type) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}