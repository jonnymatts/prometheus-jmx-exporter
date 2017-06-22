package com.jonnymatts.prometheus.jmx.configuration;

public class LinearBucketConfiguration {
    private double start;
    private double width;
    private int count;

    public LinearBucketConfiguration() {}

    public LinearBucketConfiguration(double start, double width, int count) {
        this.start = start;
        this.width = width;
        this.count = count;
    }

    public double getStart() {
        return start;
    }

    public void setStart(double start) {
        this.start = start;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
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

        LinearBucketConfiguration that = (LinearBucketConfiguration) o;

        if (Double.compare(that.start, start) != 0) return false;
        if (Double.compare(that.width, width) != 0) return false;
        return count == that.count;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(start);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(width);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + count;
        return result;
    }
}