package com.jonnymatts.prometheus.jmx.configuration;

public class QuantileConfiguration {
    private Double quantile;
    private Double error;

    public QuantileConfiguration() {}

    public QuantileConfiguration(Double quantile, Double error) {
        this.quantile = quantile;
        this.error = error;
    }

    public double getQuantile() {
        return quantile;
    }

    public void setQuantile(double quantile) {
        this.quantile = quantile;
    }

    public double getError() {
        return error;
    }

    public void setError(double error) {
        this.error = error;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuantileConfiguration that = (QuantileConfiguration) o;

        if (Double.compare(that.quantile, quantile) != 0) return false;
        return Double.compare(that.error, error) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(quantile);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(error);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}