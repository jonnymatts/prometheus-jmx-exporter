package com.jonnymatts.prometheus.jmx.configuration;

public class ExponentialBucketConfiguration {
    private double start;
    private double factor;
    private int count;

    public ExponentialBucketConfiguration() {}

    public ExponentialBucketConfiguration(double start, double factor, int count) {
        this.start = start;
        this.factor = factor;
        this.count = count;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getFactor() {
        return factor;
    }

    public void setFactor(double factor) {
        this.factor = factor;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ExponentialBucketConfiguration that = (ExponentialBucketConfiguration) o;

        if (Double.compare(that.start, start) != 0) return false;
        if (Double.compare(that.factor, factor) != 0) return false;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(start);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(factor);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + count;
        return result;
    }
}